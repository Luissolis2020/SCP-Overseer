package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import javax.annotation.Nullable;
import java.util.Queue;

public class PathJobPlaceBlock extends PathJob {

    private final BuilderNPC builder;
    private final ConstructionSite site;
    private final ConstructionSite.BlockPlacementTask task;

    private boolean navigating = false;
    private int placeTicks = 0;
    private int stuckTicks = 0;

    private static final double ARRIVE_DIST_SQR = 2.25;   // within 1.5 blocks
    private static final double TOO_FAR_SQR    = 64.0;   // hard ceiling
    private static final int MAX_STUCK_TICKS   = 60;

    private @Nullable BlockPos standPos = null;

    private boolean abortBecauseScaffoldPlanned = false;
    private boolean abortBecauseDeferred = false;

    private int repathAttempts = 0;
    private static final int MAX_REPATH_ATTEMPTS = 6;

    public PathJobPlaceBlock(BuilderNPC builder, ConstructionSite site, ConstructionSite.BlockPlacementTask task) {
        super(builder);
        this.builder = builder;
        this.site = site;
        this.task = task;
    }

    @Override
    public void resetForRetry() {
        super.resetForRetry();
        navigating = false;
        placeTicks = 0;
        stuckTicks = 0;
        standPos = null;
        abortBecauseScaffoldPlanned = false;
        abortBecauseDeferred = false;
        repathAttempts = 0;
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);

        navigating = false;
        placeTicks = 0;
        stuckTicks = 0;
        repathAttempts = 0;

        if (site == null || task == null) { this.failed = true; return; }

        BlockState existing = level.getBlockState(task.worldPos());
        BlockState desired  = task.state();

        if (existing.equals(desired)) {
            markDone(task.worldPos(), desired);
            this.completed = true;
            return;
        }

        boolean isScaffold = task.state().is(Blocks.SCAFFOLDING);
        standPos = builder.findStandPosForPlacement(level, site, task.worldPos(), isScaffold);
        int relY = task.worldPos().getY() - site.getOrigin().getY();
        if (standPos == null) {
            if (relY <= 1) {
                deferAndAbort(level, "no-stand-pos");
                return;
            }
            tryPlanScaffold(level);
            deferAndAbort(level, "no-stand-pos");
            return;
        }

        double distToStand = builder.blockPosition().distSqr(standPos);
        if (distToStand > ARRIVE_DIST_SQR) {
            var path = builder.getNavigation().createPath(standPos, 0);
            boolean navStarted = path != null && path.canReach() && builder.getNavigation().moveTo(path, builder.getNavigationSpeed());
            if (!navStarted) {
                System.out.println("[PathJobPlaceBlock] Failed to start path to " + standPos + " for " + task.worldPos());

                builder.markBadStand(level, standPos);

                if (trySwitchStandAndRepath(level, task.worldPos())) {
                    navigating = true;
                    this.started = true;
                    builder.holdItemTemporarily(new ItemStack(desired.getBlock().asItem()));
                    return;
                }

                // No stand worked => mark task temporarily bad so selector doesn't immediately re-pick it
                tryPlanScaffold(level);
                deferAndAbort(level, "nav-start-failed");
                return;
            }
            navigating = true;
        }

        this.started = true;
        builder.holdItemTemporarily(new ItemStack(desired.getBlock().asItem()));
    }

    @Override
    protected  void onTick(ServerLevel level) {
        if (this.failed || this.completed) return;
        if (builder.getAssignedSite() != site) {
            this.failed = true;
            builder.clearHeldItem();
            return;
        }

        BlockPos pos = task.worldPos();
        BlockState existing = level.getBlockState(pos);
        BlockState desired  = task.state();

        if (existing.equals(desired)) {
            markDone(pos, desired);
            builder.clearHeldItem();
            this.completed = true;
            return;
        }

        if (standPos == null) {
            boolean isScaffold = task.state().is(Blocks.SCAFFOLDING);
            standPos = builder.findStandPosForPlacement((ServerLevel) level, site, pos, isScaffold);
            if (standPos == null) {
                tryPlanScaffold((ServerLevel) level);
                deferAndAbort((ServerLevel) level, "lost-stand-pos");
                return;
            }
        }

        double distSqr = builder.blockPosition().distSqr(standPos);

        if (navigating) {
            if (distSqr <= ARRIVE_DIST_SQR) {
                builder.getNavigation().stop();
                navigating = false;
                stuckTicks = 0;
            } else {
                stuckTicks++;

                // If we're stuck, try repathing / stand swap before deferring
                if (stuckTicks > MAX_STUCK_TICKS / 2) {
                    if (trySwitchStandAndRepath((ServerLevel) level, pos)) {
                        return;
                    }
                }

                if (stuckTicks > MAX_STUCK_TICKS || distSqr > TOO_FAR_SQR) {
                    System.out.println("[PathJobPlaceBlock] Stuck reaching " + pos + ", deferring.");
                    tryPlanScaffold((ServerLevel) level);
                    deferAndAbort((ServerLevel) level, "stuck");
                    if (standPos != null) builder.markBadStandForTarget(task.worldPos(), standPos);
                }
                return;
            }
        }

        if (isOccupyingPlacementCell(pos)) {
            if (trySwitchStandAndRepath((ServerLevel) level, pos)) {
                return;
            }

            tryPlanScaffold((ServerLevel) level);
            deferAndAbort((ServerLevel) level, "occupied-target-cell");
            if (standPos != null) builder.markBadStandForTarget(task.worldPos(), standPos);
            return;
        }

        // Same block, wrong props: overwrite
        if (!existing.isAir() && existing.is(desired.getBlock()) && !existing.equals(desired)) {
            level.setBlockAndUpdate(pos, desired);
            markDone(pos, desired);
            builder.clearHeldItem();
            this.completed = true;
            return;
        }

        // Need to clear intruder
        if (!existing.isAir() && !existing.is(desired.getBlock())) {
            placeTicks = 0;
            builder.swing(builder.getUsedItemHand(), true);
            level.destroyBlock(pos, true, builder);
            return;
        }

        ItemStack expected = new ItemStack(desired.getBlock().asItem());
        if (!ItemStack.isSameItemSameTags(builder.getMainHandItem(), expected)) {
            builder.holdItemTemporarily(expected);
        }

        // Must be within reach of the block we are placing
        double reachSqr = builder.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (reachSqr > 25.0) { // 5 blocks
            // If we're too far, re-pick a closer stand and repath
            if (trySwitchStandAndRepath((ServerLevel) level, pos)) return;

            // Can't get in reach -> scaffold + defer
            tryPlanScaffold((ServerLevel) level);
            deferAndAbort((ServerLevel) level, "out-of-reach");
            if (standPos != null) builder.markBadStandForTarget(task.worldPos(), standPos);
            return;
        }

        placeTicks++;
        builder.swing(builder.getUsedItemHand(), true);

        if (placeTicks >= builder.getPlaceTicksPerBlock()) {
            if (desired.getBlock() instanceof DoorBlock) {
                placeDoorConstructionOpen((ServerLevel) level, pos, desired);
            } else if (desired.getBlock() instanceof BedBlock) {
                placeBedAsPair((ServerLevel) level, pos, desired);
            } else {
                level.setBlockAndUpdate(pos, desired);
                markDone(pos, desired);
            }

            boolean isScaffold = desired.is(Blocks.SCAFFOLDING);

            FacilityManager mgr = FacilityManager.get((ServerLevel) level);
            builder.clearHeldItem();
            this.completed = true;

            if (!isScaffold) mgr.finalizeIfComplete((ServerLevel) level, site, builder);
        }
    }

    private void markDone(BlockPos pos, BlockState desired) {
        if (desired.is(Blocks.SCAFFOLDING)) site.markScaffoldPlaced(pos);
        else site.markPlacementComplete(pos);
        builder.clearBadStandsForTarget(pos);
    }

    private void deferAndAbort(ServerLevel level, String reason) {
        System.out.println("[PathJobPlaceBlock] Deferring " + task.worldPos() + " (" + reason + ")");
        builder.markBadTask(level, task.worldPos());
        site.markPlacementFailed(level, task.worldPos());

        site.deferPlacementTask(task);

        abortBecauseDeferred = true;

        builder.clearHeldItem();
        this.completed = true;
        this.failed = false;
    }

    private void placeDoorConstructionOpen(ServerLevel level, BlockPos pos, BlockState anyHalf) {
        if (!(anyHalf.getBlock() instanceof DoorBlock)) return;

        // normalize to bottom
        BlockPos bottomPos = anyHalf.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        BlockPos topPos = bottomPos.above();

        BlockState bottom = anyHalf.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        BlockState top    = anyHalf.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);

        // keep doorway walkable while building
        if (bottom.hasProperty(DoorBlock.OPEN)) bottom = bottom.setValue(DoorBlock.OPEN, true);
        if (top.hasProperty(DoorBlock.OPEN))    top    = top.setValue(DoorBlock.OPEN, true);

        // helps iron doors stay open as a state
        if (bottom.hasProperty(DoorBlock.POWERED)) bottom = bottom.setValue(DoorBlock.POWERED, false);
        if (top.hasProperty(DoorBlock.POWERED))    top    = top.setValue(DoorBlock.POWERED, false);

        level.setBlockAndUpdate(bottomPos, bottom);
        level.setBlockAndUpdate(topPos, top);

        site.markPlacementComplete(bottomPos);
        site.markPlacementComplete(topPos);
    }

    private void placeBedAsPair(ServerLevel level, BlockPos pos, BlockState anyPart) {
        if (!(anyPart.getBlock() instanceof BedBlock)) return;

        Direction facing = anyPart.hasProperty(BedBlock.FACING)
                ? anyPart.getValue(BedBlock.FACING)
                : Direction.NORTH;
        BedPart part = anyPart.hasProperty(BedBlock.PART)
                ? anyPart.getValue(BedBlock.PART)
                : BedPart.FOOT;
        boolean occupied = anyPart.hasProperty(BedBlock.OCCUPIED) && anyPart.getValue(BedBlock.OCCUPIED);

        BlockPos footPos = (part == BedPart.FOOT) ? pos : pos.relative(facing.getOpposite());
        BlockPos headPos = (part == BedPart.HEAD) ? pos : pos.relative(facing);

        BlockState foot = anyPart.setValue(BedBlock.PART, BedPart.FOOT).setValue(BedBlock.FACING, facing);
        BlockState head = anyPart.setValue(BedBlock.PART, BedPart.HEAD).setValue(BedBlock.FACING, facing);
        if (foot.hasProperty(BedBlock.OCCUPIED)) foot = foot.setValue(BedBlock.OCCUPIED, occupied);
        if (head.hasProperty(BedBlock.OCCUPIED)) head = head.setValue(BedBlock.OCCUPIED, occupied);

        level.setBlockAndUpdate(footPos, foot);
        level.setBlockAndUpdate(headPos, head);

        site.markPlacementComplete(footPos);
        site.markPlacementComplete(headPos);
        builder.clearBadStandsForTarget(footPos);
        builder.clearBadStandsForTarget(headPos);
    }


    @Override
    public boolean shouldAbort(ServerLevel level) {
        if (site == null || task == null) return true;
        if (!builder.hasSite()) return true;
        if (builder.getAssignedSite() != site) return true;

        if (abortBecauseScaffoldPlanned || abortBecauseDeferred) {
            return true;
        }

        BlockPos pos = task.worldPos();
        BlockState existing = level.getBlockState(pos);
        BlockState desired  = task.state();

        if (existing.equals(desired)) {
            markDone(pos, desired);
            builder.clearHeldItem();
            return true;
        }

        if (!existing.isAir() && !existing.equals(desired)) {
            float hardness = existing.getDestroySpeed(level, pos);
            if (hardness < 0.0F) {
                System.out.println("[PathJobPlaceBlock] Unbreakable intruder at " + pos + " (" + existing + "), deferring.");

                site.markPlacementFailed(level, pos);
                builder.markBadTask(level, pos);

                site.deferPlacementTask(task);
                builder.clearHeldItem();
                return true;
            }
        }

        return false;
    }

    private void tryPlanScaffold(ServerLevel level) {
        if (task == null) return;
        if (task.state().is(Blocks.SCAFFOLDING)) return;
        int relY = task.worldPos().getY() - site.getOrigin().getY();
        if (relY <= 1) return;

        // Require at least one prior failure before expanding support columns.
        int failCount = site.getPlacementFailCount(task.worldPos());
        if (failCount < 1) return;

        int buildPending = site.getBuildLayerQueues().stream().mapToInt(Queue::size).sum();
        int deferredPending = site.getDeferredLayerQueues().stream().mapToInt(Queue::size).sum();
        int scaffoldPending = site.getScaffoldLayerQueues().stream().mapToInt(Queue::size).sum();
        int placedScaffolds = site.getPlacedScaffolds().size();
        int nonScaffoldPending = buildPending + deferredPending;

        // Keep scaffold growth bounded so late-stage retries don't spam support blocks.
        int scaffoldBudget = Math.max(4, Math.min(10, (nonScaffoldPending / 24) + 4));
        if ((scaffoldPending + placedScaffolds) >= scaffoldBudget) return;

        // If supports are already queued, let builder consume them before planning more.
        if (scaffoldPending > 0 && failCount < 3) return;

        boolean planned = site.planScaffoldFor(level, task.worldPos());
        if (planned) {
            abortBecauseScaffoldPlanned = true;
            System.out.println("[PathJobPlaceBlock] Planned scaffolds for " + task.worldPos());
        }
    }

    private boolean trySwitchStandAndRepath(ServerLevel level, BlockPos target) {
        if (repathAttempts++ >= MAX_REPATH_ATTEMPTS) return false;

        for (BlockPos stand : builder.findStandCandidatesForPlacement(level, target)) {
            if (builder.isStandBlacklisted(level, stand)) continue;
            if (!builder.isStandable(level, stand)) continue;
            if (!builder.canReachStandNow(level, stand)) {
                builder.markBadStand(level, stand);
                continue;
            }

            var path = builder.getNavigation().createPath(stand, 0);
            if (path == null || !path.canReach()) {
                builder.markBadStand(level, stand);
                continue;
            }

            boolean navStarted = builder.getNavigation().moveTo(path, builder.getNavigationSpeed());

            if (navStarted) {
                standPos = stand.immutable();
                navigating = true;
                stuckTicks = 0;
                return true;
            } else {
                builder.markBadStand(level, stand);
            }
        }
        return false;
    }

    private boolean isOccupyingPlacementCell(BlockPos targetPos) {
        BlockPos feet = builder.blockPosition();
        BlockPos head = feet.above();
        return feet.equals(targetPos) || head.equals(targetPos);
    }

    @Override
    public String getName() {
        return "PlaceBlock at " + task.worldPos().toShortString();
    }
}
