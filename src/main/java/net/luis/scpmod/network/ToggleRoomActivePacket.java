package net.luis.scpmod.network;

import net.luis.scpmod.facility.FacilitySaveData;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleRoomActivePacket {

    private final BlockPos pos;

    public ToggleRoomActivePacket(BlockPos pos) {
        this.pos = pos;
    }

    public ToggleRoomActivePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            FacilityManager manager = FacilityManager.get(player.serverLevel());
            FacilityRoomInstance room = manager.getRooms().stream()
                    .filter(r -> r.getOrigin().equals(pos))
                    .findFirst().orElse(null);

            if (room != null && room.isBuilt()) {
                if (!SCPModNetwork.canAccessRoom(player, room)) {
                    player.sendSystemMessage(Component.literal("§c[Facility] Access denied."));
                    return;
                }
                boolean newState = !room.isActive();
                room.setActive(newState);

                manager.saveData.addRoom(new FacilitySaveData.SavedFacilityRoom(
                        room.getOrigin(),
                        room.blueprint.id,
                        room.isBuilt(),
                        room.isActive(),
                        room.state,
                        room.getOwner(),
                        room.getRotation().ordinal()
                ));

                if (newState) {
                    manager.turnOnLights(player.serverLevel(), room);
                } else {
                    manager.turnOffLights(player.serverLevel(), room);
                }

                player.sendSystemMessage(newState
                        ? Component.literal("§a[Facility] Room activated.")
                        : Component.literal("§e[Facility] Room deactivated."));

                SCPModNetwork.syncPDAStateTo(player);
            }
        });
        context.get().setPacketHandled(true);
    }

}
