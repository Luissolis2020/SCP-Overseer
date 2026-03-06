package net.luis.scpmod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportToRoomPacket {

    private final BlockPos origin;
    private final int width;
    private final int depth;

    public TeleportToRoomPacket(BlockPos origin, int width, int depth) {
        this.origin = origin;
        this.width = width;
        this.depth = depth;
    }

    public TeleportToRoomPacket(FriendlyByteBuf buf) {
        this.origin = buf.readBlockPos();
        this.width = buf.readVarInt();
        this.depth = buf.readVarInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(origin);
        buf.writeVarInt(width);
        buf.writeVarInt(depth);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();

            if (player != null) {
                var manager = net.luis.scpmod.facility.building.FacilityManager.get(player.serverLevel());
                var room = manager.getRooms().stream().filter(r -> r.getOrigin().equals(origin)).findFirst().orElse(null);
                if (room == null || !net.luis.scpmod.network.SCPModNetwork.canAccessRoom(player, room)) {
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c[Facility] Access denied."));
                    context.get().setPacketHandled(true);
                    return;
                }
                if (player.isCreative() && !player.isPassenger()) {
                    double x = origin.getX() + (width / 2.0);
                    double y = origin.getY() + 1.0; // One block above floor
                    double z = origin.getZ() + (depth / 2.0);
                    player.teleportTo(x, y, z);
                    player.sendSystemMessage(Component.literal("§7[Debug] §aTeleported to room center at " + origin.toShortString()));
                } else if (!player.isCreative()) {
                    player.sendSystemMessage(Component.literal("§c[Facility] You must be in creative mode to teleport."));
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
