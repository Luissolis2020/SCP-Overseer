package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.ai.navigation.*;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.facility.construction.ConstructionState;
import net.luis.scpmod.facility.regression.BuilderRegressionTracker;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.*;

import static net.luis.scpmod.network.SCPModNetwork.syncPDAStateTo;

public class BuilderNPC extends PathfinderMob {

    // Baseline (rough player-like) tuning:
    // private static final double DEFAULT_WALK_SPEED = 0.10D;
    // private static final double DEFAULT_NAVIGATION_SPEED = 1.0D;
    // private static final int DEFAULT_PLACE_TICKS = 8;
    // private static final int DEFAULT_DIG_TICKS = 10;
    // private static final int DEFAULT_BUILD_SWING_TICKS = 8;
    // private static final int DEFAULT_DIG_SWING_TICKS = 6;
    // private static final double DEFAULT_MANUAL_MOVE_SPEED = 0.35D;
    // private static final double DEFAULT_MANUAL_MOVE_STEP = 0.12D;

    // Debug tuning (active): intentionally faster so construction iteration is quick.
    private static final double DEFAULT_WALK_SPEED = 0.50D;
    private static final double DEFAULT_NAVIGATION_SPEED = 1.0D;
    private static final int DEFAULT_PLACE_TICKS = 2;
    private static final int DEFAULT_DIG_TICKS = 2;
    private static final int DEFAULT_BUILD_SWING_TICKS = 8;
    private static final int DEFAULT_DIG_SWING_TICKS = 6;
    private static final double DEFAULT_MANUAL_MOVE_SPEED = 0.60D;
    private static final double DEFAULT_MANUAL_MOVE_STEP = 0.25D;

    private ConstructionSite assignedSite;
    public boolean needsSiteRecovery = false;
    public BlockPos siteToRecover = null;

    private BuildPhase currentPhase = BuildPhase.IDLE;
    private int idleCooldown = 0;

    public float swingProgress;
    public float oSwingProgress;

    private int recoveryTickCounter = 0;
    private int recoveryAttempts = 0;

    private boolean justResumedFromNBT = false;
    private static final int MAX_CANDIDATES_SCAN = 200;

    private final Map<BlockPos, Long> badStandUntil = new HashMap<>();
    private final Map<BlockPos, Long> badTaskUntil  = new HashMap<>();

    private static final int BAD_STAND_TTL = 60; // 3s
    private static final int BAD_TASK_TTL  = 200; // 10s (placement/default)
    private static final int BAD_DIG_TASK_TTL = 40; // 2s

    private final PathJobQueue pathJobQueue = new PathJobQueue(this);

    private int escapeCooldown = 0;
    private int placementRetryCooldown = 0;
    private int placementEscapeCooldown = 0;
    private @Nullable BlockPos lastPlacementEscapeTarget = null;
    private int noReachablePlacementStreak = 0;
    private static final int NO_REACHABLE_RECOVERY_TICKS = 25;
    private long lastEmergencyUnstuckTick = Long.MIN_VALUE;
    private static final int EMERGENCY_UNSTUCK_COOLDOWN = 40;

    private final Map<BlockPos, Map<BlockPos, Long>> perTargetBadStandsUntil = new HashMap<>();
    private static final int BAD_TARGET_STAND_TTL = 200; // 10s
    private static final int PATH_QUERIES_BUDGET_PER_TICK = 96;
    private long pathQueryTick = Long.MIN_VALUE;
    private int pathQueriesThisTick = 0;
    private final Map<BlockPos, Boolean> pathReachabilityCache = new HashMap<>();

    // Per-builder tuning (for future "worker tiers" and research balancing)
    private double walkSpeed = DEFAULT_WALK_SPEED;
    private double navigationSpeed = DEFAULT_NAVIGATION_SPEED;
    private int placeTicksPerBlock = DEFAULT_PLACE_TICKS;
    private int digTicksPerBlock = DEFAULT_DIG_TICKS;
    private int buildSwingTicks = DEFAULT_BUILD_SWING_TICKS;
    private int digSwingTicks = DEFAULT_DIG_SWING_TICKS;
    private double manualMoveSpeed = DEFAULT_MANUAL_MOVE_SPEED;
    private double manualMoveStep = DEFAULT_MANUAL_MOVE_STEP;

    public BuilderNPC(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    public enum BuildPhase {
        NAVIGATING_TO_SITE,
        DIGGING,
        BUILDING,
        IDLE
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F) {
            @Override public boolean canUse() { return !BuilderNPC.this.hasSite() && super.canUse(); }
            @Override public boolean canContinueToUse() { return !BuilderNPC.this.hasSite() && super.canContinueToUse(); }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override public boolean canUse() { return !BuilderNPC.this.hasSite() && super.canUse(); }
            @Override public boolean canContinueToUse() { return !BuilderNPC.this.hasSite() && super.canContinueToUse(); }
        });
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this) {
            @Override public boolean canUse() { return !BuilderNPC.this.hasSite() && super.canUse(); }
            @Override public boolean canContinueToUse() { return !BuilderNPC.this.hasSite() && super.canContinueToUse(); }
        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, DEFAULT_WALK_SPEED);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation nav = new GroundPathNavigation(this, level);
        nav.setCanOpenDoors(true);
        nav.setCanPassDoors(true);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    public void tick() {
        super.tick();
        resetPathQueryBudgetIfNeeded();
        applyMovementSpeedFromTuning();

        if (escapeCooldown > 0) escapeCooldown--;
        if (placementRetryCooldown > 0) placementRetryCooldown--;
        if (placementEscapeCooldown > 0) placementEscapeCooldown--;

        if (this.level() instanceof ServerLevel serverLevel) {
            pathJobQueue.tick(serverLevel);
        }

        // --- Site Recovery Logic with Delay ---
        if (this.needsSiteRecovery && this.siteToRecover != null && this.level() instanceof ServerLevel serverLevel) {
            if (recoveryTickCounter++ < 10) {
                return;
            }

            FacilityManager facilityManager = FacilityManager.get(serverLevel);
            ConstructionSite site = facilityManager.getSite(this.siteToRecover);

            if (site != null) {
                if (!site.hasPendingWork()) {
                    System.out.println("[BuilderNPC] Recovered site has no remaining work.");
                    this.needsSiteRecovery = false;
                    this.siteToRecover = null;
                    this.recoveryTickCounter = 0;
                    this.recoveryAttempts = 0;
                    return;
                }

                this.pathJobQueue.clear();
                this.assignToSite(site);
                this.needsSiteRecovery = false;
                this.siteToRecover = null;
                this.recoveryTickCounter = 0;
                this.recoveryAttempts = 0;
                this.justResumedFromNBT = false;

                site.pruneInvalidDigTargets(serverLevel);
                site.pruneInvalidPlacementTargets(serverLevel);
                site.promoteDeferredTasksIfReachable(serverLevel);

                if (this.assignedSite != null) {
                    if (site.hasBlocksToDig()) {
                        setCurrentPhase(BuildPhase.DIGGING);
                    } else if (site.hasBlocksToPlace()) {
                        setCurrentPhase(BuildPhase.BUILDING);
                    } else {
                        setCurrentPhase(BuildPhase.IDLE);
                    }
                }

                System.out.println("[BuilderNPC] Successfully recovered assigned site: " + site.getOrigin());

                serverLevel.players().forEach(player -> {
                    if (player.distanceToSqr(this) <= 64 * 64) {
                        player.sendSystemMessage(Component.literal("§a[Builder] Resumed construction at site: "
                                + site.getOrigin().toShortString()));
                    }
                });
                return;
            } else {
                if (recoveryAttempts++ > 100) {
                    System.out.println("[BuilderNPC] Failed to recover site after 100 attempts. Giving up.");

                    this.needsSiteRecovery = false;
                    this.siteToRecover = null;
                    this.recoveryTickCounter = 0;
                    this.recoveryAttempts = 0;

                    this.justResumedFromNBT = false;
                    this.clearAssignedSite();
                    this.setCurrentPhase(BuildPhase.IDLE);
                }
                return;
            }
        }

        // --- Normal Build Logic ---
        if (assignedSite != null) {
            if (this.level() instanceof ServerLevel sl) {
                // If no work remains, finalize once and stop working
                if (!assignedSite.hasPendingWork()) {
                    setCurrentPhase(BuildPhase.IDLE);
                    pathJobQueue.clear();
                    return;
                }
            }
            if (!assignedSite.hasBuilder()) {
                assignedSite.assignBuilder(this);
            }

            if (!needsSiteRecovery && !justResumedFromNBT) {
                // Hard recovery: if we drift too far from the work area, drop local reposition jobs
                // and force a return to the construction site.
                if (isFarFromAssignedSite()) {
                    if (!(pathJobQueue.getCurrentJob() instanceof PathJobMoveToConstructionSite)) {
                        pathJobQueue.clear();
                    }
                    if (!pathJobQueue.isBusy()) {
                        setCurrentPhase(BuildPhase.NAVIGATING_TO_SITE);
                        pathJobQueue.enqueue(new PathJobMoveToConstructionSite(this, assignedSite));
                    }
                    return;
                }

                if (!isAtAssignedSite()) {
                    if (!pathJobQueue.isBusy()) {
                        setCurrentPhase(BuildPhase.NAVIGATING_TO_SITE);
                        pathJobQueue.enqueue(new PathJobMoveToConstructionSite(this, assignedSite));
                    }
                    return;
                } else {
                    if (!pathJobQueue.isBusy()) {
                        if (currentPhase == BuildPhase.BUILDING && assignedSite.hasBlocksToPlace()) {
                            // stay BUILDING
                        } else if (!assignedSite.isDiggingComplete()) {
                            setCurrentPhase(BuildPhase.DIGGING);
                        } else if (assignedSite.hasBlocksToPlace()) {
                            setCurrentPhase(BuildPhase.BUILDING);
                        } else {
                            setCurrentPhase(BuildPhase.IDLE);
                        }
                    }
                }
            }

            if (currentPhase == BuildPhase.DIGGING && !pathJobQueue.isBusy()) {
                BlockPos target = assignedSite.getNearbyDigTarget((ServerLevel)this.level(), this, this.blockPosition(), 6.0);

                if (target != null) {
                    System.out.println("[BuilderNPC] Queuing dig job at: " + target);
                    pathJobQueue.enqueue(new PathJobDigBlock(this, assignedSite, target, false));
                } else {
                    if (assignedSite.hasBlocksToDig()) {
                        if (escapeCooldown <= 0) {
                            // First, try repositioning near a pending dig target.
                            // This helps for leftover high-corner cells where no strict stand exists.
                            if (tryQueueMoveNearPendingDigTarget((ServerLevel) this.level(), assignedSite)) {
                                escapeCooldown = 20;
                                return;
                            }

                            // Underground/mountain fallback: carve access toward pending dig work
                            // before generic local reposition.
                            if (tryQueueDigAccessToPendingTarget((ServerLevel) this.level(), assignedSite)) {
                                escapeCooldown = 20;
                                return;
                            }
                            if (tryQueueLocalUnstuckMove((ServerLevel) this.level(), assignedSite)) {
                                escapeCooldown = 20;
                                return;
                            }
                            if (tryEmergencyDigUnstuck((ServerLevel) this.level(), assignedSite)) {
                                escapeCooldown = 20;
                                return;
                            }

                            BlockPos escape = assignedSite.findExteriorStand((ServerLevel) level(), this, this.blockPosition(), 8);
                            if (escape != null) {
                                if (this.blockPosition().closerThan(escape, 2.0)) {
                                    // Avoid enqueueing no-op moves that complete immediately every second.
                                    escapeCooldown = 60;
                                } else {
                                    System.out.println("[BuilderNPC] No dig target found. Escaping to " + escape);
                                    pathJobQueue.enqueue(new PathJobMoveToPos(this, assignedSite, escape));
                                    escapeCooldown = 40; // give pathing state time to settle
                                }
                            } else {
                                // Opportunistically clear stale dig entries before staying in DIGGING loop.
                                assignedSite.pruneInvalidDigTargets((ServerLevel) this.level());
                                if (!assignedSite.hasBlocksToDig()) {
                                    setCurrentPhase(BuildPhase.BUILDING);
                                    return;
                                }
                                System.out.println("[BuilderNPC] No dig target and no escape stand found.");
                                escapeCooldown = 40;
                            }
                        }
                    } else {
                        setCurrentPhase(BuildPhase.BUILDING);
                    }
                }
            }


            if (this.level() instanceof ServerLevel sLevel && currentPhase == BuildPhase.DIGGING) {
                visualizeDiggingParticles(sLevel);
            }

            // === BUILDING job enqueue ===
            if (currentPhase == BuildPhase.BUILDING && !pathJobQueue.isBusy()) {
                if (placementRetryCooldown > 0) return;
                // promote deferred tasks that are now placeable (air/replaceable)
                assignedSite.promoteDeferredTasksIfReachable((ServerLevel) this.level());

                ConstructionSite.BlockPlacementTask task = nextPlacementTask((ServerLevel) this.level(), assignedSite);
                if (task != null) {
                    System.out.println("[BuilderNPC] Queuing placement job at: " + task.worldPos());
                    pathJobQueue.enqueue(new PathJobPlaceBlock(this, assignedSite, task));
                }
                else {
                    if (!assignedSite.isDiggingComplete()) {
                        setCurrentPhase(BuildPhase.DIGGING);
                    } else if (assignedSite.hasBlocksToPlace()) {
                        setCurrentPhase(BuildPhase.BUILDING);
                    } else {
                        setCurrentPhase(BuildPhase.IDLE);
                    }
                    placementRetryCooldown = 10;
                }
            }
        } else {
            if (idleCooldown-- <= 0) {
                setCurrentPhase(BuildPhase.IDLE);
                idleCooldown = 60;
            }
        }
        if (justResumedFromNBT && recoveryTickCounter > 20) {
            justResumedFromNBT = false;
        }
    }

    private void visualizeDiggingParticles(ServerLevel serverLevel) {
        if (assignedSite == null) return;
        BlockPos builderPos = this.blockPosition();
        double radius = 10.0;

        TreeMap<Integer, Queue<BlockPos>> digLayers = assignedSite.getDigLayers();
        int highestLayer = digLayers.isEmpty() ? 0 : digLayers.lastKey();

        for (var entry : digLayers.entrySet()) {
            int layerY = entry.getKey();
            Queue<BlockPos> layerQueue = entry.getValue();

            for (BlockPos pos : layerQueue) {
                if (pos.distSqr(builderPos) <= radius * radius) {
                    double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
                    double y = pos.getY() + 0.1;
                    double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;

                    if (layerY >= highestLayer) {
                        serverLevel.sendParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0.05, 0, 0.001);
                    } else if (layerY >= highestLayer - 3) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0.05, 0, 0.001);
                    } else {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0, 0.05, 0, 0.001);
                    }
                }
            }
        }
    }

    private boolean tryQueueDigAccessToPendingTarget(ServerLevel level, ConstructionSite site) {
        BlockPos pending = findNearestPendingDigTarget(level, site);
        if (pending == null) return false;

        // If we're already close enough for emergency direct-dig fallback, prefer that over carving new access holes.
        if (!isTaskBlacklisted(level, pending) && canDirectDigFallback(level, pending)) {
            pathJobQueue.enqueue(new PathJobDigBlock(this, site, pending, false));
            return true;
        }

        BlockPos access = findDiggableAccessObstacle(level, site, this.blockPosition(), pending);
        boolean queuedAny = false;
        int queuedCount = 0;
        BlockPos primarySeed = access != null ? access : pending;

        // Prefer opening a headspace + direct obstacle first.
        for (BlockPos candidate : buildAccessDigCandidates(primarySeed)) {
            if (queuedCount >= 2) break;
            if (queueAccessDigCandidate(level, site, candidate)) {
                queuedAny = true;
                queuedCount++;
            }
        }
        // If line-obstacle probing failed to produce queueable digs, also try around the pending block itself.
        if (!queuedAny && access != null && !access.equals(pending)) {
            for (BlockPos candidate : buildAccessDigCandidates(pending)) {
                if (queuedCount >= 2) break;
                if (queueAccessDigCandidate(level, site, candidate)) {
                    queuedAny = true;
                    queuedCount++;
                }
            }
        }

        if (queuedAny) {
            System.out.println("[BuilderNPC] Queuing access dig toward pending target " + pending + " via " + primarySeed);
        } else {
            // Prevent immediate re-selection of the exact same dead-end.
            markBadDigTask(level, pending);
            if (access != null) markBadDigTask(level, access);
        }
        return queuedAny;
    }

    private List<BlockPos> buildAccessDigCandidates(BlockPos access) {
        LinkedHashSet<BlockPos> out = new LinkedHashSet<>(32);
        out.add(access.above());
        out.add(access);
        out.add(access.below());
        for (Direction d : Direction.Plane.HORIZONTAL) {
            out.add(access.above().relative(d));
        }
        for (Direction d : Direction.Plane.HORIZONTAL) {
            out.add(access.relative(d));
        }
        for (Direction d : Direction.Plane.HORIZONTAL) {
            out.add(access.below().relative(d));
        }
        // Corner blocks frequently gate pathing in underground cuts.
        for (int dx : new int[]{-1, 1}) {
            for (int dz : new int[]{-1, 1}) {
                out.add(access.offset(dx, 1, dz));
                out.add(access.offset(dx, 0, dz));
            }
        }
        // One block farther in cardinal directions can unlock short tunnels.
        for (Direction d : Direction.Plane.HORIZONTAL) {
            out.add(access.relative(d, 2));
            out.add(access.above().relative(d, 2));
        }
        return new ArrayList<>(out);
    }

    private boolean queueAccessDigCandidate(ServerLevel level, ConstructionSite site, BlockPos candidate) {
        if (!isWithinAccessDigBounds(site, candidate)) return false;
        if (isTaskBlacklisted(level, candidate)) return false;
        if (!isAccessDigCandidate(level, site, candidate)) return false;

        // Do not enqueue access dig jobs that are guaranteed to fail start due to no stand.
        if (findStandPosForDig(level, candidate) == null) {
            markBadDigTask(level, candidate);
            return false;
        }

        pathJobQueue.enqueue(new PathJobDigBlock(this, site, candidate, true));
        return true;
    }

    private boolean isAccessDigCandidate(ServerLevel level, ConstructionSite site, BlockPos pos) {
        BlockState expected = site.getExpectedBlockState(pos);
        if (ConstructionSite.shouldStillBeDug(level, pos, expected)) {
            return true;
        }
        return isDiggableAccessObstacle(level, site, pos);
    }

    private @Nullable BlockPos findNearestPendingDigTarget(ServerLevel level, ConstructionSite site) {
        BlockPos from = this.blockPosition();

        // Keep fallback targeting on the highest unresolved layer first so leftover ceiling
        // cells are not starved by lower-layer noise.
        for (Map.Entry<Integer, Queue<BlockPos>> entry : site.getDigLayers().descendingMap().entrySet()) {
            BlockPos best = null;
            double bestDist = Double.POSITIVE_INFINITY;
            BlockPos bestBlacklisted = null;
            double bestBlacklistedDist = Double.POSITIVE_INFINITY;

            for (BlockPos pos : entry.getValue()) {
                BlockState expected = site.getExpectedBlockState(pos);
                if (!ConstructionSite.shouldStillBeDug(level, pos, expected)) continue;

                double d = pos.distSqr(from);
                if (isTaskBlacklisted(level, pos)) {
                    if (d < bestBlacklistedDist) {
                        bestBlacklistedDist = d;
                        bestBlacklisted = pos.immutable();
                    }
                    continue;
                }

                if (d < bestDist) {
                    bestDist = d;
                    best = pos.immutable();
                }
            }

            if (best != null || bestBlacklisted != null) {
                return best != null ? best : bestBlacklisted;
            }
        }
        return null;
    }

    private boolean tryQueueMoveNearPendingDigTarget(ServerLevel level, ConstructionSite site) {
        BlockPos pending = findNearestPendingDigTarget(level, site);
        if (pending == null) return false;

        if (!isTaskBlacklisted(level, pending) && canDirectDigFallback(level, pending)) {
            pathJobQueue.enqueue(new PathJobDigBlock(this, site, pending, false));
            return true;
        }

        // Avoid spamming move-close jobs for targets that currently have no viable approach.
        BlockPos approachHint = findApproachPosNearDigTarget(level, site, pending);
        if (approachHint == null) return false;
        if (this.blockPosition().closerThan(approachHint, 2.0)) return false;

        BlockPos anchor = site.getFootprintCenter();
        System.out.println("[BuilderNPC] Repositioning near pending dig target " + pending + " near anchor " + anchor);
        pathJobQueue.enqueue(new PathJobMoveCloseToTargetNearAnchor(this, site, pending, anchor, 4.0));
        return true;
    }

    private boolean tryQueueLocalUnstuckMove(ServerLevel level, ConstructionSite site) {
        BlockPos from = this.blockPosition();
        BlockPos pending = findNearestPendingDigTarget(level, site);
        BlockPos best = null;
        double bestScore = Double.POSITIVE_INFINITY;

        int siteFloorY = site.getOrigin().getY();
        int minY = Math.max(siteFloorY, from.getY() - 1);
        int maxY = Math.min(site.getOrigin().getY() + site.getHeight() + 2, from.getY() + 3);
        int minX = site.getFootprintMinX() - 2;
        int maxX = site.getFootprintMaxX() + 2;
        int minZ = site.getFootprintMinZ() - 2;
        int maxZ = site.getFootprintMaxZ() + 2;

        int[] dyOrder = new int[]{0, 1, -1, 2, -2};
        double curPendingDist = pending != null ? from.distSqr(pending) : Double.NaN;
        for (int dy : dyOrder) {
            int y = from.getY() + dy;
            if (y < minY || y > maxY) continue;

            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    BlockPos stand = new BlockPos(from.getX() + dx, y, from.getZ() + dz);
                    if (stand.getX() < minX || stand.getX() > maxX || stand.getZ() < minZ || stand.getZ() > maxZ) continue;
                    double distSq = stand.distSqr(from);
                    if (distSq < 2.25 || distSq > 25.0) continue;
                    if (isStandBlacklisted(level, stand)) continue;
                    if (!isStandable(level, stand)) continue;
                    if (!canReachStandNow(level, stand)) continue;
                    if (pending != null) {
                        double nextPendingDist = stand.distSqr(pending);
                        // Don't drift if this move does not improve access to pending dig work.
                        if (nextPendingDist >= (curPendingDist - 0.75)) continue;
                    }

                    int drop = from.getY() - stand.getY();
                    double dropPenalty = drop > 0 ? drop * 60.0 : 0.0;
                    double score = distSq + dropPenalty;
                    if (score < bestScore) {
                        bestScore = score;
                        best = stand.immutable();
                    }
                }
            }
        }

        if (best == null) return false;
        System.out.println("[BuilderNPC] Local unstuck reposition to " + best);
        pathJobQueue.enqueue(new PathJobMoveToPos(this, site, best));
        return true;
    }

    private @Nullable BlockPos findApproachPosNearDigTarget(ServerLevel level, ConstructionSite site, BlockPos target) {
        BlockPos best = null;
        double bestScore = Double.POSITIVE_INFINITY;

        boolean highTarget = target.getY() >= this.blockPosition().getY() + 2;
        final int maxHoriz = highTarget ? 7 : 5;
        final double maxTargetDistSq = highTarget ? 64.0 : 49.0;
        int[] dyOrder = highTarget
                ? new int[]{-2, -1, 0, -3, 1, -4, 2, 3}
                : new int[]{0, 1, -1, 2, -2};
        int minY = site.getOrigin().getY();
        int maxY = site.getOrigin().getY() + site.getHeight() + 2;

        for (int dy : dyOrder) {
            int y = target.getY() + dy;
            if (y < minY || y > maxY) continue;

            for (int dx = -maxHoriz; dx <= maxHoriz; dx++) {
                for (int dz = -maxHoriz; dz <= maxHoriz; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    BlockPos stand = new BlockPos(target.getX() + dx, y, target.getZ() + dz);
                    if (stand.distSqr(target) > maxTargetDistSq) continue;
                    if (!isStandable(level, stand)) continue;
                    if (isStandBlacklisted(level, stand)) continue;
                    if (!canReachStandNow(level, stand)) continue;

                    double toTarget = stand.distSqr(target);
                    double toBuilder = stand.distSqr(this.blockPosition());
                    double score = (toTarget * 8.0) + toBuilder;
                    if (score < bestScore) {
                        bestScore = score;
                        best = stand.immutable();
                    }
                }
            }
        }
        return best;
    }

    private @Nullable BlockPos findDiggableAccessObstacle(ServerLevel level, ConstructionSite site, BlockPos from, BlockPos toward) {
        double sx = from.getX() + 0.5;
        double sy = from.getY() + 0.5;
        double sz = from.getZ() + 0.5;

        double tx = toward.getX() + 0.5;
        double ty = toward.getY() + 0.5;
        double tz = toward.getZ() + 0.5;

        double dx = tx - sx;
        double dy = ty - sy;
        double dz = tz - sz;
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-4) return null;

        double nx = dx / len;
        double ny = dy / len;
        double nz = dz / len;

        for (int step = 1; step <= 6; step++) {
            int px = Mth.floor(sx + nx * step);
            int py = Mth.floor(sy + ny * step);
            int pz = Mth.floor(sz + nz * step);

            for (int yOff : new int[]{0, 1, -1, 2, -2}) {
                BlockPos probe = new BlockPos(px, py + yOff, pz);
                if (!isWithinAccessDigBounds(site, probe)) continue;
                if (isDiggableAccessObstacle(level, site, probe)) {
                    return probe.immutable();
                }
            }
        }
        return null;
    }

    private boolean isWithinAccessDigBounds(ConstructionSite site, BlockPos pos) {
        int minX = site.getFootprintMinX() - 3;
        int maxX = site.getFootprintMaxX() + 3;
        int minZ = site.getFootprintMinZ() - 3;
        int maxZ = site.getFootprintMaxZ() + 3;
        int minY = site.getOrigin().getY();
        int maxY = site.getOrigin().getY() + site.getHeight() + 2;

        return pos.getX() >= minX && pos.getX() <= maxX
                && pos.getZ() >= minZ && pos.getZ() <= maxZ
                && pos.getY() >= minY && pos.getY() <= maxY;
    }

    private boolean tryEmergencyDigUnstuck(ServerLevel level, ConstructionSite site) {
        long now = level.getServer().getTickCount();
        if (lastEmergencyUnstuckTick != Long.MIN_VALUE
                && (now - lastEmergencyUnstuckTick) < EMERGENCY_UNSTUCK_COOLDOWN) {
            return false;
        }

        if (!this.onGround()) return false;

        BlockPos feet = this.blockPosition();
        BlockPos supportPos = feet.below();
        int siteFloorY = site.getOrigin().getY();
        // Never dig ourselves below the planned site floor.
        if (feet.getY() <= siteFloorY + 1) return false;
        if (supportPos.getY() < siteFloorY) return false;

        BlockState support = level.getBlockState(supportPos);
        if (support.isAir() || support.canBeReplaced()) return false;
        if (!support.getFluidState().isEmpty()) return false;
        if (support.getDestroySpeed(level, supportPos) < 0.0F) return false;
        if (isTaskBlacklisted(level, supportPos)) return false;

        BlockState expectedSupport = site.getExpectedBlockState(supportPos);
        boolean canDigSupport =
                ConstructionSite.shouldStillBeDug(level, supportPos, expectedSupport)
                        || isDiggableAccessObstacle(level, site, supportPos);
        if (!canDigSupport) return false;

        // Ensure there's a safe-ish landing zone if we remove the support.
        int landingY = Integer.MIN_VALUE;
        int maxDrop = 4;
        for (int dy = 2; dy <= maxDrop + 1; dy++) {
            BlockPos floor = feet.below(dy);
            BlockState floorState = level.getBlockState(floor);
            if (!floorState.isAir() && !floorState.canBeReplaced()) {
                landingY = floor.getY() + 1;
                break;
            }
        }
        if (landingY == Integer.MIN_VALUE) return false;

        int drop = feet.getY() - landingY;
        if (drop < 1 || drop > maxDrop) return false;

        BlockPos landing = new BlockPos(feet.getX(), landingY, feet.getZ());
        if (!landing.equals(supportPos)) {
            BlockState landingFeet = level.getBlockState(landing);
            if (!landingFeet.isAir() && !landingFeet.canBeReplaced()) return false;
        }
        BlockState landingHead = level.getBlockState(landing.above());
        if (!landingHead.isAir() && !landingHead.canBeReplaced()) return false;

        if (!level.destroyBlock(supportPos, true, this)) return false;

        this.swing(InteractionHand.MAIN_HAND);
        lastEmergencyUnstuckTick = now;
        markBadDigTask(level, supportPos);
        badStandUntil.clear();
        perTargetBadStandsUntil.clear();
        pathReachabilityCache.clear();
        pathQueriesThisTick = 0;
        System.out.println("[BuilderNPC] Emergency unstuck: removed support at " + supportPos + ", drop=" + drop);
        return true;
    }

    private boolean isDiggableAccessObstacle(ServerLevel level, ConstructionSite site, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.canBeReplaced()) return false;
        if (!state.getFluidState().isEmpty()) return false;
        if (state.getDestroySpeed(level, pos) < 0.0F) return false;

        BlockState expected = site.getExpectedBlockState(pos);
        return expected == null || expected.isAir();
    }

    private @Nullable ConstructionSite.BlockPlacementTask nextPlacementTask(ServerLevel level, ConstructionSite site) {
        int buildPending = site.getBuildLayerQueues().stream().mapToInt(Queue::size).sum();
        int deferredPending = site.getDeferredLayerQueues().stream().mapToInt(Queue::size).sum();
        int scaffoldPending = site.getScaffoldLayerQueues().stream().mapToInt(Queue::size).sum();

        // If only scaffold tasks remain, they are stale support tasks and should not block completion.
        if (buildPending == 0 && deferredPending == 0 && scaffoldPending > 0) {
            int removed = site.discardPendingScaffoldTasks();
            if (removed > 0) {
                System.out.println("[BuilderNPC] Dropped stale scaffold-only tasks: " + removed);
                noReachablePlacementStreak = 0;
                return null;
            }
        }

        // 1) Prefer real structure blocks first.
        ConstructionSite.BlockPlacementTask bestBuild = pickBestCandidate(level, site, site.getBuildLayerQueues(), false);
        if (bestBuild != null) {
            noReachablePlacementStreak = 0;
            return bestBuild;
        }

        // 2) Then deferred structure blocks.
        ConstructionSite.BlockPlacementTask bestDeferred = pickBestCandidate(level, site, site.getDeferredLayerQueues(), false);
        if (bestDeferred != null) {
            noReachablePlacementStreak = 0;
            return bestDeferred;
        }

        // 3) Then scaffold support blocks.
        ConstructionSite.BlockPlacementTask bestScaffold = pickBestCandidate(level, site, site.getScaffoldLayerQueues(), true);
        if (bestScaffold != null) {
            noReachablePlacementStreak = 0;
            return bestScaffold;
        }

        // Last-mile fallback: if only a few tasks remain, ignore temporary task cooldown/blacklist
        // so we don't sit idle waiting for TTL expiry on the final blocks.
        int pending = countPendingPlacementTasks(site);
        if (pending <= 6) {
            ConstructionSite.BlockPlacementTask lmBuild = pickBestCandidateLastMile(level, site, site.getBuildLayerQueues(), false);
            if (lmBuild != null) {
                noReachablePlacementStreak = 0;
                return lmBuild;
            }

            ConstructionSite.BlockPlacementTask lmDeferred = pickBestCandidateLastMile(level, site, site.getDeferredLayerQueues(), false);
            if (lmDeferred != null) {
                noReachablePlacementStreak = 0;
                return lmDeferred;
            }

            ConstructionSite.BlockPlacementTask lmScaffold = pickBestCandidateLastMile(level, site, site.getScaffoldLayerQueues(), true);
            if (lmScaffold != null) {
                noReachablePlacementStreak = 0;
                return lmScaffold;
            }
        }

        // 4) Nothing reachable: only plan more scaffolds if there aren't already scaffold tasks pending.
        int nonScaffoldPending = buildPending + deferredPending;
        boolean allowScaffoldPlanning =
                scaffoldPending == 0
                        && nonScaffoldPending > 0
                        && nonScaffoldPending <= 24
                        && noReachablePlacementStreak >= 4;
        if (allowScaffoldPlanning) {
            ConstructionSite.BlockPlacementTask scaffoldCandidate = findScaffoldPlanCandidate(level, site, 16);
            if (scaffoldCandidate != null) {
                site.deferPlacementTask(scaffoldCandidate);
                noReachablePlacementStreak = 0;
                return null;
            }
        }

        // 5) Fallback for enclosed/mountain builds: carve access toward a pending placement.
        if ((buildPending + deferredPending) > 0 && scaffoldPending == 0) {
            if (tryQueuePlacementAccessToPending(level, site)) {
                placementEscapeCooldown = 20;
                noReachablePlacementStreak = 0;
                return null;
            }
        }

        noReachablePlacementStreak++;
        BuilderRegressionTracker.get().recordNoReachablePlacement(site.getOrigin());
        if (noReachablePlacementStreak >= NO_REACHABLE_RECOVERY_TICKS && (buildPending + deferredPending) > 0) {
            int dropped = 0;
            if (scaffoldPending > 0) {
                dropped = site.discardPendingScaffoldTasks();
                if (dropped > 0) {
                    scaffoldPending = Math.max(0, scaffoldPending - dropped);
                    System.out.println("[BuilderNPC] Placement recovery: dropped scaffold backlog=" + dropped);
                }
            }

            // Clear transient pathing memory so we can retry fresh routes/stands.
            badStandUntil.clear();
            perTargetBadStandsUntil.clear();
            pathReachabilityCache.clear();
            pathQueriesThisTick = 0;
            placementEscapeCooldown = 0;
            lastPlacementEscapeTarget = null;
            noReachablePlacementStreak = 0;
        }

        if (noReachablePlacementStreak <= 3 || (noReachablePlacementStreak % 10) == 0) {
            System.out.println("[BuilderNPC] No reachable placement tasks. build="
                    + buildPending
                    + " deferred="
                    + deferredPending
                    + " scaffold="
                    + scaffoldPending);
        }

        // If builder is inside footprint and no reachable placement tasks, evacuate outside.
        if (site.isInsideFootprint(this.blockPosition())) {
            if (placementEscapeCooldown <= 0) {
                BlockPos escape = site.findExteriorStand(level, this, this.blockPosition(), 6);
                if (escape != null && !this.blockPosition().closerThan(escape, 2.0)
                        && (lastPlacementEscapeTarget == null
                        || !lastPlacementEscapeTarget.equals(escape)
                        || noReachablePlacementStreak >= 8)) {
                    System.out.println("[BuilderNPC] No reachable tasks while inside footprint. Escaping to " + escape);
                    pathJobQueue.enqueue(new PathJobMoveToPos(this, site, escape));
                    lastPlacementEscapeTarget = escape.immutable();
                }
                placementEscapeCooldown = 30;
            }
        }
        // If we're NOT inside footprint, still try a gentle reposition to refresh paths.
        if (!site.isInsideFootprint(this.blockPosition())) {
            if (placementEscapeCooldown <= 0) {
                BlockPos anchor = site.findExteriorStand(level, this, site.getFootprintCenter(), 4);
                if (anchor != null && !this.blockPosition().closerThan(anchor, 2.0)
                        && (lastPlacementEscapeTarget == null
                        || !lastPlacementEscapeTarget.equals(anchor)
                        || noReachablePlacementStreak >= 8)) {
                    pathJobQueue.enqueue(new PathJobMoveToPos(this, site, anchor));
                    lastPlacementEscapeTarget = anchor.immutable();
                }
                placementEscapeCooldown = 30;
            }
        }
        return null;

    }

    private @Nullable ConstructionSite.BlockPlacementTask pickBestCandidate(
            ServerLevel level,
            ConstructionSite site,
            Iterable<Queue<ConstructionSite.BlockPlacementTask>> layers,
            boolean isScaffoldPass
    ) {
        ConstructionSite.BlockPlacementTask best = null;
        double bestScore = Double.POSITIVE_INFINITY;

        int scanned = 0;
        Integer minPendingY = null;

        if (!isScaffoldPass) {
            for (var layer : layers) {
                for (var t : layer) {
                    if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                    if (!isPlacementEligibleNow(level, site, t)) continue;
                    if (isTaskBlacklisted(level, t.worldPos())) continue;
                    if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                    int y = t.worldPos().getY();
                    if (minPendingY == null || y < minPendingY) {
                        minPendingY = y;
                    }
                }
            }
        }

        for (var layer : layers) {
            for (var t : layer) {
                if (scanned++ > MAX_CANDIDATES_SCAN) break;

                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;

                if (!isScaffoldPass && !isPlacementEligibleNow(level, site, t)) continue;
                if (!isScaffoldPass && minPendingY != null && t.worldPos().getY() > (minPendingY + 1)) continue;

                if (isTaskBlacklisted(level, t.worldPos())) continue;

                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                BlockPos stand = findStandPosForPlacement(level, site, t.worldPos(), isScaffoldPass);
                if (stand == null) continue;
                if (isStandBlacklisted(level, stand)) continue;
                
                // Score: lower Y first, then “floor-like” first, then distance
                double score = scorePlacementCandidate(level, site, t, stand, isScaffoldPass);
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                }
            }
            if (scanned > MAX_CANDIDATES_SCAN) break;
        }

        // Fallback pass for non-scaffold tasks:
        // if the low-layer window has no valid candidates, widen selection to any eligible Y.
        if (best == null && !isScaffoldPass && minPendingY != null) {
            scanned = 0;
            for (var layer : layers) {
                for (var t : layer) {
                    if (scanned++ > MAX_CANDIDATES_SCAN) break;

                    if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                    if (!isPlacementEligibleNow(level, site, t)) continue;
                    if (isTaskBlacklisted(level, t.worldPos())) continue;
                    if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                    BlockPos stand = findStandPosForPlacement(level, site, t.worldPos(), false);
                    if (stand == null) continue;
                    if (isStandBlacklisted(level, stand)) continue;

                    double score = scorePlacementCandidate(level, site, t, stand, false);
                    if (score < bestScore) {
                        bestScore = score;
                        best = t;
                    }
                }
                if (scanned > MAX_CANDIDATES_SCAN) break;
            }
        }

        return best;
    }

    private double scorePlacementCandidate(ServerLevel level, ConstructionSite site,
                                           ConstructionSite.BlockPlacementTask t,
                                           BlockPos stand,
                                           boolean isScaffoldPass) {

        // 1) Y priority: build low to high (clean sweep)
        int y = t.worldPos().getY() - site.getOrigin().getY();

        // 2) floor-first bias (only for real structure blocks, not scaffolds)
        int floorBias = 0;
        if (!isScaffoldPass) {
            floorBias = isSolidBlock(t.state()) ? 0 : 5000; // big bias
        }

        // 3) distance from builder to stand pos
        double dist = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);

        int relY = t.worldPos().getY() - site.getOrigin().getY();

        boolean standInside = site.isInsideFootprint(stand) || site.isInsideFootprint(stand.below());
        double insidePenalty = (standInside && relY >= 2) ? 200000.0 : 0.0;

        double failPenalty = site.getPlacementFailCount(t.worldPos()) * 20000.0;

        return (y * 10000.0) + floorBias + insidePenalty + failPenalty + dist;
    }

    private boolean isSolidBlock(BlockState s) {
        if (s.is(Blocks.SCAFFOLDING)) return false;
        if (!s.canOcclude()) return false;
        return true;
    }

    private boolean isPlacementEligibleNow(ServerLevel level, ConstructionSite site, ConstructionSite.BlockPlacementTask t) {
        BlockPos pos = t.worldPos();
        BlockState desired = t.state();

        int relY = pos.getY() - site.getOrigin().getY();
        if (relY == 0) return true; // foundation layer always allowed

        if (hasSolidSupportBelow(level, pos)) return true;
        if (hasAdjacentStructureNeighborPlaced(level, site, pos)) return true;

        return false;
    }

    private boolean hasSolidSupportBelow(ServerLevel level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState s = level.getBlockState(below);
        return s.isFaceSturdy(level, below, Direction.UP) || s.is(Blocks.SCAFFOLDING);
    }

    private boolean hasAdjacentStructureNeighborPlaced(ServerLevel level, ConstructionSite site, BlockPos pos) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos n = pos.relative(d);
            BlockState expected = site.getExpectedBlockState(n);
            if (expected == null) continue;
            if (expected.isAir()) continue;

            BlockState cur = level.getBlockState(n);
            if (cur.equals(expected)) return true;
        }
        return false;
    }

    private @Nullable ConstructionSite.BlockPlacementTask findAnyTask(ServerLevel level, ConstructionSite site) {
        for (var layer : site.getBuildLayerQueues()) {
            for (var t : layer) if (!level.getBlockState(t.worldPos()).equals(t.state())) return t;
        }
        for (var layer : site.getDeferredLayerQueues()) {
            for (var t : layer) if (!level.getBlockState(t.worldPos()).equals(t.state())) return t;
        }
        return null;
    }

    private @Nullable ConstructionSite.BlockPlacementTask findScaffoldPlanCandidate(ServerLevel level, ConstructionSite site, int maxScan) {
        int scanned = 0;

        for (var layer : site.getBuildLayerQueues()) {
            for (var t : layer) {
                if (scanned++ >= maxScan) return null;
                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                if (isTaskBlacklisted(level, t.worldPos())) continue;
                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                if (site.planScaffoldFor(level, t.worldPos())) {
                    return t;
                }
            }
        }

        for (var layer : site.getDeferredLayerQueues()) {
            for (var t : layer) {
                if (scanned++ >= maxScan) return null;
                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                if (isTaskBlacklisted(level, t.worldPos())) continue;
                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                if (site.planScaffoldFor(level, t.worldPos())) {
                    return t;
                }
            }
        }
        return null;
    }

    private boolean tryQueuePlacementAccessToPending(ServerLevel level, ConstructionSite site) {
        ConstructionSite.BlockPlacementTask task = findNearestPendingPlacementTask(level, site);
        if (task == null) return false;

        BlockPos access = findDiggableAccessObstacle(level, site, this.blockPosition(), task.worldPos());
        if (access == null) return false;

        boolean queuedAny = false;
        int queuedCount = 0;
        for (BlockPos candidate : buildAccessDigCandidates(access)) {
            if (queuedCount >= 2) break;
            if (queueAccessDigCandidate(level, site, candidate)) {
                queuedAny = true;
                queuedCount++;
            }
        }
        if (queuedAny) {
            System.out.println("[BuilderNPC] Queuing placement access dig toward " + task.worldPos() + " via " + access);
        } else {
            // Don't immediately thrash the same impossible placement/access pair.
            markBadTask(level, task.worldPos());
            markBadTask(level, access);
        }
        return queuedAny;
    }

    private @Nullable ConstructionSite.BlockPlacementTask findNearestPendingPlacementTask(ServerLevel level, ConstructionSite site) {
        ConstructionSite.BlockPlacementTask best = null;
        double bestDist = Double.POSITIVE_INFINITY;
        BlockPos from = this.blockPosition();

        for (var layer : site.getBuildLayerQueues()) {
            for (var t : layer) {
                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                if (isTaskBlacklisted(level, t.worldPos())) continue;
                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                double d = t.worldPos().distSqr(from);
                if (d < bestDist) {
                    bestDist = d;
                    best = t;
                }
            }
        }
        for (var layer : site.getDeferredLayerQueues()) {
            for (var t : layer) {
                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                if (isTaskBlacklisted(level, t.worldPos())) continue;
                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                double d = t.worldPos().distSqr(from);
                if (d < bestDist) {
                    bestDist = d;
                    best = t;
                }
            }
        }
        return best;
    }


    public void assignToSite(ConstructionSite site) {
        if (this.assignedSite != null && this.assignedSite != site) {
            this.pathJobQueue.clear();
            this.badStandUntil.clear();
            this.badTaskUntil.clear();
            this.perTargetBadStandsUntil.clear();
            this.lastPlacementEscapeTarget = null;
            this.placementEscapeCooldown = 0;
        }
        this.assignedSite = site;
        site.assignBuilder(this);
        this.noReachablePlacementStreak = 0;

        if (!this.justResumedFromNBT) {
            if (!site.isDiggingComplete()) {
                setCurrentPhase(BuildPhase.DIGGING);
            } else if (site.hasBlocksToPlace()) {
                setCurrentPhase(BuildPhase.BUILDING);

            } else {
                setCurrentPhase(BuildPhase.IDLE);
            }
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.players().forEach(p -> {
                if (p.distanceToSqr(site.getOrigin().getCenter()) < 64 * 64) {
                    SCPModNetwork.syncFacilityRoomsTo(p);
                }
            });
            FacilityManager.get(serverLevel).saveConstructionSite(site);
        }

        System.out.println("[BuilderNPC] Assigned to site at " + site.getOrigin() + ", phase = " + this.currentPhase);

    }

    public boolean hasSite() {
        return this.assignedSite != null;
    }

    public ConstructionSite getAssignedSite() {
        return this.assignedSite;
    }

    public void clearAssignedSite() {
        if (this.assignedSite != null) {
            this.assignedSite.setBuilder(null);
        }
        this.assignedSite = null;
        this.pathJobQueue.clear();
        this.badStandUntil.clear();
        this.badTaskUntil.clear();
        this.perTargetBadStandsUntil.clear();
        this.noReachablePlacementStreak = 0;
        this.lastPlacementEscapeTarget = null;
        this.placementEscapeCooldown = 0;
        this.currentPhase = BuildPhase.IDLE;
    }

    private int countPendingPlacementTasks(ConstructionSite site) {
        int build = site.getBuildLayerQueues().stream().mapToInt(Queue::size).sum();
        int deferred = site.getDeferredLayerQueues().stream().mapToInt(Queue::size).sum();
        int scaffold = site.getScaffoldLayerQueues().stream().mapToInt(Queue::size).sum();
        return build + deferred + scaffold;
    }

    private @Nullable ConstructionSite.BlockPlacementTask pickBestCandidateLastMile(
            ServerLevel level,
            ConstructionSite site,
            Iterable<Queue<ConstructionSite.BlockPlacementTask>> layers,
            boolean isScaffoldPass
    ) {
        ConstructionSite.BlockPlacementTask best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int scanned = 0;

        for (var layer : layers) {
            for (var t : layer) {
                if (scanned++ > 120) break;
                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;
                if (!isScaffoldPass && !isPlacementEligibleNow(level, site, t)) continue;

                BlockPos stand = findStandPosForPlacement(level, site, t.worldPos(), isScaffoldPass);
                if (stand == null) continue;

                double score = scorePlacementCandidate(level, site, t, stand, isScaffoldPass);
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                }
            }
            if (scanned > 120) break;
        }
        return best;
    }

    public boolean isAtAssignedSite() {
        if (assignedSite == null) return false;

        BlockPos pos = this.blockPosition();

        int bufferXZ = 6;

        int minX = assignedSite.getFootprintMinX();
        int maxX = assignedSite.getFootprintMaxX();
        int minZ = assignedSite.getFootprintMinZ();
        int maxZ = assignedSite.getFootprintMaxZ();

        boolean insideXZ =
                pos.getX() >= minX - bufferXZ &&
                        pos.getX() <= maxX + bufferXZ &&
                        pos.getZ() >= minZ - bufferXZ &&
                        pos.getZ() <= maxZ + bufferXZ;

        // tolerate Y drift (terrain)
        boolean okY = Math.abs(pos.getY() - assignedSite.getOrigin().getY()) <= 8;

        return insideXZ && okY;
    }

    private boolean isFarFromAssignedSite() {
        if (assignedSite == null) return false;

        BlockPos pos = this.blockPosition();
        int minX = assignedSite.getFootprintMinX();
        int maxX = assignedSite.getFootprintMaxX();
        int minZ = assignedSite.getFootprintMinZ();
        int maxZ = assignedSite.getFootprintMaxZ();

        int farBufferXZ = 14;
        boolean insideFarXZ =
                pos.getX() >= minX - farBufferXZ &&
                        pos.getX() <= maxX + farBufferXZ &&
                        pos.getZ() >= minZ - farBufferXZ &&
                        pos.getZ() <= maxZ + farBufferXZ;

        boolean okFarY = Math.abs(pos.getY() - assignedSite.getOrigin().getY()) <= 14;
        return !(insideFarXZ && okFarY);
    }

    public BuildPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(BuildPhase phase) {
        if (this.currentPhase != phase) {
            System.out.println("[BuilderNPC] Phase changed to: " + phase);
            this.currentPhase = phase;
            if (this.level() instanceof ServerLevel && this.assignedSite != null) {
                BuilderRegressionTracker.get().recordPhaseChange(this.assignedSite.getOrigin());
            }
        }
    }

    @Override
    public net.minecraft.world.InteractionHand getUsedItemHand() {
        return net.minecraft.world.InteractionHand.MAIN_HAND;
    }

    public void holdItemTemporarily(ItemStack stack) {
        this.setItemInHand(this.getUsedItemHand(), stack);
    }

    public void clearHeldItem() {
        this.setItemInHand(this.getUsedItemHand(), ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putDouble("BuilderWalkSpeed", this.walkSpeed);
        tag.putDouble("BuilderNavSpeed", this.navigationSpeed);
        tag.putInt("BuilderPlaceTicks", this.placeTicksPerBlock);
        tag.putInt("BuilderDigTicks", this.digTicksPerBlock);
        tag.putInt("BuilderBuildSwingTicks", this.buildSwingTicks);
        tag.putInt("BuilderDigSwingTicks", this.digSwingTicks);
        tag.putDouble("BuilderManualMoveSpeed", this.manualMoveSpeed);
        tag.putDouble("BuilderManualMoveStep", this.manualMoveStep);

        if (this.assignedSite != null) {
            tag.putBoolean("NeedsSiteRecovery", true);
            tag.putLong("AssignedSitePos", this.assignedSite.getOrigin().asLong());
            tag.putString("AssignedSiteStructureId", this.assignedSite.getStructureId().toString());
            tag.putString("Phase", this.currentPhase.name());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.walkSpeed = tag.contains("BuilderWalkSpeed", Tag.TAG_DOUBLE) ? tag.getDouble("BuilderWalkSpeed") : DEFAULT_WALK_SPEED;
        this.navigationSpeed = tag.contains("BuilderNavSpeed", Tag.TAG_DOUBLE) ? tag.getDouble("BuilderNavSpeed") : DEFAULT_NAVIGATION_SPEED;
        this.placeTicksPerBlock = tag.contains("BuilderPlaceTicks", Tag.TAG_INT) ? tag.getInt("BuilderPlaceTicks") : DEFAULT_PLACE_TICKS;
        this.digTicksPerBlock = tag.contains("BuilderDigTicks", Tag.TAG_INT) ? tag.getInt("BuilderDigTicks") : DEFAULT_DIG_TICKS;
        this.buildSwingTicks = tag.contains("BuilderBuildSwingTicks", Tag.TAG_INT) ? tag.getInt("BuilderBuildSwingTicks") : DEFAULT_BUILD_SWING_TICKS;
        this.digSwingTicks = tag.contains("BuilderDigSwingTicks", Tag.TAG_INT) ? tag.getInt("BuilderDigSwingTicks") : DEFAULT_DIG_SWING_TICKS;
        this.manualMoveSpeed = tag.contains("BuilderManualMoveSpeed", Tag.TAG_DOUBLE) ? tag.getDouble("BuilderManualMoveSpeed") : DEFAULT_MANUAL_MOVE_SPEED;
        this.manualMoveStep = tag.contains("BuilderManualMoveStep", Tag.TAG_DOUBLE) ? tag.getDouble("BuilderManualMoveStep") : DEFAULT_MANUAL_MOVE_STEP;
        normalizeWorkTuning();
        applyMovementSpeedFromTuning();

        if (tag.getBoolean("NeedsSiteRecovery")) {
            this.pathJobQueue.clear();
            this.needsSiteRecovery = true;
            this.siteToRecover = BlockPos.of(tag.getLong("AssignedSitePos"));
            this.justResumedFromNBT = true;

            if (tag.contains("Phase", Tag.TAG_STRING)) {
                this.setCurrentPhase(BuildPhase.valueOf(tag.getString("Phase")));
            } else {
                this.setCurrentPhase(BuildPhase.NAVIGATING_TO_SITE);
            }

            System.out.println("[BuilderNPC] NBT load - needsSiteRecovery = true, siteToRecover = " + this.siteToRecover);
        }
    }

    @Override
    public void swing(InteractionHand hand, boolean fromServer) {
        super.swing(hand, fromServer);

        if (this.getCurrentPhase() == BuildPhase.BUILDING) {
            this.swingTime = 0;
            this.swinging = true;
        } else if (this.getCurrentPhase() == BuildPhase.DIGGING) {
            this.swingTime = 0;
            this.swinging = true;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.oSwingProgress = this.swingProgress;

        if (this.swinging) {
            this.swingTime++;
            if (this.swingTime >= this.getAttackDuration()) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }

        this.swingProgress = (float) this.swingTime / (float) this.getAttackDuration();
    }

    public int getAttackDuration() {
        if (this.getCurrentPhase() == BuildPhase.DIGGING) {
            return this.digSwingTicks;
        } else if (this.getCurrentPhase() == BuildPhase.BUILDING) {
            return this.buildSwingTicks;
        }
        return this.buildSwingTicks;
    }

    @Override
    public float getAttackAnim(float partialTicks) {
        return Mth.lerp(partialTicks, this.oSwingProgress, this.swingProgress);
    }

    public PathJobQueue getPathJobQueue() {
        return pathJobQueue;
    }

    public double getNavigationSpeed() {
        return this.navigationSpeed;
    }

    public int getPlaceTicksPerBlock() {
        return this.placeTicksPerBlock;
    }

    public int getDigTicksPerBlock() {
        return this.digTicksPerBlock;
    }

    public double getManualMoveSpeed() {
        return this.manualMoveSpeed;
    }

    public double getManualMoveStep() {
        return this.manualMoveStep;
    }

    public void configureWorkTuning(
            double walkSpeed,
            double navigationSpeed,
            int placeTicksPerBlock,
            int digTicksPerBlock,
            int buildSwingTicks,
            int digSwingTicks,
            double manualMoveSpeed,
            double manualMoveStep
    ) {
        this.walkSpeed = walkSpeed;
        this.navigationSpeed = navigationSpeed;
        this.placeTicksPerBlock = placeTicksPerBlock;
        this.digTicksPerBlock = digTicksPerBlock;
        this.buildSwingTicks = buildSwingTicks;
        this.digSwingTicks = digSwingTicks;
        this.manualMoveSpeed = manualMoveSpeed;
        this.manualMoveStep = manualMoveStep;

        normalizeWorkTuning();
        applyMovementSpeedFromTuning();
    }

    public void applyDebugWorkTuning() {
        configureWorkTuning(
                DEFAULT_WALK_SPEED,
                DEFAULT_NAVIGATION_SPEED,
                DEFAULT_PLACE_TICKS,
                DEFAULT_DIG_TICKS,
                DEFAULT_BUILD_SWING_TICKS,
                DEFAULT_DIG_SWING_TICKS,
                DEFAULT_MANUAL_MOVE_SPEED,
                DEFAULT_MANUAL_MOVE_STEP
        );
    }

    public void applyPlayerLikeWorkTuning() {
        // Player-like baseline kept here for experiments with slower, more realistic builders.
        configureWorkTuning(0.10D, 1.0D, 8, 10, 8, 6, 0.35D, 0.12D);
    }

    @Nullable
    public BlockPos findStandPosForPlacement(ServerLevel level, ConstructionSite site, BlockPos target, boolean isScaffold) {
        BlockPos best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        List<BlockPos> candidates = findStandCandidatesForPlacement(level, target);

        int relY = target.getY() - site.getOrigin().getY();
        boolean preferOutside = (!isScaffold && relY >= 2);

        for (BlockPos stand : candidates) {

            if (isStandBlacklisted(level, stand)) continue;
            if (!isStandable(level, stand)) continue;
            if (!canReachStand(level, stand)) continue;
            if (isBadStandForTarget(level, target, stand)) continue;

            double dist = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);

            boolean inside = site.isInsideFootprint(stand) || site.isInsideFootprint(stand.below());

            double insidePenalty = (preferOutside && inside) ? 100000.0 : 0.0;

            double smallInsidePenalty = (!isScaffold && inside) ? 250.0 : 0.0;

            double score = dist + insidePenalty + smallInsidePenalty;

            if (score < bestScore) {
                bestScore = score;
                best = stand.immutable();
            }
        }

        if (best != null) return best;

        // Budget-bypass fallback for late-stage high/edge placements.
        int forcedChecks = 0;
        final int maxForcedChecks = 24;
        for (BlockPos stand : candidates) {
            if (forcedChecks++ >= maxForcedChecks) break;
            if (isStandBlacklisted(level, stand)) continue;
            if (!isStandable(level, stand)) continue;
            if (!canReachStandNow(level, stand)) continue;
            if (isBadStandForTarget(level, target, stand)) continue;

            double dist = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);
            boolean inside = site.isInsideFootprint(stand) || site.isInsideFootprint(stand.below());
            double insidePenalty = (preferOutside && inside) ? 100000.0 : 0.0;
            double smallInsidePenalty = (!isScaffold && inside) ? 250.0 : 0.0;
            double score = dist + insidePenalty + smallInsidePenalty;

            if (score < bestScore) {
                bestScore = score;
                best = stand.immutable();
            }
        }

        return best;
    }

    public boolean isStandable(ServerLevel level, BlockPos stand) {
        BlockState feet = level.getBlockState(stand);
        BlockState head = level.getBlockState(stand.above());
        BlockPos floorPos = stand.below();
        BlockState floor = level.getBlockState(floorPos);

        if (!feet.isAir() && !feet.canBeReplaced()) return false;
        if (!head.isAir() && !head.canBeReplaced()) return false;

        if (floor.is(Blocks.SCAFFOLDING)) return true;

        return floor.isFaceSturdy(level, floorPos, Direction.UP);
    }

    @Nullable
    public BlockPos findStandPosForDig(ServerLevel level, BlockPos target) {
        List<BlockPos> candidates = findStandCandidatesForDig(level, target);
        BlockPos best = null;
        double bestDist = Double.POSITIVE_INFINITY;

        for (BlockPos stand : candidates) {
            if (!isStandable(level, stand)) continue;
            if (!canReachStand(level, stand)) continue;

            double d = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);
            if (d < bestDist) {
                bestDist = d;
                best = stand.immutable();
            }
        }

        if (best != null) return best;

        // Budget-bypass fallback for low-frequency deadlocks on the final dig cells.
        int forcedChecks = 0;
        final int maxForcedChecks = 24;
        for (BlockPos stand : candidates) {
            if (forcedChecks++ >= maxForcedChecks) break;
            if (!isStandable(level, stand)) continue;
            if (!canReachStandNow(level, stand)) continue;

            double d = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);
            if (d < bestDist) {
                bestDist = d;
                best = stand.immutable();
            }
        }

        return best;
    }

    /**
     * Emergency fallback for isolated leftover dig cells where no stand position exists
     * after terrain has been carved out. Keep this very local to avoid long-range "magic" digs.
     */
    public boolean canDirectDigFallback(ServerLevel level, BlockPos target) {
        if (target == null) return false;

        double dy = (target.getY() + 0.5) - this.getY();
        if (dy < -1.5 || dy > 7.0) return false;

        // Overhead cleanup often needs a slightly wider local fallback radius.
        boolean overheadTarget = dy >= 1.0;
        double maxHoriz = overheadTarget ? 4.0 : 2.5;
        double maxDistSq = overheadTarget ? 64.0 : 49.0;

        double dx = Math.abs((target.getX() + 0.5) - this.getX());
        double dz = Math.abs((target.getZ() + 0.5) - this.getZ());
        if (dx > maxHoriz || dz > maxHoriz) return false;

        double distSq = this.distanceToSqr(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
        return distSq <= maxDistSq; // Local fallback only used when no stand exists
    }

    public List<BlockPos> findStandCandidatesForDig(ServerLevel level, BlockPos target) {
        ArrayList<BlockPos> out = new ArrayList<>();
        boolean highTarget = target.getY() >= this.blockPosition().getY() + 2;
        int[] dyList = highTarget
                ? new int[]{-2, -1, 0, -3, 1, -4, 2, 3}
                : new int[]{0, 1, -1, 2, -2, 3, -3};
        int[] offsets = highTarget
                ? new int[]{0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5}
                : new int[]{0, 1, -1, 2, -2, 3, -3};
        double maxDistSq = highTarget ? 64.0 : 36.0;

        for (int dy : dyList) {
            BlockPos base = target.offset(0, dy, 0);

            for (int dx : offsets) {
                for (int dz : offsets) {
                    if (dx == 0 && dz == 0) continue;
                    BlockPos stand = base.offset(dx, 0, dz);
                    if (stand.distSqr(target) <= maxDistSq) {
                        out.add(stand.immutable());
                    }
                }
            }
        }
        return out;
    }

    public List<BlockPos> findStandCandidatesForPlacement(ServerLevel level, BlockPos target) {
        ArrayList<BlockPos> out = new ArrayList<>();
        boolean highTarget = target.getY() >= this.blockPosition().getY() + 2;

        int[] dyList = highTarget
                ? new int[]{0, 1, -1, 2, -2, 3, -3, 4, -4}
                : new int[]{0, 1, -1, 2, -2, 3, -3};
        int maxOffset = highTarget ? 3 : 2;
        double maxReachSq = 25.0; // keep candidate reach consistent with actual placement reach (5 blocks)

        for (int dy : dyList) {
            BlockPos base = target.offset(0, dy, 0);

            for (int dx = -maxOffset; dx <= maxOffset; dx++) {
                for (int dz = -maxOffset; dz <= maxOffset; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos stand = base.offset(dx, 0, dz);
                    if (!isStandable(level, stand)) continue;

                    // reach check should be to the placed block center, not "feetTarget"
                    double reachSqr = stand.distSqr(target);
                    if (reachSqr <= maxReachSq) {
                        out.add(stand.immutable());
                    }
                }
            }
        }

        return out;
    }

    public void markBadStand(ServerLevel level, BlockPos stand) {
        badStandUntil.put(stand.immutable(), (long) (level.getServer().getTickCount() + BAD_STAND_TTL));
    }
    public boolean isStandBlacklisted(ServerLevel level, BlockPos stand) {
        return badStandUntil.getOrDefault(stand, 0L) > level.getServer().getTickCount();
    }

    public void markBadTask(ServerLevel level, BlockPos taskPos) {
        badTaskUntil.put(taskPos.immutable(), (long) (level.getServer().getTickCount() + BAD_TASK_TTL));
    }
    public void markBadDigTask(ServerLevel level, BlockPos taskPos) {
        badTaskUntil.put(taskPos.immutable(), (long) (level.getServer().getTickCount() + BAD_DIG_TASK_TTL));
    }
    public boolean isTaskBlacklisted(ServerLevel level, BlockPos taskPos) {
        return badTaskUntil.getOrDefault(taskPos, 0L) > level.getServer().getTickCount();
    }

    public void markBadStandForTarget(BlockPos target, BlockPos stand) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        long until = serverLevel.getServer().getTickCount() + BAD_TARGET_STAND_TTL;
        perTargetBadStandsUntil
                .computeIfAbsent(target.immutable(), k -> new HashMap<>())
                .put(stand.immutable(), until);
    }

    public boolean isBadStandForTarget(ServerLevel level, BlockPos target, BlockPos stand) {
        Map<BlockPos, Long> stands = perTargetBadStandsUntil.get(target);
        if (stands == null) return false;

        long now = level.getServer().getTickCount();
        long until = stands.getOrDefault(stand, 0L);
        if (until <= now) {
            stands.remove(stand);
            if (stands.isEmpty()) perTargetBadStandsUntil.remove(target);
            return false;
        }
        return true;
    }

    public void clearBadStandsForTarget(BlockPos target) {
        perTargetBadStandsUntil.remove(target);
    }

    public boolean canReachStand(ServerLevel level, BlockPos stand) {
        return canReachStandInternal(stand, true);
    }

    public boolean canReachStandNow(ServerLevel level, BlockPos stand) {
        return canReachStandInternal(stand, false);
    }

    private void resetPathQueryBudgetIfNeeded() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        long nowTick = serverLevel.getServer().getTickCount();
        if (nowTick != pathQueryTick) {
            pathQueryTick = nowTick;
            pathQueriesThisTick = 0;
            pathReachabilityCache.clear();
        }
    }

    private boolean canReachStandInternal(BlockPos stand, boolean enforceBudget) {
        resetPathQueryBudgetIfNeeded();

        BlockPos key = stand.immutable();
        Boolean cached = pathReachabilityCache.get(key);
        if (cached != null) return cached;

        if (enforceBudget && pathQueriesThisTick >= PATH_QUERIES_BUDGET_PER_TICK) {
            return false;
        }

        pathQueriesThisTick++;
        Path path = this.getNavigation().createPath(key, 0);
        boolean ok = path != null && path.canReach();
        pathReachabilityCache.put(key, ok);
        return ok;
    }

    private void normalizeWorkTuning() {
        this.walkSpeed = Math.max(0.01D, this.walkSpeed);
        this.navigationSpeed = Math.max(0.1D, this.navigationSpeed);
        this.placeTicksPerBlock = Math.max(1, this.placeTicksPerBlock);
        this.digTicksPerBlock = Math.max(1, this.digTicksPerBlock);
        this.buildSwingTicks = Math.max(1, this.buildSwingTicks);
        this.digSwingTicks = Math.max(1, this.digSwingTicks);
        this.manualMoveSpeed = Math.max(0.05D, this.manualMoveSpeed);
        this.manualMoveStep = Math.max(0.02D, this.manualMoveStep);
    }

    private void applyMovementSpeedFromTuning() {
        var speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr == null) return;
        if (Math.abs(speedAttr.getBaseValue() - this.walkSpeed) > 1.0E-6) {
            speedAttr.setBaseValue(this.walkSpeed);
        }
    }

}
