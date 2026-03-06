package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.pocket.PocketDimensionData;
import net.luis.scpmod.pocket.PocketMazeGenerator;
import net.luis.scpmod.pocket.PocketPrefabLibrary;
import net.luis.scpmod.pocket.PocketReturnData;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.worldgen.dimension.ModDimensions;
import net.luis.scpmod.worldgen.portal.SCPInstantTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class SCP_106_Entity extends Monster implements GeoEntity, AnomalyEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE_STAND = RawAnimation.begin().thenLoop("idle_stand");
    public static final RawAnimation IDLE_SIT = RawAnimation.begin().thenLoop("idle_sit");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation HELLO = RawAnimation.begin().then("hello", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation GOODBYE = RawAnimation.begin().then("goodbye", Animation.LoopType.PLAY_ONCE);

    private static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_VANISHING = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_EMERGING = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_STALKING = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STALK_TIMER = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_IN_TRANSITION = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TRANSITION_TIMER = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ATTACK_COOLDOWN_TICKS = SynchedEntityData.defineId(SCP_106_Entity.class, EntityDataSerializers.INT);

    private UUID teleportTarget = null;
    private int repathCooldown = 0;

    public SCP_106_Entity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_106.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-106";
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SITTING, false);
        this.entityData.define(IS_VANISHING, false);
        this.entityData.define(IS_EMERGING, false);
        this.entityData.define(IS_STALKING, false);
        this.entityData.define(STALK_TIMER, 0);
        this.entityData.define(IS_IN_TRANSITION, false);
        this.entityData.define(TRANSITION_TIMER, 0);
        this.entityData.define(ATTACK_COOLDOWN_TICKS, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "main_controller", 5, this::mainPredicate));
        controllerRegistrar.add(new AnimationController<>(this, "transition_controller", 0, this::transitionPredicate));
    }

    private <E extends SCP_106_Entity & GeoAnimatable> PlayState mainPredicate(final AnimationState<E> event) {
        if (this.isSitting()) return event.setAndContinue(IDLE_SIT);

        boolean isMoving = event.isMoving() || this.getDeltaMovement().horizontalDistanceSqr() > 0.0001;
        boolean hasMovementGoal = this.getNavigation().isInProgress();

        if (isMoving || hasMovementGoal) {
            return event.setAndContinue(WALK);
        }

        return event.setAndContinue(IDLE_STAND);
    }

    private <E extends SCP_106_Entity & GeoAnimatable> PlayState transitionPredicate(final AnimationState<E> event) {
        if (this.isEmerging()) {
            return event.setAndContinue(HELLO);
        }
        if (this.isVanishing()) {
            return event.setAndContinue(GOODBYE);
        }
        return PlayState.STOP;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.getAttackCooldownTicks() > 0) {
                this.setAttackCooldownTicks(this.getAttackCooldownTicks() - 1);
            }
            if (this.isInTransition()) {
                int timer = this.getTransitionTimer() - 1;
                this.setTransitionTimer(timer);
                this.setDeltaMovement(Vec3.ZERO);
                this.getNavigation().stop();

                if (timer <= 0) {
                    if (this.isVanishing()) {
                        if (!this.level().isClientSide && this.teleportTarget != null) {
                            Player target = this.level().getPlayerByUUID(this.teleportTarget);
                            if (target != null) {
                                sendPlayerToPocketDimension(target);
                            }
                            this.teleportTarget = null; // Clear it after use
                        }

                        Player target = this.level().getNearestPlayer(this, 64.0);
                        if (target != null) {
                            BlockPos spawnPos = findSafeSpawnNear(target.blockPosition(), 4, 10);
                            this.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                        }
                        this.setVanishing(false);
                        this.playHelloAnimation();
                        return;
                    }

                    this.setInTransition(false);
                    this.setEmerging(false);
                }

                return;
            }
            if (repathCooldown > 0) repathCooldown--;
            Player closest = this.level().getNearestPlayer(this, 16.0);
            if (!this.isStalking() && closest != null && this.hasLineOfSight(closest) && !closest.isCreative()) {
                this.setStalking(true);
                this.setTarget(closest);
                this.setStalkTimer(200);
                this.repathCooldown = 0;
            }

            if (this.isStalking() && this.getTarget() instanceof Player target) {
                double distSq = this.distanceToSqr(target);
                boolean lostLOS = !this.hasLineOfSight(target);

                if (distSq > 196 || lostLOS) {
                    this.playGoodbyeAnimation();
                    this.setStalking(false);
                    this.setTarget(null);
                    return;
                }

                // repath every ~10 ticks
                if (repathCooldown <= 0) {
                    this.getNavigation().moveTo(target, 1.1);
                    repathCooldown = 10;
                }

                this.setStalkTimer(this.getStalkTimer() - 1);
                if (this.getStalkTimer() <= 0 || !target.isAlive()) {
                    this.setStalking(false);
                    this.setTarget(null);
                }
            }
        }
    }

    private BlockPos findSafeSpawnNear(BlockPos center, int minDist, int maxDist) {
        for (int i = 0; i < 10; i++) {
            int dx = this.random.nextInt(maxDist * 2 + 1) - maxDist;
            int dz = this.random.nextInt(maxDist * 2 + 1) - maxDist;
            if (Math.abs(dx) < minDist && Math.abs(dz) < minDist) continue;

            BlockPos pos = center.offset(dx, 0, dz);
            int y = this.level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY();
            pos = new BlockPos(pos.getX(), y, pos.getZ());

            if (this.level().getBlockState(pos).isAir() && this.level().getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        return center;
    }

    public void sendPlayerToPocketDimension(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ServerLevel pocketWorld = serverPlayer.server.getLevel(ModDimensions.POCKET_DIM_LEVEL);
        if (pocketWorld == null) return;

        PocketReturnData.setReturn(
                serverPlayer,
                serverPlayer.level().dimension(),
                serverPlayer.blockPosition(),
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
        );

        PocketReturnData.beginPocketSession(serverPlayer);

        // 1) Ensure prefabs.json -> templates are loaded
        PocketPrefabLibrary.ensureLoaded(serverPlayer.server);

        // 2) Generate once
        PocketDimensionData data = PocketDimensionData.get(pocketWorld);
        if (!data.generated) {
            BlockPos mazeOrigin = new BlockPos(0, 64, 0);

            PocketMazeGenerator gen = new PocketMazeGenerator(
                    650,     // maxPieces
                    0.25f,  // trapChance
                    0.03f,  // exitChance
                    220      // exitAfterCount
            );

            gen.generate(pocketWorld, mazeOrigin, pocketWorld.random);

            // Save a spawn point inside the start room
            data.startSpawn = PocketPrefabLibrary.getDefaultStartSpawn(mazeOrigin);

            data.generated = true;
            data.setDirty();

            System.out.println("[SCPMod] Pocket maze generated at " + mazeOrigin + ", spawn=" + data.startSpawn);
        }

        // 3) Teleport to saved spawn
        BlockPos targetPos = data.startSpawn;

        PortalInfo teleportTarget = new PortalInfo(
                new Vec3(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5),
                Vec3.ZERO,
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
        );

        serverPlayer.changeDimension(pocketWorld, new SCPInstantTeleporter(teleportTarget));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof Player player && !this.isInTransition() && this.getAttackCooldownTicks() <= 0) {
            this.playGoodbyeAnimation();
            this.setStalking(false);
            this.setTarget(null);
            this.setAttackCooldownTicks(100);

            if (!this.level().isClientSide) {
                this.teleportTarget = player.getUUID(); // Store UUID instead of direct player reference
            }

            return true;
        }
        return super.doHurtTarget(target);
    }


    @Override
    public boolean canAttackType(EntityType<?> type) {
        return this.getAttackCooldownTicks() <= 0 && super.canAttackType(type);
    }

    public boolean isSitting() {
        return this.entityData.get(IS_SITTING);
    }

    public void setSitting(boolean value) {
        this.entityData.set(IS_SITTING, value);
    }

    public boolean isVanishing() {
        return this.entityData.get(IS_VANISHING);
    }

    public void setVanishing(boolean value) {
        this.entityData.set(IS_VANISHING, value);
    }

    public boolean isEmerging() {
        return this.entityData.get(IS_EMERGING);
    }

    public void setEmerging(boolean value) {
        this.entityData.set(IS_EMERGING, value);
    }

    public boolean isStalking() {
        return this.entityData.get(IS_STALKING);
    }

    public void setStalking(boolean value) {
        this.entityData.set(IS_STALKING, value);
    }

    public int getStalkTimer() {
        return this.entityData.get(STALK_TIMER);
    }

    public void setStalkTimer(int value) {
        this.entityData.set(STALK_TIMER, value);
    }

    public boolean isInTransition() {
        return this.entityData.get(IS_IN_TRANSITION);
    }

    public void setInTransition(boolean value) {
        this.entityData.set(IS_IN_TRANSITION, value);
    }

    public int getTransitionTimer() {
        return this.entityData.get(TRANSITION_TIMER);
    }

    public void setTransitionTimer(int value) {
        this.entityData.set(TRANSITION_TIMER, value);
    }

    public int getAttackCooldownTicks() {
        return this.entityData.get(ATTACK_COOLDOWN_TICKS);
    }

    public void setAttackCooldownTicks(int ticks) {
        this.entityData.set(ATTACK_COOLDOWN_TICKS, ticks);
    }

    public void playHelloAnimation() {
        this.setVanishing(false);
        this.setEmerging(true);
        this.setInTransition(true);
        this.setTransitionTimer(120);
        this.setAttackCooldownTicks(40);
    }

    public void playGoodbyeAnimation() {
        this.setEmerging(false);
        this.setVanishing(true);
        this.setInTransition(true);
        this.setTransitionTimer(80);
    }


//    @Override
//    public InteractionResult interactAt(Player player, Vec3 hitVec, InteractionHand hand) {
//        if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
//            if (!this.isEmerging() && !this.isVanishing()) {
//                if (this.isSitting()) {
//                    this.setSitting(false);
//                    this.playHelloAnimation();
//                    player.sendSystemMessage(Component.literal("SCP-106 emerging..."));
//                } else {
//                    this.playGoodbyeAnimation();
//                    this.setSitting(true);
//                    player.sendSystemMessage(Component.literal("SCP-106 vanishing..."));
//                }
//            }
//            return InteractionResult.SUCCESS;
//        }
//        return super.interactAt(player, hitVec, hand);
//    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInTransition()) return false;

        if (source.is(DamageTypeTags.IS_FIRE) ||
                source.is(DamageTypeTags.IS_FALL) ||
                source.is(DamageTypeTags.IS_DROWNING) ||
                source.is(DamageTypeTags.IS_FREEZING)) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override public boolean isOnFire() { return false; }
    @Override public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) { return false; }
    @Override public boolean isPushedByFluid() { return false; }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
