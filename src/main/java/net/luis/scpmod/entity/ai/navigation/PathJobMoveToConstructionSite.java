package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class PathJobMoveToConstructionSite extends PathJob {

    private final BuilderNPC builder;
    private final ConstructionSite site;
    private BlockPos navTarget;

    private int failedNavigationCooldown = 0;
    private int failedAttempts = 0;
    private boolean manualAdvance = false;
    private int manualTicks = 0;

    private static final int MAX_ATTEMPTS = 5;
    private static final int COOLDOWN_TICKS = 40;
    private static final int MAX_MANUAL_TICKS = 160;
    private static final int APPROACH_RADIUS = 4;

    public PathJobMoveToConstructionSite(BuilderNPC builder, ConstructionSite site) {
        super(builder);
        this.builder = builder;
        this.site = site;
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);
        manualTicks = 0;

        if (builder.isAtAssignedSite()) {
            completed = true;
            System.out.println("[PathJobMoveToConstructionSite] Already at site, completing immediately.");
            return;
        }

        // Keep approach points close to the site footprint so reaching them counts as "at site".
        navTarget = site.findExteriorStand(level, builder, builder.blockPosition(), APPROACH_RADIUS);
        if (navTarget == null) navTarget = site.getOrigin(); // fallback

        var path = builder.getNavigation().createPath(navTarget, 0);
        boolean success = path != null && path.canReach() && builder.getNavigation().moveTo(path, builder.getNavigationSpeed());
        System.out.println("[PathJobMoveToConstructionSite] Navigation started to " + navTarget + ", success: " + success);

        if (!success) {
            failedAttempts++;
            failedNavigationCooldown = COOLDOWN_TICKS;
            if (failedAttempts >= MAX_ATTEMPTS) {
                if (tryQueueAccessDig(level)) {
                    failed = true;
                    failedAttempts = 0;
                    return;
                }
                System.out.println("[PathJobMoveToConstructionSite] Max failures reached. Entering manual advance mode.");
                manualAdvance = true;
                failedAttempts = 0;
            }
        } else {
            failedAttempts = 0;
            manualAdvance = false;
        }
    }

    @Override
    protected void onTick(ServerLevel level) {
        if (builder.isAtAssignedSite()) {
            completed = true;
            System.out.println("[PathJobMoveToConstructionSite] Reached site, job complete.");
            return;
        }

        if (manualAdvance) {
            manualTicks++;

            double dx = navTarget.getX() + 0.5 - builder.getX();
            double dy = navTarget.getY() + 0.5 - builder.getY();
            double dz = navTarget.getZ() + 0.5 - builder.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 0.2) {
                builder.getMoveControl().setWantedPosition(
                        builder.getX() + dx / distance * builder.getManualMoveStep(),
                        builder.getY() + dy / distance * builder.getManualMoveStep(),
                        builder.getZ() + dz / distance * builder.getManualMoveStep(),
                        builder.getManualMoveSpeed()
                );
            }
            if (distance <= 0.8) {
                if (builder.isAtAssignedSite()) {
                    completed = true;
                    return;
                }
                // We reached a local approach point but still aren't considered at-site.
                // Force a fresh local approach target instead of completing in the wrong zone.
                navTarget = site.findExteriorStand(level, builder, builder.blockPosition(), APPROACH_RADIUS);
                if (navTarget == null) navTarget = site.getOrigin();
                manualTicks = 0;
            }

            if (manualTicks >= MAX_MANUAL_TICKS) {
                if (tryQueueAccessDig(level)) {
                    failed = true;
                    return;
                }
                System.out.println("[PathJobMoveToConstructionSite] Manual mode timeout, requeuing fresh move job.");
                builder.getNavigation().stop();
                failed = true;
            }
            return;
        }

        if (failedNavigationCooldown > 0) {
            failedNavigationCooldown--;
            return;
        }

        if (!builder.getNavigation().isInProgress()) {
            if (!builder.canReachStandNow(level, navTarget)) {
                BlockPos fresh = site.findExteriorStand(level, builder, builder.blockPosition(), APPROACH_RADIUS);
                if (fresh != null) navTarget = fresh;
            }
            var path = builder.getNavigation().createPath(navTarget, 0);
            boolean success = path != null && path.canReach() && builder.getNavigation().moveTo(path, builder.getNavigationSpeed());

            System.out.println("[PathJobMoveToConstructionSite] Retrying navigation to " + navTarget + ", success: " + success);

            if (!success) {
                failedAttempts++;
                failedNavigationCooldown = COOLDOWN_TICKS;

                if (failedAttempts >= MAX_ATTEMPTS) {
                    if (tryQueueAccessDig(level)) {
                        failed = true;
                        failedAttempts = 0;
                        return;
                    }
                    System.out.println("[PathJobMoveToConstructionSite] Max retries reached. Manual mode.");
                    manualAdvance = true;
                    failedAttempts = 0;
                }
            } else {
                failedAttempts = 0;
            }
        }
    }

    @Override
    public boolean shouldAbort(ServerLevel level) {
        // If builder no longer has a site, or site is complete, drop this job
        if (!builder.hasSite()) return true;
        if (site == null) return true;
        if (builder.getAssignedSite() != site) return true;
        if (site.isComplete()) return true;
        return false;
    }

    @Override
    public void cancel() {
        super.cancel();
        builder.getNavigation().stop();
    }

    @Override
    public String getName() {
        return "MoveToConstructionSite";
    }

    private boolean tryQueueAccessDig(ServerLevel level) {
        BlockPos digTarget = findAccessDigTarget(level);
        if (digTarget == null) return false;
        if (builder.isTaskBlacklisted(level, digTarget)) return false;

        // Carve a 2-block-high tunnel cell when needed.
        BlockPos upper = digTarget.above();
        if (isDiggableObstacle(level, upper) && !builder.isTaskBlacklisted(level, upper)) {
            builder.getPathJobQueue().enqueue(new PathJobDigBlock(builder, site, upper, true));
        }
        builder.getPathJobQueue().enqueue(new PathJobDigBlock(builder, site, digTarget, true));

        System.out.println("[PathJobMoveToConstructionSite] Queued access dig toward site at " + digTarget);
        return true;
    }

    private BlockPos findAccessDigTarget(ServerLevel level) {
        BlockPos from = builder.blockPosition();
        BlockPos target = navTarget != null ? navTarget : site.getOrigin();
        int minY = site.getOrigin().getY();
        int maxY = site.getOrigin().getY() + site.getHeight() + 2;

        double dx = (target.getX() + 0.5) - builder.getX();
        double dz = (target.getZ() + 0.5) - builder.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 1.0E-4) return null;

        double nx = dx / len;
        double nz = dz / len;
        int baseY = from.getY();

        for (int step = 1; step <= 5; step++) {
            int px = Mth.floor(builder.getX() + nx * step);
            int pz = Mth.floor(builder.getZ() + nz * step);
            for (int yOff : new int[]{0, 1, -1, 2}) {
                BlockPos probe = new BlockPos(px, baseY + yOff, pz);
                if (probe.getY() < minY || probe.getY() > maxY) continue;
                if (isDiggableObstacle(level, probe)) {
                    return probe.immutable();
                }
            }
        }

        // Fallback: adjacent block in the cardinal direction toward target.
        Direction primaryX = dx >= 0 ? Direction.EAST : Direction.WEST;
        Direction primaryZ = dz >= 0 ? Direction.SOUTH : Direction.NORTH;
        for (Direction d : new Direction[]{primaryX, primaryZ, primaryX.getClockWise(), primaryX.getCounterClockWise()}) {
            BlockPos probe = from.relative(d);
            if (probe.getY() >= minY && probe.getY() <= maxY && isDiggableObstacle(level, probe)) return probe.immutable();
            BlockPos probeUp = probe.above();
            if (probeUp.getY() >= minY && probeUp.getY() <= maxY && isDiggableObstacle(level, probeUp)) return probeUp.immutable();
        }
        return null;
    }

    private boolean isDiggableObstacle(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.canBeReplaced()) return false;
        if (!state.getFluidState().isEmpty()) return false;
        return state.getDestroySpeed(level, pos) >= 0.0F;
    }
}
