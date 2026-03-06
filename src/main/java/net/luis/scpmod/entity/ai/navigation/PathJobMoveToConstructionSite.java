package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class PathJobMoveToConstructionSite extends PathJob {

    private final BuilderNPC builder;
    private final ConstructionSite site;
    private BlockPos navTarget;

    private int failedNavigationCooldown = 0;
    private int failedAttempts = 0;
    private boolean manualAdvance = false;

    private static final int MAX_ATTEMPTS = 5;
    private static final int COOLDOWN_TICKS = 40;

    public PathJobMoveToConstructionSite(BuilderNPC builder, ConstructionSite site) {
        super(builder);
        this.builder = builder;
        this.site = site;
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);

        if (builder.isAtAssignedSite()) {
            completed = true;
            System.out.println("[PathJobMoveToConstructionSite] Already at site, completing immediately.");
            return;
        }

        navTarget = site.findExteriorStand(level, builder, builder.blockPosition(), 10);
        if (navTarget == null) navTarget = site.getOrigin(); // fallback

        boolean success = builder.getNavigation().moveTo(
                navTarget.getX() + 0.5,
                navTarget.getY(),
                navTarget.getZ() + 0.5,
                1.0
        );
        System.out.println("[PathJobMoveToConstructionSite] Navigation started to " + navTarget + ", success: " + success);

        if (!success) {
            failedAttempts++;
            failedNavigationCooldown = COOLDOWN_TICKS;
            if (failedAttempts >= MAX_ATTEMPTS) {
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
            double dx = navTarget.getX() + 0.5 - builder.getX();
            double dy = navTarget.getY() + 0.5 - builder.getY();
            double dz = navTarget.getZ() + 0.5 - builder.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 0.2) {
                builder.getMoveControl().setWantedPosition(
                        builder.getX() + dx / distance * 0.25,
                        builder.getY() + dy / distance * 0.25,
                        builder.getZ() + dz / distance * 0.25,
                        0.6
                );
            } if (distance <= 0.8) {
                completed = true;
                return;
            }
            return;
        }

        if (failedNavigationCooldown > 0) {
            failedNavigationCooldown--;
            return;
        }

        if (!builder.getNavigation().isInProgress()) {
            boolean success = builder.getNavigation().moveTo(
                    navTarget.getX() + 0.5,
                    navTarget.getY(),
                    navTarget.getZ() + 0.5,
                    1.0
            );

            System.out.println("[PathJobMoveToConstructionSite] Retrying navigation to " + navTarget + ", success: " + success);

            if (!success) {
                failedAttempts++;
                failedNavigationCooldown = COOLDOWN_TICKS;

                if (failedAttempts >= MAX_ATTEMPTS) {
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
}
