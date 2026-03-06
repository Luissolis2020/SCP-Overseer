package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPDADataCache;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundFacilityRoomsPacket {
    private final List<FacilityRoomData> rooms;

    public static record FacilityRoomData(
            String displayName,
            String state,
            BlockPos origin,
            ResourceLocation blueprintId,
            int width,
            int height,
            int depth,
            boolean isActive,
            int progressPercent,
            boolean builderAssigned
    ) {}

    public ClientboundFacilityRoomsPacket(List<FacilityRoomData> rooms) {
        this.rooms = rooms;
    }

    public ClientboundFacilityRoomsPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.rooms = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String name = buf.readUtf();
            String state = buf.readUtf();
            BlockPos origin = buf.readBlockPos();
            ResourceLocation blueprintId = buf.readResourceLocation();
            int width = buf.readVarInt();
            int height = buf.readVarInt();
            int depth = buf.readVarInt();
            boolean isActive = buf.readBoolean();
            int progress = buf.readVarInt();
            boolean hasBuilder = buf.readBoolean();
            rooms.add(new FacilityRoomData(name, state, origin, blueprintId, width, height, depth, isActive, progress, hasBuilder));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(rooms.size());
        for (FacilityRoomData data : rooms) {
            buf.writeUtf(data.displayName());
            buf.writeUtf(data.state());
            buf.writeBlockPos(data.origin());
            buf.writeResourceLocation(data.blueprintId());
            buf.writeVarInt(data.width());
            buf.writeVarInt(data.height());
            buf.writeVarInt(data.depth());
            buf.writeBoolean(data.isActive());
            buf.writeVarInt(data.progressPercent());
            buf.writeBoolean(data.builderAssigned());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(ClientboundFacilityRoomsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        //System.out.println("[ClientboundFacilityRoomsPacket] Synced " + packet.rooms.size() + " rooms to client.");
        ctx.get().enqueueWork(() -> {
            ClientPDADataCache.setFacilityRooms(packet.rooms);
        });
        ctx.get().setPacketHandled(true);
    }

    public List<FacilityRoomData> getRooms() {
        return rooms;
    }
}
