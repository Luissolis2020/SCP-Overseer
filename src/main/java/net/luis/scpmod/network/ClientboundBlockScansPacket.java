package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundBlockScansPacket {

    private final List<String> ids;

    public ClientboundBlockScansPacket(List<String> ids) {
        this.ids = ids;
    }

    public ClientboundBlockScansPacket(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<String> tmp = new ArrayList<>(n);
        for (int i = 0; i < n; i++) tmp.add(buf.readUtf());
        this.ids = tmp;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(ids.size());
        for (String s : ids) buf.writeUtf(s);
    }

    public static void handle(ClientboundBlockScansPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientPDADataCache.setScannedBlocks(pkt.ids));
        ctx.get().setPacketHandled(true);
    }
}

