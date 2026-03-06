package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.research.ResearchCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundResearchCatalogPacket {

    public record Entry(
            String id,
            ResearchCategory category,
            String name,
            String desc,
            int cost,
            int sort,
            List<String> requires,
            int tier,
            boolean hiddenUntilPrereq,
            boolean repeatable
    ) {}

    private final List<Entry> entries;

    public ClientboundResearchCatalogPacket(List<Entry> entries) {
        this.entries = entries;
    }

    public ClientboundResearchCatalogPacket(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<Entry> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String id = buf.readUtf(64);
            ResearchCategory cat = buf.readEnum(ResearchCategory.class);
            String name = buf.readUtf(256);
            String desc = buf.readUtf(2048);
            int cost = buf.readVarInt();
            int sort = buf.readVarInt();
            int tier = buf.readVarInt();
            boolean hidden = buf.readBoolean();
            boolean repeatable = buf.readBoolean();

            int rN = buf.readVarInt();
            List<String> req = new ArrayList<>(rN);
            for (int r = 0; r < rN; r++) req.add(buf.readUtf());

            list.add(new Entry(id, cat, name, desc, cost, sort, req, tier, hidden, repeatable));
        }
        this.entries = list;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entries.size());
        for (Entry e : entries) {
            buf.writeUtf(e.id(), 64);
            buf.writeEnum(e.category());
            buf.writeUtf(e.name(), 256);
            buf.writeUtf(e.desc(), 2048);
            buf.writeVarInt(e.cost());
            buf.writeVarInt(e.sort());
            buf.writeVarInt(e.tier());
            buf.writeBoolean(e.hiddenUntilPrereq());
            buf.writeBoolean(e.repeatable());

            buf.writeVarInt(e.requires().size());
            for (String r : e.requires()) buf.writeUtf(r, 64);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientPDADataCache.setResearchCatalog(entries));
        ctx.get().setPacketHandled(true);
    }
}
