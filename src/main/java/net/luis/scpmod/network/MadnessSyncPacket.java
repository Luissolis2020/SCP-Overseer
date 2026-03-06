package net.luis.scpmod.network;

import net.luis.scpmod.capability.MadnessCapability;
import net.luis.scpmod.capability.MadnessProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
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
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                MadnessCapability madness = MadnessProvider.get(player);
                madness.setMadnessLevel(msg.madnessLevel);
            }
        });
        ctx.get().setPacketHandled(true);
    }



}
