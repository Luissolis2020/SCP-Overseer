package net.luis.scpmod.network;

import net.luis.scpmod.pda.MissionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMissionCompletePacket {
    private final String id;
    private final Component title;

    public ClientboundMissionCompletePacket(String id, Component title) {
        this.id = id;
        this.title = title;
    }

    public ClientboundMissionCompletePacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.title = buf.readComponent();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeComponent(title);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(ClientboundMissionCompletePacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MissionManager.markCompletedFromServer(pkt.id, pkt.title);
        });
        ctx.get().setPacketHandled(true);
    }
}
