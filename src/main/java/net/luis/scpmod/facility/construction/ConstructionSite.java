package net.luis.scpmod.facility.construction;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.*;

public class ConstructionSite {
    private final BlockPos origin;
    private final ResourceLocation structureId;
    private final StructureTemplate template;
    private final StructurePlaceSettings settings;

    private BuilderNPC assignedBuilder;
    private int progress;
    private ConstructionState state;

    private final TreeMap<Integer, Queue<BlockPos>> digLayers = new TreeMap<>(Comparator.naturalOrder());
    private final TreeMap<Integer, Queue<BlockPlacementTask>> buildLayers = new TreeMap<>(Comparator.naturalOrder());
    private final TreeMap<Integer, Queue<BlockPlacementTask>> deferredLayers = new TreeMap<>(Comparator.naturalOrder());
    private final Map<BlockPos, BlockState> expectedBlocks = new HashMap<>();
    private final TreeMap<Integer, Queue<BlockPlacementTask>> scaffoldLayers = new TreeMap<>(Comparator.naturalOrder());
    private final Set<BlockPos> placedScaffolds = new HashSet<>();

    private Integer currentDigLayer = null;
    private int totalWorkUnitsEnqueued = 0;

    private long lastSaveTick = -9999;
    private static final int SAVE_COOLDOWN_TICKS = 20; // 1 second

    private final Map<BlockPos, Integer> placeFailCount = new HashMap<>();
    private final Map<BlockPos, Long> placeCooldownUntil = new HashMap<>();

    // --- Constructor ---
    public ConstructionSite(BlockPos origin, ResourceLocation structureId, StructureTemplate template, StructurePlaceSettings settings) {
        this.origin = origin;
        this.structureId = structureId;
        this.template = template;
        this.settings = settings;
        this.state = ConstructionState.PLANNED;

        // Build expected block map immediately
//        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, settings, Blocks.AIR)) {
//            BlockPos worldPos = StructureTemplate.calculateRelativePosition(settings, info.pos()).offset(origin);
//            expectedBlocks.put(worldPos.immutable(), info.state());
//        }
        populateExpectedBlocks(template, settings);
        System.out.println("[Debug] expectedBlocks count = " + expectedBlocks.size() + " for site at " + origin);
        System.out.println("[Site] rot=" + settings.getRotation()
                + " templateSize=" + template.getSize()
                + " footprint=" + getFootprintWidth() + "x" + getHeight() + "x" + getFootprintDepth()
                + " expectedBlocks=" + expectedBlocks.size());
    }

    // --- Basic Info ---
    public BlockPos getOrigin() { return origin; }
    public ResourceLocation getStructureId() { return structureId; }
    public StructureTemplate getTemplate() { return template; }
    public StructurePlaceSettings getSettings() { return settings; }
    private final Map<BlockPos, BuildStage> buildStages = new HashMap<>();

    // --- Builder Tracking ---
    public boolean hasBuilder() { return assignedBuilder != null; }

    public void assignBuilder(BuilderNPC builder) {
        if (this.assignedBuilder != null && this.assignedBuilder != builder) {
            System.out.println("[ConstructionSite] Skipped assignment: site at " + origin + " already has a different builder.");
            return;
        }

        this.assignedBuilder = builder;

        if (this.state == ConstructionState.PLANNED) {
            this.state = ConstructionState.UNDER_CONSTRUCTION;
        }
    }

    public enum BuildStage {
        DIG_PENDING,
        DIG_DONE,
        PLACE_PENDING,
        PLACED
    }

    public void setBuilder(@Nullable BuilderNPC builder) {
        this.assignedBuilder = builder;
    }

    public BuilderNPC getBuilder() { return assignedBuilder; }

    public ConstructionState getState() { return state; }
    public void setState(ConstructionState state) { this.state = state; }

    public boolean isComplete() { return state == ConstructionState.BUILT || state == ConstructionState.ACTIVE; }

    // --- Digging System ---
    public void enqueueDigTarget(BlockPos pos) {
        if (buildStages.get(pos) == BuildStage.DIG_DONE) return;

        digLayers.computeIfAbsent(pos.getY(), y -> new LinkedList<>()).add(pos);
        buildStages.put(pos, BuildStage.DIG_PENDING);

        if (currentDigLayer == null || pos.getY() > currentDigLayer) {
            currentDigLayer = pos.getY();
        }
        markDirty();
    }

    @Nullable
    public BlockPos getNearbyDigTarget(ServerLevel level, BuilderNPC builder, BlockPos from, double radius) {
        if (digLayers.isEmpty()) {
            currentDigLayer = null;
            return null;
        }

        if (currentDigLayer == null || !digLayers.containsKey(currentDigLayer)) {
            currentDigLayer = digLayers.lastKey(); // highest Y
        }

        double bestDist = radius * radius;
        BlockPos bestPos = null;

        double bestFallbackDist = Double.POSITIVE_INFINITY;
        BlockPos bestFallback = null;

        int safety = 0;
        while (currentDigLayer != null && safety++ < 64) {
            final int layerY = currentDigLayer;
            Queue<BlockPos> layer = digLayers.get(layerY);

            if (layer == null || layer.isEmpty()) {
                digLayers.remove(layerY);
                currentDigLayer = digLayers.isEmpty() ? null : digLayers.lowerKey(layerY); // go downward
                continue;
            }

            Iterator<BlockPos> it = layer.iterator();
            while (it.hasNext()) {
                BlockPos pos = it.next();

                if (buildStages.get(pos) == BuildStage.DIG_DONE) {
                    it.remove();
                    continue;
                }

                BlockState expected = getExpectedBlockState(pos);
                if (!shouldStillBeDug(level, pos, expected)) {
                    buildStages.put(pos, BuildStage.DIG_DONE);
                    it.remove();
                    continue;
                }

                if (builder.isTaskBlacklisted(level, pos)) continue;

                BlockPos stand = builder.findStandPosForDig(level, pos);
                if (stand == null) {
                    builder.markBadTask(level, pos);
                    continue;
                }
                if (builder.isStandBlacklisted(level, stand)) continue;

                double dist = pos.distSqr(from);

                // Best within radius
                if (dist <= bestDist) {
                    bestDist = dist;
                    bestPos = pos;
                }

                // Fallback reachable (outside radius)
                if (dist < bestFallbackDist) {
                    bestFallbackDist = dist;
                    bestFallback = pos;
                }
            }

            if (bestPos != null) return bestPos;

            if (layer.isEmpty()) {
                digLayers.remove(layerY);
            }

            // Move down a layer and try again
            currentDigLayer = digLayers.isEmpty() ? null : digLayers.lowerKey(layerY);
        }

        return bestFallback;
    }

    public void markDigComplete(BlockPos pos) {
        if (buildStages.get(pos) == BuildStage.DIG_DONE) {
            return;
        }

        Queue<BlockPos> layer = digLayers.get(pos.getY());
        if (layer != null) {
            layer.remove(pos);
            if (layer.isEmpty()) {
                digLayers.remove(pos.getY());
                updateCurrentDigLayer();
            }
        }
        buildStages.put(pos, BuildStage.DIG_DONE);
        markDirty();
    }

    private void updateCurrentDigLayer() {
        currentDigLayer = digLayers.isEmpty() ? null : digLayers.lastKey(); // highest Y
    }

    public boolean hasBlocksToDig() { return !digLayers.isEmpty(); }
    public Integer getCurrentDigLayer() { return currentDigLayer; }
    public TreeMap<Integer, Queue<BlockPos>> getDigLayers() { return digLayers; }

    // --- Placement System ---
    public record BlockPlacementTask(BlockPos worldPos, BlockState state) {}

    public void enqueuePlacement(Level level, BlockPos pos, BlockState state) {
        // skip upper door half; placing lower will place+complete both
        if (state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            buildStages.put(pos, BuildStage.PLACED);
            return;
        }

        if (levelAlreadyMatches(level, pos, state)) {
            buildStages.put(pos, BuildStage.PLACED);
            return;
        } else if (buildStages.get(pos) != BuildStage.PLACED) {
            BlockPlacementTask task = new BlockPlacementTask(pos, state);
            (shouldDeferPlacement(state) ? deferredLayers : buildLayers)
                    .computeIfAbsent(pos.getY(), y -> new LinkedList<>()).add(task);
            buildStages.put(pos, BuildStage.PLACE_PENDING);
            markDirty();
            return;
        }

        System.out.println("[enqueuePlacement] WARNING: Redundant placement enqueue detected at " + pos);
    }

    private boolean levelAlreadyMatches(Level level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos).equals(state);
    }

    public Collection<Queue<BlockPlacementTask>> getBuildLayerQueues() {
        return buildLayers.values();
    }

    public Collection<Queue<BlockPlacementTask>> getDeferredLayerQueues() {
        return deferredLayers.values();
    }

    public boolean hasBlocksToPlace() {
        return !buildLayers.isEmpty() || !deferredLayers.isEmpty() || !scaffoldLayers.isEmpty();
    }

    public void markPlacementComplete(BlockPos pos) {
        boolean removedFromBuild = removePlacementFromAllLayers(buildLayers, pos);
        boolean removedFromDeferred = removePlacementFromAllLayers(deferredLayers, pos);

        if (!removedFromBuild && !removedFromDeferred && buildStages.get(pos) != BuildStage.PLACED) {
            System.out.println("[markPlacementComplete] WARNING: Task at " + pos + " not found in any queue!");
        }
        buildStages.put(pos, BuildStage.PLACED);
        markDirty();
    }

    private boolean removePlacementFromAllLayers(TreeMap<Integer, Queue<BlockPlacementTask>> layers, BlockPos pos) {
        boolean removed = false;

        Iterator<Map.Entry<Integer, Queue<BlockPlacementTask>>> it = layers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Queue<BlockPlacementTask>> entry = it.next();
            Queue<BlockPlacementTask> queue = entry.getValue();
            boolean wasRemoved = queue.removeIf(task -> task.worldPos().equals(pos));
            if (wasRemoved) removed = true;
            if (queue.isEmpty()) it.remove();
        }

        return removed;
    }

    // --- Expected State Map ---
    @Nullable
    public BlockState getExpectedBlockState(BlockPos pos) {
        BlockState st = expectedBlocks.get(pos);
        if (st != null) return st;

        if (isInsideFootprint(pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return null;
    }

    // --- Footprint Check ---
    public boolean isInsideFootprint(BlockPos pos) {
        int w = getFootprintWidth();
        int d = getFootprintDepth();

        return pos.getX() >= origin.getX()
                && pos.getX() < origin.getX() + w
                && pos.getY() >= origin.getY()
                && pos.getY() < origin.getY() + getHeight()
                && pos.getZ() >= origin.getZ()
                && pos.getZ() < origin.getZ() + d;
    }

    // --- Dimension Info ---
    public int getWidth() { return template.getSize().getX(); }
    public int getHeight() { return template.getSize().getY(); }
    public int getDepth() { return template.getSize().getZ(); }

    // --- Deferred Placement Types ---
    private boolean shouldDeferPlacement(BlockState state) {
        return state.is(Blocks.TORCH)
                || state.is(Blocks.WALL_TORCH)
                || state.is(Blocks.OAK_DOOR)
                || state.is(Blocks.SPRUCE_DOOR)
                || state.is(Blocks.BIRCH_DOOR)
                || state.is(Blocks.JUNGLE_DOOR)
                || state.is(Blocks.ACACIA_DOOR)
                || state.is(Blocks.DARK_OAK_DOOR)
                || state.is(Blocks.IRON_DOOR);
    }

    public int getTotalWorkUnits() {
        return totalWorkUnitsEnqueued;
    }

    public int getCompletedWorkUnits() {
        int remaining = digLayers.values().stream().mapToInt(Queue::size).sum()
                + buildLayers.values().stream().mapToInt(Queue::size).sum()
                + deferredLayers.values().stream().mapToInt(Queue::size).sum()
                + scaffoldLayers.values().stream().mapToInt(Queue::size).sum(); // ADD

        int total = Math.max(totalWorkUnitsEnqueued, remaining);
        int completed = total - remaining;

        return Math.max(0, Math.min(completed, total));
    }

    public static boolean shouldStillBeDug(ServerLevel level, BlockPos pos, @Nullable BlockState expectedState) {
        BlockState current = level.getBlockState(pos);

        if (expectedState == null) return false;

        BlockState expected = expectedState;

        if (current.equals(expected)) return false;

        if (expected.isAir()) {
            if (current.isAir()) return false;
            float hardness = current.getDestroySpeed(level, pos);
            return hardness >= 0.0F;
        }

        if (current.isAir()) return false;
        if (current.canBeReplaced()) return false;
        if (!current.getFluidState().isEmpty()) return true;
        if (!current.canOcclude()) return false;

        float hardness = current.getDestroySpeed(level, pos);
        return hardness >= 0.0F;
    }

    public void pruneInvalidDigTargets(ServerLevel level) {
        Iterator<Map.Entry<Integer, Queue<BlockPos>>> iter = digLayers.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Integer, Queue<BlockPos>> entry = iter.next();
            Queue<BlockPos> queue = entry.getValue();

            queue.removeIf(pos -> {
                BlockState expected = this.getExpectedBlockState(pos);
                BlockState current = level.getBlockState(pos);
                boolean shouldRemove = !shouldStillBeDug(level, pos, expected);

                if (shouldRemove) {
                    System.out.println("[pruneInvalidDigTargets] Removed dig target at " + pos +
                            " | expected=" + expected +
                            " | current=" + current);
                }

                return shouldRemove;
            });

            if (queue.isEmpty()) {
                iter.remove();
            }
        }

        updateCurrentDigLayer();
    }

    public void pruneInvalidPlacementTargets(ServerLevel level) {
        pruneLayeredPlacements(level, buildLayers);
        pruneLayeredPlacements(level, deferredLayers);
        pruneLayeredPlacements(level, scaffoldLayers);
    }

    private void pruneLayeredPlacements(ServerLevel level, TreeMap<Integer, Queue<BlockPlacementTask>> layers) {
        Iterator<Map.Entry<Integer, Queue<BlockPlacementTask>>> iter = layers.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Integer, Queue<BlockPlacementTask>> entry = iter.next();
            Queue<BlockPlacementTask> queue = entry.getValue();

            queue.removeIf(task -> level.getBlockState(task.worldPos()).equals(task.state()));

            if (queue.isEmpty()) {
                iter.remove();
            }
        }
    }

    public Map<BlockPos, BlockState> getExpectedBlockStates() {
        return this.expectedBlocks;
    }

    public void populateExpectedBlocks(StructureTemplate template, StructurePlaceSettings settings) {
        expectedBlocks.clear();

        var palettes = ((StructureTemplateAccessor) template).getPalettes();
        if (palettes.isEmpty()) return;

        // Use the primary palette only
        var palette = palettes.get(0);

        int included = 0;
        for (StructureTemplate.StructureBlockInfo info : palette.blocks()) {
            BlockState state = info.state();
            if (state.is(Blocks.STRUCTURE_VOID)) continue;

            if (state.isAir()) continue;

            BlockPos worldPos = StructureTemplate.calculateRelativePosition(settings, info.pos()).offset(origin);
            expectedBlocks.put(worldPos.immutable(), state);
            included++;
        }

        System.out.println("[populateExpectedBlocks] expected=" + included
                + " paletteCount=" + palettes.size()
                + " usingPalette=0"
                + " at " + origin);
    }

    public void setTotalWorkUnitsEnqueued(int total) {
        this.totalWorkUnitsEnqueued = total;
    }

    public void recalculateTotalWorkUnits() {
        int dig = digLayers.values().stream().mapToInt(Queue::size).sum();
        int build = buildLayers.values().stream().mapToInt(Queue::size).sum();
        int deferred = deferredLayers.values().stream().mapToInt(Queue::size).sum();
        int scaffold = scaffoldLayers.values().stream().mapToInt(Queue::size).sum();

        this.totalWorkUnitsEnqueued = dig + build + deferred + scaffold;
        System.out.println("[ConstructionSite] Recalculated total work units = " + this.totalWorkUnitsEnqueued);
    }

    public boolean isDiggingComplete() {
        return !hasBlocksToDig();
    }

    public Map<BlockPos, BuildStage> getBuildStages() {
        return this.buildStages;
    }

    public void markDirty() {
        if (this.assignedBuilder != null && this.assignedBuilder.level() instanceof ServerLevel serverLevel) {
            long now = serverLevel.getServer().getTickCount();
            if ((now - lastSaveTick) >= SAVE_COOLDOWN_TICKS) {
                lastSaveTick = now;
                FacilityManager.get(serverLevel).saveConstructionSite(this);
            }
        }
    }

    public boolean hasPendingWork() {
        return hasBlocksToDig() || hasBlocksToPlace();
    }

    public void deferPlacementTask(BlockPlacementTask task) {
        // Remove from build layers if present
        removePlacementFromAllLayers(buildLayers, task.worldPos());
        removePlacementFromAllLayers(deferredLayers, task.worldPos());

        deferredLayers.computeIfAbsent(task.worldPos().getY(), y -> new LinkedList<>()).add(task);
        buildStages.put(task.worldPos(), BuildStage.PLACE_PENDING);
        markDirty();
    }

    public void promoteDeferredTasksIfReachable(ServerLevel level) {
        List<BlockPlacementTask> toPromote = new ArrayList<>();

        for (Queue<BlockPlacementTask> layer : deferredLayers.values()) {
            for (BlockPlacementTask task : layer) {
                BlockState current = level.getBlockState(task.worldPos());
                BlockState desired = task.state();

                if (current.equals(desired)) {
                    markPlacementComplete(task.worldPos());
                    continue;
                }

                if (current.isAir()) {
                    toPromote.add(task);
                    continue;
                }

                // promote if the builder is allowed to clear this spot
                if (builderCanOverwrite(level, task.worldPos(), current, desired)) {
                    toPromote.add(task);
                }
            }
        }

        for (BlockPlacementTask task : toPromote) {
            removePlacementFromAllLayers(deferredLayers, task.worldPos());
            buildLayers.computeIfAbsent(task.worldPos().getY(), y -> new LinkedList<>()).add(task);
        }

        if (!toPromote.isEmpty()) markDirty();
    }

    private static boolean builderCanOverwrite(ServerLevel level, BlockPos pos, BlockState existing, BlockState desired) {
        // already handled air above
        if (!existing.getFluidState().isEmpty()) return true;
        if (!existing.canOcclude()) return true; // torches, grass, etc.
        if (existing.canBeReplaced()) return true;

        float hardness = existing.getDestroySpeed(level, pos);
        if (hardness < 0.0F) return false;      // unbreakable
        //if (hardness > 50.0F) return false;
        return true;
    }

    public boolean hasScaffoldsToPlace() {
        return !scaffoldLayers.isEmpty();
    }

    public Collection<Queue<BlockPlacementTask>> getScaffoldLayerQueues() {
        return scaffoldLayers.values();
    }

    public void enqueueScaffold(ServerLevel level, BlockPos pos) {
        BlockState cur = level.getBlockState(pos);
        if (cur.is(Blocks.SCAFFOLDING)) return;

        // don't scaffold inside structure-required positions
        BlockState expected = expectedBlocks.get(pos);
        if (expected != null && !expected.isAir()) return;

        BlockPlacementTask t = new BlockPlacementTask(pos, Blocks.SCAFFOLDING.defaultBlockState());
        scaffoldLayers.computeIfAbsent(pos.getY(), y -> new LinkedList<>()).add(t);
        markDirty();
    }

    public void markScaffoldPlaced(BlockPos pos) {
        removePlacementFromAllLayers(scaffoldLayers, pos);
        placedScaffolds.add(pos.immutable());
        markDirty();
    }

    public Set<BlockPos> getPlacedScaffolds() {
        return placedScaffolds;
    }

    public boolean planScaffoldFor(ServerLevel level, BlockPos target) {
        // try multiple candidates, accept the first plan that works
        for (BlockPos stand : findStandCandidates(level, target)) {
            if (tryPlanScaffoldColumn(level, stand)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryPlanScaffoldColumn(ServerLevel level, BlockPos stand) {
        BlockPos floor = stand.below();

        // find a real support block below the floor
        BlockPos support = null;
        for (int y = floor.getY(); y >= level.getMinBuildHeight(); y--) {
            BlockPos p = new BlockPos(floor.getX(), y, floor.getZ());
            BlockState s = level.getBlockState(p);

            if (s.isFaceSturdy(level, p, Direction.UP) || s.is(Blocks.SCAFFOLDING)) {
                support = p;
                break;
            }
        }
        if (support == null) return false;

        BlockPos start = support.above();
        BlockPos end = floor;
        if (end.getY() < start.getY()) return false;

        boolean enqueuedAny = false;

        for (int y = start.getY(); y <= end.getY(); y++) {
            BlockPos pos = new BlockPos(floor.getX(), y, floor.getZ());

            // DO NOT scaffold where a structure block is expected
            BlockState expected = getExpectedBlockState(pos);
            if (expected != null && !expected.isAir()) {
                return false; // this candidate column is invalid, try next stand
            }

            BlockState cur = level.getBlockState(pos);

            if (cur.isAir() || cur.canBeReplaced() || !cur.getFluidState().isEmpty()) {
                enqueueScaffold(level, pos);
                enqueuedAny = true;
            } else if (!cur.is(Blocks.SCAFFOLDING)) {
                return false; // blocked by a solid intruder, try next stand
            }
        }

        return enqueuedAny;
    }

    private List<BlockPos> findStandCandidates(ServerLevel level, BlockPos target) {
        List<BlockPos> out = new ArrayList<>();
        for (int dy : new int[]{0, 1, -1}) {
            BlockPos base = target.offset(0, dy, 0);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    BlockPos stand = base.offset(dx, 0, dz);

                    if (isInsideFootprint(stand) || isInsideFootprint(stand.below())) continue;
                    BlockState feet = level.getBlockState(stand);
                    BlockState head = level.getBlockState(stand.above());
                    if ((!feet.isAir() && !feet.canBeReplaced()) || (!head.isAir() && !head.canBeReplaced())) continue;

                    out.add(stand);
                }
            }
        }
        return out;
    }

    public boolean isDigPending(BlockPos pos) {
        return buildStages.get(pos) == BuildStage.DIG_PENDING;
    }

    @Nullable
    public BlockPos findExteriorStand(ServerLevel level, BuilderNPC builder, BlockPos from, int searchRadius) {
        BlockPos best = null;
        double bestDistSq = Double.POSITIVE_INFINITY;

        int w = getFootprintWidth();
        int depth = getFootprintDepth();

        int minX = origin.getX() - searchRadius;
        int maxX = origin.getX() + w + searchRadius;
        int minZ = origin.getZ() - searchRadius;
        int maxZ = origin.getZ() + depth + searchRadius;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                boolean outside =
                        x < origin.getX() || x >= origin.getX() + w ||
                                z < origin.getZ() || z >= origin.getZ() + depth;
                if (!outside) continue;

                for (int dy : new int[]{0, 1, -1, 2, -2}) {
                    BlockPos stand = new BlockPos(x, from.getY() + dy, z);

                    if (!builder.isStandable(level, stand)) continue;
                    if (builder.isStandBlacklisted(level, stand)) continue;
                    if (builder.getNavigation().createPath(stand, 0) == null) continue;

                    double distSq = stand.distSqr(from);
                    if (distSq < bestDistSq) {
                        bestDistSq = distSq;
                        best = stand.immutable();
                    }
                }
            }
        }
        return best;
    }

    public int getFootprintWidth() {
        return switch (settings.getRotation()) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> template.getSize().getZ();
            default -> template.getSize().getX();
        };
    }

    public int getFootprintDepth() {
        return switch (settings.getRotation()) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> template.getSize().getX();
            default -> template.getSize().getZ();
        };
    }

    public boolean isPlacementOnCooldown(ServerLevel level, BlockPos pos) {
        long until = placeCooldownUntil.getOrDefault(pos, 0L);
        return until > level.getServer().getTickCount();
    }

    public void markPlacementFailed(ServerLevel level, BlockPos pos) {
        int n = placeFailCount.getOrDefault(pos, 0) + 1;
        placeFailCount.put(pos.immutable(), n);

        int backoff = Math.min(200, 10 + n * 20); // 0.5s + 1s per fail, cap 10s
        placeCooldownUntil.put(pos.immutable(), (long) (level.getServer().getTickCount() + backoff));
    }
}