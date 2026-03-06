package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ClientboundPDASyncPacket {

    private final Set<String> unlockedTabs;
    private final String scpSortMode;
    private final int researchPoints;
    private final Set<String> boughtUnlocks;

    public ClientboundPDASyncPacket(Set<String> unlockedTabs, String scpSortMode, int researchPoints, Set<String> boughtUnlocks) {
        this.unlockedTabs = unlockedTabs;
        this.scpSortMode = scpSortMode;
        this.researchPoints = researchPoints;
        this.boughtUnlocks = boughtUnlocks;
    }

    public ClientboundPDASyncPacket(FriendlyByteBuf buf) {
        int tabCount = buf.readVarInt();
        this.unlockedTabs = new HashSet<>();
        for (int i = 0; i < tabCount; i++) unlockedTabs.add(buf.readUtf());

        this.scpSortMode = buf.readUtf();
        this.researchPoints = buf.readVarInt();

        int boughtCount = buf.readVarInt();
        this.boughtUnlocks = new HashSet<>();
        for (int i = 0; i < boughtCount; i++) {
            boughtUnlocks.add(buf.readUtf(64));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(unlockedTabs.size());
        for (String tab : unlockedTabs) buf.writeUtf(tab);

        buf.writeUtf(scpSortMode);
        buf.writeVarInt(researchPoints);

        buf.writeVarInt(boughtUnlocks.size());
        for (String id : boughtUnlocks) buf.writeUtf(id, 64);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(ClientboundPDASyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPDADataCache.setUnlockedTabs(pkt.unlockedTabs);
            ClientPDADataCache.setSCPSortMode(pkt.scpSortMode);
            ClientPDADataCache.setResearchPoints(pkt.researchPoints);
            ClientPDADataCache.setBoughtResearchUnlocks(pkt.boughtUnlocks);
        });
        ctx.get().setPacketHandled(true);
    }
}
