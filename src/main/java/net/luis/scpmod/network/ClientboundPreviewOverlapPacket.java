package net.luis.scpmod.network;

import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.facility.FacilityOverlapChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundPreviewOverlapPacket {

    private final boolean overlapping;
    private final List<AABB> overlaps;

    public ClientboundPreviewOverlapPacket(boolean overlapping, List<AABB> overlaps) {
        this.overlapping = overlapping;
        this.overlaps = overlaps;
    }

    public ClientboundPreviewOverlapPacket(FriendlyByteBuf buf) {
        this.overlapping = buf.readBoolean();
        int n = buf.readVarInt();
        this.overlaps = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            // store as ints (block-aligned)
            int minX = buf.readInt();
            int minY = buf.readInt();
            int minZ = buf.readInt();
            int maxX = buf.readInt();
            int maxY = buf.readInt();
            int maxZ = buf.readInt();
            overlaps.add(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(overlapping);
        buf.writeVarInt(overlaps.size());
        for (AABB a : overlaps) {
            buf.writeInt((int) a.minX);
            buf.writeInt((int) a.minY);
            buf.writeInt((int) a.minZ);
            buf.writeInt((int) a.maxX);
            buf.writeInt((int) a.maxY);
            buf.writeInt((int) a.maxZ);
        }
    }

    public static ClientboundPreviewOverlapPacket fromServer(ServerLevel level, BlockPos origin, StructureTemplate template, StructurePlaceSettings settings) {
        boolean overlap = FacilityOverlapChecker.isOverlapping(level, origin, template, settings);
        List<AABB> boxes = FacilityOverlapChecker.getOverlappingBoundingBoxes(level, origin, template, settings);
        return new ClientboundPreviewOverlapPacket(overlap, boxes);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPreviewState.setServerOverlaps(overlaps, overlapping);
        });
        ctx.get().setPacketHandled(true);
    }
}
