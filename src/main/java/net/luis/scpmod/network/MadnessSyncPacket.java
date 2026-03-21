package net.luis.scpmod.network;

import net.luis.scpmod.capability.MadnessCapability;
import net.luis.scpmod.capability.MadnessProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MadnessSyncPacket {
    private final int madnessLevel;

    public MadnessSyncPacket(int madnessLevel) {
        this.madnessLevel = madnessLevel;
    }

    public static void encode(MadnessSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.madnessLevel);
    }

    public static MadnessSyncPacket decode(FriendlyByteBuf buf) {
        return new MadnessSyncPacket(buf.readInt());
    }

    public static void handle(MadnessSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(msg));
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static final class ClientHandler {
        private static void handle(MadnessSyncPacket msg) {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if (player == null) return;
            MadnessCapability madness = MadnessProvider.get(player);
            madness.setMadnessLevel(msg.madnessLevel);
        }
    }
}
