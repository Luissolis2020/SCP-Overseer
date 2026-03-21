package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.BuilderNPC;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Repositions the builder near a work target, while biasing candidates toward a site anchor.
 */
public class PathJobMoveCloseToTargetNearAnchor extends PathJob {

    private static final int STUCK_TICKS = 80;
    private static final int REPLAN_COOLDOWN_TICKS = 12;
    private static final int MAX_REPLANS = 8;
    private static final int MAX_CANDIDATE_ATTEMPTS = 6;
    private static final int MAX_ALLOWED_DROP = 2;
    private static final int MAX_ALLOWED_CLIMB = 4;

    private final BuilderNPC builder;
    private final ConstructionSite site;
    private final BlockPos target;
    private final BlockPos anchor;
    private final double closeRange;
    private final double closeRangeSq;
    private final double maxSearchDistSq;

    private int ticksSinceProgress = 0;
    private int replanCooldown = 0;
    private int replans = 0;
    private double lastDistSq = Double.POSITIVE_INFINITY;

    private @Nullable BlockPos navTarget = null;

    public PathJobMoveCloseToTargetNearAnchor(
            BuilderNPC builder,
            ConstructionSite site,
            BlockPos target,
            BlockPos anchor,
            double closeRange
    ) {
        super(builder);
        this.builder = builder;
        this.site = site;
        this.target = target.immutable();
        this.anchor = anchor.immutable();
        this.closeRange = Math.max(2.0, closeRange);
        this.closeRangeSq = this.closeRange * this.closeRange;
        this.maxSearchDistSq = this.closeRangeSq + 25.0;
    }

    @Override
    public void start(ServerLevel level) {
        super.start(level);
        ticksSinceProgress = 0;
        replanCooldown = 0;
        replans = 0;
        lastDistSq = distSqToTarget();

        if (isCloseEnoughToTarget()) {
            completed = true;
            return;
        }

        if (!tryStartMove(level)) {
            failed = true;
        }
    }

    @Override
    protected void onTick(ServerLevel level) {
        if (isCloseEnoughToTarget()) {
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
            boolean started = tryStartMove(level);
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

    private boolean tryStartMove(ServerLevel level) {
        Set<BlockPos> rejected = new HashSet<>();

        for (int i = 0; i < MAX_CANDIDATE_ATTEMPTS; i++) {
            BlockPos candidate = selectBestCandidate(level, rejected);
            if (candidate == null) return false;

            var path = builder.getNavigation().createPath(candidate, 0);
            if (path == null || !path.canReach()) {
                builder.markBadStand(level, candidate);
                rejected.add(candidate);
                continue;
            }

            if (builder.getNavigation().moveTo(path, builder.getNavigationSpeed())) {
                navTarget = candidate.immutable();
                return true;
            }

            builder.markBadStand(level, candidate);
            rejected.add(candidate);
        }

        return false;
    }

    private @Nullable BlockPos selectBestCandidate(ServerLevel level, Set<BlockPos> rejected) {
        BlockPos from = builder.blockPosition();
        int minY = Math.max(site.getOrigin().getY(), from.getY() - MAX_ALLOWED_DROP);
        int maxY = Math.min(site.getOrigin().getY() + site.getHeight() + 2, from.getY() + MAX_ALLOWED_CLIMB);

        BlockPos best = null;
        double bestScore = Double.POSITIVE_INFINITY;

        for (BlockPos stand : builder.findStandCandidatesForDig(level, target)) {
            if (rejected.contains(stand)) continue;
            if (stand.getY() < minY || stand.getY() > maxY) continue;
            if (stand.distSqr(target) > maxSearchDistSq) continue;
            if (builder.isStandBlacklisted(level, stand)) continue;
            if (!builder.isStandable(level, stand)) continue;
            if (!builder.canReachStandNow(level, stand)) continue;

            double toTarget = stand.distSqr(target);
            double toAnchor = stand.distSqr(anchor);
            double toBuilder = stand.distSqr(from);
            int drop = from.getY() - stand.getY();
            double dropPenalty = drop > 0 ? (drop * drop * 90.0) : 0.0;

            boolean inside = site.isInsideFootprint(stand) || site.isInsideFootprint(stand.below());
            double insidePenalty = inside ? 50.0 : 0.0;

            double nearTargetBonus = toTarget <= closeRangeSq ? -200.0 : 0.0;
            double score = (toTarget * 12.0) + (toAnchor * 2.0) + toBuilder + dropPenalty + insidePenalty + nearTargetBonus;

            if (score < bestScore) {
                bestScore = score;
                best = stand.immutable();
            }
        }

        return best;
    }

    private boolean isCloseEnoughToTarget() {
        Vec3 pos = builder.position();
        Vec3 center = Vec3.atCenterOf(target);

        double dx = pos.x - center.x;
        double dz = pos.z - center.z;
        double horizSq = dx * dx + dz * dz;
        double dy = Math.abs(pos.y - center.y);

        return horizSq <= closeRangeSq && dy <= 4.0;
    }

    private double distSqToTarget() {
        Vec3 pos = builder.position();
        Vec3 center = Vec3.atCenterOf(target);
        return pos.distanceToSqr(center);
    }

    @Override
    public boolean shouldAbort(ServerLevel level) {
        return !builder.hasSite() || builder.getAssignedSite() != site;
    }

    @Override
    public String getRetryKey() {
        return "MoveCloseNear:" + target.asLong() + ":" + anchor.asLong();
    }

    @Override
    public int getMaxFailureRetries() {
        return 2;
    }

    @Override
    public int getRetryDelayTicks() {
        return 15;
    }

    @Override
    public String getName() {
        return "MoveCloseToTargetNearAnchor:" + target.toShortString() + "@" + anchor.toShortString();
    }
}
