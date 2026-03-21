package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PathJobDigBlock extends PathJob {

    private final BuilderNPC builder;
    private final @Nullable ConstructionSite site;
    private final BlockPos target;
    private final boolean isIntruder;

    private int digTicks = 0;
    private int requiredTicks = 10;
    private boolean navigating = false;
    private int stuckTicks = 0;
    private static final int MAX_STUCK_TICKS = 60;
    private @Nullable BlockPos standPos = null;

    private int repathAttempts = 0;
    private static final int MAX_REPATH_ATTEMPTS = 6;

    public PathJobDigBlock(BuilderNPC builder, @Nullable ConstructionSite site, BlockPos target, boolean isIntruder) {
        super(builder);
        this.builder = builder;
        this.site = site;
        this.target = target.immutable();
        this.isIntruder = isIntruder;
    }

    @Override
    public void resetForRetry() {
        super.resetForRetry();
        digTicks = 0;
        stuckTicks = 0;
        navigating = false;
        standPos = null;
        repathAttempts = 0;
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);

        digTicks = 0;
        stuckTicks = 0;
        navigating = false;
        repathAttempts = 0;

        BlockState state = level.getBlockState(target);
        if (state.isAir()) {
            completeDigIfNeeded();
            this.completed = true;
            return;
        }

        requiredTicks = calculateDigTicks(state);

        standPos = builder.findStandPosForDig(level, target);
        if (standPos == null) {
            if (builder.canDirectDigFallback(level, target)) {
                navigating = false;
                this.started = true;
                builder.holdItemTemporarily(new ItemStack(Items.IRON_SHOVEL));
                System.out.println("[PathJobDigBlock] No standPos for target " + target + ", using direct-dig fallback.");
                return;
            }
            System.out.println("[PathJobDigBlock] No standPos for target " + target + ", failing.");
            builder.markBadDigTask(level, target);
            this.failed = true;
            return;
        }

        // Already close enough?
        if (builder.blockPosition().closerThan(standPos, 2.0)) {
            this.started = true;
            return;
        }

        // Try to path; if fails, try alternate stand candidates
        var initialPath = builder.getNavigation().createPath(standPos, 0);
        if (initialPath == null || !initialPath.canReach()) {
            builder.markBadStand(level, standPos);

            if (!trySwitchStandAndRepath(level, target)) {
                builder.markBadDigTask(level, target);
                System.out.println("[PathJobDigBlock] No path to ANY standPos for " + target + ", failing.");
                this.failed = true;
                return;
            }

            // trySwitchStandAndRepath started navigation + set standPos
            this.started = true;
            builder.holdItemTemporarily(new ItemStack(Items.IRON_SHOVEL));
            return;
        }

        boolean startedNav = builder.getNavigation().moveTo(initialPath, builder.getNavigationSpeed());

        if (!startedNav) {
            builder.markBadStand(level, standPos);

            if (!trySwitchStandAndRepath(level, target)) {
                builder.markBadDigTask(level, target);
                System.out.println("[PathJobDigBlock] Failed to start nav to ANY standPos for " + target);
                this.failed = true;
                return;
            }

            this.started = true;
            builder.holdItemTemporarily(new ItemStack(Items.IRON_SHOVEL));
            return;
        }

        navigating = true;
        this.started = true;
        builder.holdItemTemporarily(new ItemStack(Items.IRON_SHOVEL));
    }

    @Override
    protected  void onTick(ServerLevel level) {
        if (this.failed || this.completed) return;
        if (!builder.hasSite()) { this.failed = true; return; }

        BlockState state = level.getBlockState(target);
        if (state.isAir()) {
            completeDigIfNeeded();
            markComplete();
            return;
        }

        if (standPos == null) {
            standPos = builder.findStandPosForDig(level, target);
            if (standPos == null) {
                if (!builder.canDirectDigFallback(level, target)) {
                    this.failed = true;
                    return;
                }
            }
        }

        if (standPos != null && !builder.isStandable(level, standPos)) {
            builder.markBadStand(level, standPos);
            if (trySwitchStandAndRepath(level, target)) return;

            builder.markBadDigTask(level, target);
            this.failed = true;
            builder.clearHeldItem();
            return;
        }

        if (navigating) {
            if (!builder.blockPosition().closerThan(standPos, 2.0)) {
                stuckTicks++;

                // try switch stand & repath halfway through
                if (stuckTicks > MAX_STUCK_TICKS / 2) {
                    if (trySwitchStandAndRepath(level, target)) {
                        return;
                    }
                }

                if (stuckTicks > MAX_STUCK_TICKS) {
                    System.out.println("[PathJobDigBlock] Stuck reaching standPos " + standPos + " for " + target);
                    builder.markBadDigTask(level, target);
                    this.failed = true;
                    builder.clearHeldItem();
                }
                return;
            }
            builder.getNavigation().stop();
            navigating = false;
        }

        // dig...
        builder.holdItemTemporarily(new ItemStack(Items.IRON_SHOVEL));
        builder.swing(builder.getUsedItemHand());

        digTicks++;
        if (digTicks >= requiredTicks) {
            ServerLevel serverLevel = (ServerLevel) builder.level();
            serverLevel.destroyBlock(target, true, builder);

            completeDigIfNeeded();
            markComplete();
        }
    }

    private void markComplete() {
        this.completed = true;
        builder.clearHeldItem();
        System.out.println("[PathJobDigBlock] Dug: " + target);
    }

    private int calculateDigTicks(BlockState state) {
        return state.isAir() ? 1 : builder.getDigTicksPerBlock();
    }

    @Override
    public String getName() {
        return "DigBlock at " + target.toShortString();
    }

    private void completeDigIfNeeded() {
        if (!isIntruder) {
            if (site != null) {
                site.markDigComplete(target);
            }
        }
    }

    @Override
    public boolean shouldAbort(ServerLevel level) {
        if (this.completed) return false;
        // If builder lost the site, don't retry forever
        if (!builder.hasSite()) return true;
        if (!isIntruder && (site == null || builder.getAssignedSite() != site)) return true;
        if (builder.isTaskBlacklisted(level, target)) return true;

        BlockState current = level.getBlockState(target);
        if (current.isAir()) {
            // IMPORTANT: remove from dig queue so builder doesn't keep selecting it
            if (!isIntruder) {
                if (site != null) {
                    site.markDigComplete(target);
                }
            }
            return true;
        }

        // If this target is inside footprint but expected block is already present, abort
        if (!isIntruder) {
            if (site != null) {
                BlockState expected = site.getExpectedBlockState(target);
                if (expected != null && current.equals(expected)) {
                    site.markDigComplete(target);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean trySwitchStandAndRepath(ServerLevel level, BlockPos target) {
        if (repathAttempts++ >= MAX_REPATH_ATTEMPTS) return false;

        for (BlockPos stand : builder.findStandCandidatesForDig(level, target)) {
            if (builder.isStandBlacklisted(level, stand)) continue;
            if (!builder.isStandable(level, stand)) continue;
            if (!builder.canReachStandNow(level, stand)) continue;
            var path = builder.getNavigation().createPath(stand, 0);
            if (path == null || !path.canReach()) continue;

            boolean ok = builder.getNavigation().moveTo(path, builder.getNavigationSpeed());
            if (ok) {
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

}
