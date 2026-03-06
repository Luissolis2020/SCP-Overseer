package net.luis.scpmod.network;

import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RoomHighlightRequestPacket {
    private final BlockPos origin;

    public RoomHighlightRequestPacket(BlockPos origin) {
        this.origin = origin;
    }

    public RoomHighlightRequestPacket(FriendlyByteBuf buf) {
        this.origin = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(origin);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            FacilityManager manager = FacilityManager.get(player.serverLevel());
            FacilityRoomInstance room = manager.getRooms().stream()
                    .filter(r -> r.getOrigin().equals(origin))
                    .findFirst()
                    .orElse(null);

            if (room == null || !SCPModNetwork.canAccessRoom(player, room)) {
                player.sendSystemMessage(Component.literal("§c[Facility] Access denied."));
                return;
            }

            SCPModNetwork.sendToPlayer(new RoomHighlightPacket(origin), player);
        });
        context.get().setPacketHandled(true);
    }

    public BlockPos getOrigin() {
        return origin;
    }
}
