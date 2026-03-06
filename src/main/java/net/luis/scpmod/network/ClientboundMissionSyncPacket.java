package net.luis.scpmod.network;

import net.luis.scpmod.pda.Mission;
import net.luis.scpmod.pda.MissionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMissionSyncPacket {
    private final String id;
    private final Component title;
    private final Component objective;
    private final int current;
    private final int required;

    public ClientboundMissionSyncPacket(Mission mission) {
        this.id = mission.getId();
        this.title = mission.getTitle();
        this.objective = mission.getObjective();
        this.current = mission.getCurrentProgress();
        this.required = mission.getRequiredProgress();
    }

    public ClientboundMissionSyncPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.title = buf.readComponent();
        this.objective = buf.readComponent();
        this.current = buf.readVarInt();
        this.required = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeComponent(title);
        buf.writeComponent(objective);
        buf.writeVarInt(current);
        buf.writeVarInt(required);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(ClientboundMissionSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Mission m = new Mission(pkt.id, pkt.title, pkt.objective, pkt.required, pkt.current);
            MissionManager.applyFromNetwork(m);
        });
        ctx.get().setPacketHandled(true);
    }
}
