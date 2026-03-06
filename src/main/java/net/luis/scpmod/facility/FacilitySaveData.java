package net.luis.scpmod.facility;

import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.facility.construction.ConstructionState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.*;

public class FacilitySaveData extends SavedData {

    private final Map<BlockPos, SavedConstructionSite> sites = new HashMap<>();
    private final Map<BlockPos, SavedFacilityRoom> rooms = new HashMap<>();

    public static FacilitySaveData load(CompoundTag tag) {
        FacilitySaveData data = new FacilitySaveData();

        ListTag siteList = tag.getList("Sites", Tag.TAG_COMPOUND);
        for (Tag siteTag : siteList) {
            SavedConstructionSite saved = SavedConstructionSite.load((CompoundTag) siteTag);
            data.sites.put(saved.pos, saved);
        }

        ListTag roomList = tag.getList("Rooms", Tag.TAG_COMPOUND);
        for (Tag roomTag : roomList) {
            SavedFacilityRoom savedRoom = SavedFacilityRoom.load((CompoundTag) roomTag);
            data.rooms.put(savedRoom.pos(), savedRoom);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag siteList = new ListTag();
        for (SavedConstructionSite saved : sites.values()) {
            siteList.add(saved.save());
        }
        tag.put("Sites", siteList);

        ListTag roomList = new ListTag();
        for (SavedFacilityRoom savedRoom : rooms.values()) {
            roomList.add(savedRoom.save());
        }
        tag.put("Rooms", roomList);

        return tag;
    }

    public void addSite(BlockPos pos, SavedConstructionSite site) {
        sites.put(pos, site);
        setDirty();
    }

    public Map<BlockPos, SavedConstructionSite> getSites() {
        return sites;
    }

    public Map<BlockPos, SavedFacilityRoom> getSavedRooms() {
        return rooms;
    }

    public void addRoom(SavedFacilityRoom room) {
        rooms.put(room.pos(), room);
        setDirty();
    }

    public record SavedConstructionSite(
            BlockPos pos,
            ResourceLocation structureId,
            StructurePlaceSettings settings,
            List<BlockPos> digTargets,
            List<ConstructionSite.BlockPlacementTask> buildTasks,
            List<ConstructionSite.BlockPlacementTask> deferredTasks,
            List<ConstructionSite.BlockPlacementTask> scaffoldTasks,
            List<BlockPos> placedScaffolds,
            int totalWorkUnits,
            Map<BlockPos, ConstructionSite.BuildStage> buildStages
    ) {
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Pos", pos.asLong());
            tag.putString("StructureId", structureId.toString());
            tag.putInt("Rotation", settings.getRotation().ordinal());
            tag.putInt("Mirror", settings.getMirror().ordinal());
            tag.putBoolean("IgnoreEntities", settings.isIgnoreEntities());
            tag.putBoolean("KeepLiquids", settings.shouldKeepLiquids());

            ListTag digList = new ListTag();
            for (BlockPos dig : digTargets) {
                digList.add(LongTag.valueOf(dig.asLong()));
            }
            tag.put("DigTargets", digList);

            ListTag stageList = new ListTag();
            for (Map.Entry<BlockPos, ConstructionSite.BuildStage> entry : buildStages.entrySet()) {
                CompoundTag stageTag = new CompoundTag();
                stageTag.putLong("Pos", entry.getKey().asLong());
                stageTag.putString("Stage", entry.getValue().name());
                stageList.add(stageTag);
            }
            tag.put("BuildStages", stageList);

            tag.put("BuildTasks", writePlacementTasks(buildTasks));
            tag.put("DeferredTasks", writePlacementTasks(deferredTasks));

            tag.putInt("TotalWorkUnits", totalWorkUnits);

            tag.put("ScaffoldTasks", writePlacementTasks(scaffoldTasks));

            ListTag placedList = new ListTag();
            for (BlockPos p : placedScaffolds) {
                placedList.add(LongTag.valueOf(p.asLong()));
            }
            tag.put("PlacedScaffolds", placedList);

            return tag;
        }

        public static SavedConstructionSite load(CompoundTag tag) {
            BlockPos pos = BlockPos.of(tag.getLong("Pos"));
            ResourceLocation id = ResourceLocation.parse(tag.getString("StructureId"));
            StructurePlaceSettings settings = new StructurePlaceSettings();
            settings.setRotation(Rotation.values()[tag.getInt("Rotation")]);
            settings.setMirror(Mirror.values()[tag.getInt("Mirror")]);
            settings.setIgnoreEntities(tag.getBoolean("IgnoreEntities"));
            settings.setKeepLiquids(tag.getBoolean("KeepLiquids"));

            List<BlockPos> digTargets = new ArrayList<>();
            ListTag digList = tag.getList("DigTargets", Tag.TAG_LONG);
            for (Tag t : digList) {
                digTargets.add(BlockPos.of(((LongTag) t).getAsLong()));
            }

            List<ConstructionSite.BlockPlacementTask> buildTasks = readPlacementTasks(tag.getList("BuildTasks", Tag.TAG_COMPOUND));
            List<ConstructionSite.BlockPlacementTask> deferredTasks = readPlacementTasks(tag.getList("DeferredTasks", Tag.TAG_COMPOUND));
            int total = tag.getInt("TotalWorkUnits");

            Map<BlockPos, ConstructionSite.BuildStage> buildStages = new HashMap<>();
            ListTag stageList = tag.getList("BuildStages", Tag.TAG_COMPOUND);
            for (Tag t : stageList) {
                CompoundTag stageTag = (CompoundTag) t;
                BlockPos bpos = BlockPos.of(stageTag.getLong("Pos"));
                ConstructionSite.BuildStage stage = ConstructionSite.BuildStage.valueOf(stageTag.getString("Stage"));
                buildStages.put(bpos, stage);
            }

            List<ConstructionSite.BlockPlacementTask> scaffoldTasks =
                    readPlacementTasks(tag.getList("ScaffoldTasks", Tag.TAG_COMPOUND));

            List<BlockPos> placedScaffolds = new ArrayList<>();
            ListTag placedList = tag.getList("PlacedScaffolds", Tag.TAG_LONG);
            for (Tag t : placedList) {
                placedScaffolds.add(BlockPos.of(((LongTag) t).getAsLong()));
            }

            return new SavedConstructionSite(
                    pos, id, settings,
                    digTargets,
                    buildTasks,
                    deferredTasks,
                    scaffoldTasks,
                    placedScaffolds,
                    total,
                    buildStages
            );
        }

        private static ListTag writePlacementTasks(List<ConstructionSite.BlockPlacementTask> tasks) {
            ListTag list = new ListTag();
            for (ConstructionSite.BlockPlacementTask task : tasks) {
                CompoundTag taskTag = new CompoundTag();
                taskTag.putLong("Pos", task.worldPos().asLong());

                Block block = task.state().getBlock();
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                taskTag.putString("Block", id.toString());

                CompoundTag props = new CompoundTag();
                for (Map.Entry<Property<?>, Comparable<?>> entry : task.state().getValues().entrySet()) {
                    props.putString(entry.getKey().getName(), entry.getValue().toString());
                }
                taskTag.put("Properties", props);

                list.add(taskTag);
            }
            return list;
        }

        public static List<ConstructionSite.BlockPlacementTask> readPlacementTasks(ListTag list) {
            List<ConstructionSite.BlockPlacementTask> result = new ArrayList<>();
            for (Tag t : list) {
                CompoundTag taskTag = (CompoundTag) t;
                BlockPos pos = BlockPos.of(taskTag.getLong("Pos"));

                Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(taskTag.getString("Block")))
                        .orElse(Blocks.AIR);
                if (block == Blocks.AIR) {
                    System.out.println("[FacilitySaveData] Warning: unknown block ID '" + taskTag.getString("Block") + "' — using AIR.");
                }

                BlockState state = block.defaultBlockState();

                if (taskTag.contains("Properties")) {
                    CompoundTag props = taskTag.getCompound("Properties");
                    for (String key : props.getAllKeys()) {
                        Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
                        if (property != null) {
                            state = setPropertyFromString(state, property, props.getString(key));
                        }
                    }
                }

                result.add(new ConstructionSite.BlockPlacementTask(pos, state));
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        private static <T extends Comparable<T>> BlockState setPropertyFromString(BlockState state, Property<T> property, String valueStr) {
            Optional<T> optional = property.getValue(valueStr);
            return optional.map(value -> state.setValue(property, value)).orElse(state);
        }
    }

    // === SAVED ROOM ===

    public static record SavedFacilityRoom(
            BlockPos pos,
            ResourceLocation structureId,
            boolean built,
            boolean active,
            ConstructionState state,
            UUID owner,
            int rotation
    ) {
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Pos", pos.asLong());
            tag.putString("StructureId", structureId.toString());
            tag.putBoolean("Built", built);
            tag.putBoolean("Active", active);
            tag.putString("State", state.name());
            if (owner != null) {
                tag.putString("Owner", owner.toString());
            }
            tag.putInt("Rotation", rotation);
            return tag;
        }

        public static SavedFacilityRoom load(CompoundTag tag) {
            BlockPos pos = BlockPos.of(tag.getLong("Pos"));
            ResourceLocation id = ResourceLocation.parse(tag.getString("StructureId"));
            boolean built = tag.getBoolean("Built");
            boolean active = tag.getBoolean("Active");
            ConstructionState state = ConstructionState.valueOf(tag.getString("State"));
            UUID owner = null;
            if (tag.contains("Owner", Tag.TAG_STRING)) {
                try {
                    owner = UUID.fromString(tag.getString("Owner"));
                } catch (Exception ignored) {}
            }
            int rot = tag.contains("Rotation") ? tag.getInt("Rotation") : Rotation.NONE.ordinal();
            return new SavedFacilityRoom(pos, id, built, active, state, owner, rot);
        }
    }
}
