package net.luis.scpmod.network;

import net.luis.scpmod.entity.custom.CursedPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCursedPacket {
    private final boolean isCursed;

    public SyncCursedPacket(boolean isCursed) {
        this.isCursed = isCursed;
    }

    // Decode method to read from packet buffer
    public static SyncCursedPacket decode(FriendlyByteBuf buf) {
        return new SyncCursedPacket(buf.readBoolean());
    }

    // Encode method to write data to packet buffer
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isCursed);
    }

    // Handle packet logic on the client side
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(this));
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static final class ClientHandler {
        private static void handle(SyncCursedPacket packet) {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if (player == null) return;
            CursedPlayer.setCursed(player, packet.isCursed);
        }
    }
}
