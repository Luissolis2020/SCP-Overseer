package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.ai.navigation.*;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.facility.construction.ConstructionState;
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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;
import java.util.*;

import static net.luis.scpmod.network.SCPModNetwork.syncPDAStateTo;

public class BuilderNPC extends PathfinderMob {

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
    private static final int MAX_CANDIDATES_SCAN = 250;

    private final Map<BlockPos, Long> badStandUntil = new HashMap<>();
    private final Map<BlockPos, Long> badTaskUntil  = new HashMap<>();

    private static final int BAD_STAND_TTL = 60; // 3s
    private static final int BAD_TASK_TTL  = 40; // 2s

    private final PathJobQueue pathJobQueue = new PathJobQueue(this);

    private int escapeCooldown = 0;
    private int placementRetryCooldown = 0;

    private final Map<BlockPos, Set<BlockPos>> perTargetBadStands = new HashMap<>();

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
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override public boolean canUse() { return !BuilderNPC.this.hasSite() && super.canUse(); }
            @Override public boolean canContinueToUse() { return !BuilderNPC.this.hasSite() && super.canContinueToUse(); }
        });
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    @Override
    public void tick() {
        super.tick();

        if (escapeCooldown > 0) escapeCooldown--;
        if (placementRetryCooldown > 0) placementRetryCooldown--;

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
                    pathJobQueue.enqueue(new PathJobDigBlock(this, target, false));
                } else {
                    if (assignedSite.hasBlocksToDig()) {
                        if (escapeCooldown <= 0) {
                            BlockPos escape = assignedSite.findExteriorStand((ServerLevel) level(), this, this.blockPosition(), 8);
                            if (escape != null) {
                                System.out.println("[BuilderNPC] No dig target found. Escaping to " + escape);
                                pathJobQueue.enqueue(new PathJobMoveToPos(this, escape));
                                escapeCooldown = 20; // 1 second
                            } else {
                                System.out.println("[BuilderNPC] No dig target and no escape stand found.");
                                escapeCooldown = 20;
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

    private @Nullable ConstructionSite.BlockPlacementTask nextPlacementTask(ServerLevel level, ConstructionSite site) {

        // 1) scaffolds FIRST (temporary support) — but also pick “best” scaffold, not first
        ConstructionSite.BlockPlacementTask bestScaffold = pickBestCandidate(level, site, site.getScaffoldLayerQueues(), true);
        if (bestScaffold != null) return bestScaffold;

        // 2) normal build tasks
        ConstructionSite.BlockPlacementTask bestBuild = pickBestCandidate(level, site, site.getBuildLayerQueues(), false);
        if (bestBuild != null) return bestBuild;

        // 3) deferred tasks (only if now placeable)
        ConstructionSite.BlockPlacementTask bestDeferred = pickBestCandidate(level, site, site.getDeferredLayerQueues(), false);
        if (bestDeferred != null) return bestDeferred;

        // 4) nothing reachable: try to plan scaffolds for some pending task
        ConstructionSite.BlockPlacementTask any = findAnyTask(level, site);
        if (any != null) {
            boolean planned = site.planScaffoldFor(level, any.worldPos());
            if (planned) {
                site.deferPlacementTask(any);
                return null;
            }
        }

        System.out.println("[BuilderNPC] No reachable placement tasks. build="
                + site.getBuildLayerQueues().stream().mapToInt(Queue::size).sum()
                + " deferred="
                + site.getDeferredLayerQueues().stream().mapToInt(Queue::size).sum()
                + " scaffold="
                + site.getScaffoldLayerQueues().stream().mapToInt(Queue::size).sum());

        // If builder is inside footprint and no reachable placement tasks, evacuate outside
        if (site.isInsideFootprint(this.blockPosition())) {
            BlockPos escape = site.findExteriorStand(level, this, this.blockPosition(), 6);
            if (escape != null) {
                System.out.println("[BuilderNPC] No reachable tasks while inside footprint. Escaping to " + escape);
                pathJobQueue.enqueue(new PathJobMoveToPos(this, escape));
            }
        }
        // If we're NOT inside footprint, still try a gentle reposition to refresh paths
        if (!site.isInsideFootprint(this.blockPosition())) {
            BlockPos anchor = site.findExteriorStand(level, this, site.getOrigin(), 8);
            if (anchor != null) {
                pathJobQueue.enqueue(new PathJobMoveToPos(this, anchor));
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

        for (var layer : layers) {
            for (var t : layer) {
                if (scanned++ > MAX_CANDIDATES_SCAN) break;

                if (level.getBlockState(t.worldPos()).equals(t.state())) continue;

                if (!isScaffoldPass && !isPlacementEligibleNow(level, site, t)) continue;

                if (isTaskBlacklisted(level, t.worldPos())) continue;

                if (site.isPlacementOnCooldown(level, t.worldPos())) continue;

                BlockPos stand = findStandPosForPlacement(level, site, t.worldPos(), isScaffoldPass);
                if (stand == null) continue;
                if (isStandBlacklisted(level, stand)) continue;

                if (this.getNavigation().createPath(stand, 0) == null) continue;

                // Score: lower Y first, then “floor-like” first, then distance
                double score = scorePlacementCandidate(level, site, t, stand, isScaffoldPass);
                if (score < bestScore) {
                    bestScore = score;
                    best = t;
                }
            }
            if (scanned > MAX_CANDIDATES_SCAN) break;
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

        return (y * 10000.0) + floorBias + insidePenalty + dist;
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


    public void assignToSite(ConstructionSite site) {
        this.assignedSite = site;
        site.assignBuilder(this);

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
    }

    public boolean isAtAssignedSite() {
        if (assignedSite == null) return false;

        BlockPos pos = this.blockPosition();
        BlockPos o = assignedSite.getOrigin();

        int bufferXZ = (currentPhase == BuildPhase.NAVIGATING_TO_SITE) ? 4 : 1;

        int w = assignedSite.getFootprintWidth();
        int d = assignedSite.getFootprintDepth();

        boolean insideXZ =
                pos.getX() >= o.getX() - bufferXZ &&
                        pos.getX() <= o.getX() + w + bufferXZ &&
                        pos.getZ() >= o.getZ() - bufferXZ &&
                        pos.getZ() <= o.getZ() + d + bufferXZ;

        // tolerate Y drift (terrain)
        boolean okY = Math.abs(pos.getY() - o.getY()) <= 6;

        return insideXZ && okY;
    }

    public BuildPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(BuildPhase phase) {
        if (this.currentPhase != phase) {
            System.out.println("[BuilderNPC] Phase changed to: " + phase);
            this.currentPhase = phase;
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
            return 6;
        } else if (this.getCurrentPhase() == BuildPhase.BUILDING) {
            return 8;
        }
        return 8;
    }

    @Override
    public float getAttackAnim(float partialTicks) {
        return Mth.lerp(partialTicks, this.oSwingProgress, this.swingProgress);
    }

    public PathJobQueue getPathJobQueue() {
        return pathJobQueue;
    }

    @Nullable
    public BlockPos findStandPosForPlacement(ServerLevel level, ConstructionSite site, BlockPos target, boolean isScaffold) {
        BlockPos best = null;
        double bestScore = Double.POSITIVE_INFINITY;

        int relY = target.getY() - site.getOrigin().getY();
        boolean preferOutside = (!isScaffold && relY >= 2);

        for (BlockPos stand : findStandCandidatesForPlacement(level, target)) {

            if (isStandBlacklisted(level, stand)) continue;
            if (!isStandable(level, stand)) continue;
            if (this.getNavigation().createPath(stand, 0) == null) continue;
            if (isBadStandForTarget(target, stand)) continue;

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
        BlockPos best = null;
        double bestDist = Double.POSITIVE_INFINITY;

        for (BlockPos stand : findStandCandidatesForDig(level, target)) {
            if (!isStandable(level, stand)) continue;
            if (this.getNavigation().createPath(stand, 0) == null) continue;

            double d = this.distanceToSqr(stand.getX() + 0.5, stand.getY(), stand.getZ() + 0.5);
            if (d < bestDist) {
                bestDist = d;
                best = stand.immutable();
            }
        }

        return best;
    }

    public List<BlockPos> findStandCandidatesForDig(ServerLevel level, BlockPos target) {
        ArrayList<BlockPos> out = new ArrayList<>();
        int[] dyList = new int[]{0, 1, -1};

        for (int dy : dyList) {
            BlockPos base = target.offset(0, dy, 0);

            // cardinals first
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                out.add(base.relative(dir));
            }

            // diagonals
            out.add(base.offset( 1, 0,  1));
            out.add(base.offset( 1, 0, -1));
            out.add(base.offset(-1, 0,  1));
            out.add(base.offset(-1, 0, -1));
        }
        return out;
    }

    public List<BlockPos> findStandCandidatesForPlacement(ServerLevel level, BlockPos target) {
        ArrayList<BlockPos> out = new ArrayList<>();

        int[] dyList = new int[]{0, 1, -1, 2, -2, 3, -3};

        for (int dy : dyList) {
            BlockPos base = target.offset(0, dy, 0);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;

                    BlockPos stand = base.offset(dx, 0, dz);
                    if (!isStandable(level, stand)) continue;

                    // reach check should be to the placed block center, not "feetTarget"
                    double reachSqr = stand.distSqr(target);
                    if (reachSqr <= 25.0) { // 5 blocks
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
    public boolean isTaskBlacklisted(ServerLevel level, BlockPos taskPos) {
        return badTaskUntil.getOrDefault(taskPos, 0L) > level.getServer().getTickCount();
    }

    public void markBadStandForTarget(BlockPos target, BlockPos stand) {
        perTargetBadStands.computeIfAbsent(target.immutable(), k -> new HashSet<>()).add(stand.immutable());
    }

    public boolean isBadStandForTarget(BlockPos target, BlockPos stand) {
        Set<BlockPos> s = perTargetBadStands.get(target);
        return s != null && s.contains(stand);
    }

}
