package net.luis.scpmod.network;

import net.luis.scpmod.facility.FacilityOverlapChecker;
import net.luis.scpmod.facility.FacilitySaveData;
import net.luis.scpmod.facility.building.FacilityBlueprint;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.facility.construction.ConstructionState;
import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfirmFacilityPlacementPacket {
    private final BlockPos pos;
    private final ResourceLocation structureId;
    private final Rotation rotation;

    public ConfirmFacilityPlacementPacket(BlockPos pos, ResourceLocation structureId, Rotation rotation) {
        this.pos = pos;
        this.structureId = structureId;
        this.rotation = rotation;
    }

    public ConfirmFacilityPlacementPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.structureId = buf.readResourceLocation();
        this.rotation = buf.readEnum(Rotation.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(structureId);
        buf.writeEnum(rotation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = (ServerLevel) player.level();
            StructureTemplate template = player.server.getStructureManager().getOrCreate(structureId);
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation);

            if (FacilityOverlapChecker.isOverlapping(level, pos, template, settings)) {
                player.sendSystemMessage(Component.literal("Cannot place: structure overlaps existing facility."));
                return;
            }

            FacilityOverlapChecker.registerStructurePlacement(level, pos, template, settings);

            // Create and register room
            FacilityBlueprint blueprint = FacilityBlueprintLoader.get(structureId);
            FacilityRoomInstance room = new FacilityRoomInstance(blueprint, pos, rotation);
            room.state = ConstructionState.PLANNED;
            room.setOwner(player.getUUID());

            ConstructionSite site = new ConstructionSite(pos, structureId, template, settings);
            room.linkConstructionSite(site);

            FacilityManager manager = FacilityManager.get(level);
            manager.addRoom(room);

            manager.saveData.addRoom(new FacilitySaveData.SavedFacilityRoom(
                    room.getOrigin(),
                    room.blueprint.id,
                    false,
                    false,
                    room.state,
                    room.getOwner(),
                    rotation.ordinal()
            ));

            // Enqueue dig and build targets
            int digCount = 0;
            int placeCount = 0;

            BlockPos base = pos;
            int width = template.getSize().getX();
            int height = template.getSize().getY();
            int depth = template.getSize().getZ();

            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    for (int x = 0; x < width; x++) {
                        BlockPos offset = new BlockPos(x, y, z);
                        BlockPos digPos = StructureTemplate.calculateRelativePosition(settings, offset).offset(base);
                        BlockState existing = level.getBlockState(digPos);
                        BlockState expected = site.getExpectedBlockState(digPos); // null => AIR
                        // skip bedrock
                        if (existing.is(Blocks.BEDROCK)) continue;

                        // only enqueue dig if it truly needs clearing
                        if (ConstructionSite.shouldStillBeDug(level, digPos, expected)) {
                            site.enqueueDigTarget(digPos);
                            digCount++;
                        }
                    }
                }
            }

            // Enqueue ALL non-air expected blocks
            for (var e : site.getExpectedBlockStates().entrySet()) {
                BlockPos worldPos = e.getKey();
                BlockState state  = e.getValue();

                site.enqueuePlacement(level, worldPos, state);
                placeCount++;
            }

            site.recalculateTotalWorkUnits();

            int buildQ = site.getBuildLayerQueues().stream().mapToInt(java.util.Queue::size).sum();
            int defQ   = site.getDeferredLayerQueues().stream().mapToInt(java.util.Queue::size).sum();
            int digQ   = site.getDigLayers().values().stream().mapToInt(java.util.Queue::size).sum();

            System.out.println("[ConfirmPlace] expected=" + site.getExpectedBlockStates().size()
                    + " digQ=" + digQ + " buildQ=" + buildQ + " defQ=" + defQ
                    + " totalWork=" + site.getTotalWorkUnits());

            manager.registerConstructionSite(site);

            player.sendSystemMessage(Component.literal("Facility registered and awaiting construction."));

            SCPModNetwork.syncPDAStateTo(player);
        });

        ctx.get().setPacketHandled(true);
    }


}
