package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class PathJobMoveToPos extends PathJob {
    private final BuilderNPC builder;
    private final BlockPos target;

    private int ticksSinceProgress = 0;
    private int replanCooldown = 0;

    private double lastDistSq = Double.POSITIVE_INFINITY;

    private static final int STUCK_TICKS = 80;          // ~4s no progress
    private static final int REPLAN_COOLDOWN_TICKS = 15; // retry ~0.75s
    private static final int MAX_REPLANS = 6;

    private int replans = 0;

    public PathJobMoveToPos(BuilderNPC builder, BlockPos target) {
        super(builder);
        this.builder = builder;
        this.target = target.immutable();
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);

        ticksSinceProgress = 0;
        replanCooldown = 0;
        replans = 0;

        lastDistSq = distSqToTarget();
        if (!tryStartMove()) {
            replanCooldown = REPLAN_COOLDOWN_TICKS;
        }
    }

    @Override
    protected void onTick(ServerLevel level) {
        if (isAtTarget()) {
            completed = true;
            return;
        }

        double d = distSqToTarget();
        if (d + 0.01 < lastDistSq) {
            lastDistSq = d;
            ticksSinceProgress = 0;
        } else {
            ticksSinceProgress++;
        }

        if (replanCooldown > 0) replanCooldown--;

        boolean navIdle = builder.getNavigation().isDone() || !builder.getNavigation().isInProgress();

        if ((navIdle || ticksSinceProgress > 20) && replanCooldown == 0) {
            boolean started = tryStartMove();
            replans++;

            replanCooldown = REPLAN_COOLDOWN_TICKS;

            if (!started && replans >= MAX_REPLANS) {
                failed = true;
                return;
            }
        }

        if (ticksSinceProgress >= STUCK_TICKS) {
            failed = true;
        }
    }

    private boolean tryStartMove() {
        Vec3 center = Vec3.atCenterOf(target);
        return builder.getNavigation().moveTo(center.x, center.y, center.z, 1.0);
    }

    private boolean isAtTarget() {
        Vec3 pos = builder.position();
        Vec3 center = Vec3.atCenterOf(target);

        // horizontal within ~1.5 blocks
        double dx = pos.x - center.x;
        double dz = pos.z - center.z;
        double horizSq = dx * dx + dz * dz;
        double dy = Math.abs(pos.y - center.y);

        return horizSq <= (1.5 * 1.5) && dy <= 2.0;
    }

    private double distSqToTarget() {
        Vec3 pos = builder.position();
        Vec3 center = Vec3.atCenterOf(target);
        return pos.distanceToSqr(center);
    }

    @Override
    public boolean shouldAbort(ServerLevel level) {
        return !builder.hasSite();
    }

    @Override
    public String getName() {
        return "MoveToPos:" + target.asLong();
    }
}