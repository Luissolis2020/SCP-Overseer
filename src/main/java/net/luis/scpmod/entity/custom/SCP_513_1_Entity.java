package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.util.SCPDamageTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Random;

public class SCP_513_1_Entity extends PathfinderMob implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("Idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("Walking");
    public static final RawAnimation ATTACK = RawAnimation.begin().then("Attack", Animation.LoopType.PLAY_ONCE);

    private static final EntityDataAccessor<Boolean> VISIBLE = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DISAPPEAR_TIMER = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TELEPORT_COOLDOWN = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_REAL = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TIMER = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STARE_TIMER = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> PHASE1_TRIGGERED = SynchedEntityData.defineId(SCP_513_1_Entity.class, EntityDataSerializers.BOOLEAN);

    public SCP_513_1_Entity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(false);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_513_1.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-513-1";
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VISIBLE, false);
        this.entityData.define(DISAPPEAR_TIMER, 0);
        this.entityData.define(TELEPORT_COOLDOWN, 100);
        this.entityData.define(IS_REAL, false);
        this.entityData.define(ATTACK_TIMER, 0);
        this.entityData.define(STARE_TIMER, 0);
        this.entityData.define(PHASE1_TRIGGERED, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (areAllCursedPlayersDead()) {
            this.discard();
            return;
        }

        Player closestPlayer = findClosestCursedPlayer();

        if (closestPlayer != null) {

            this.getLookControl().setLookAt(closestPlayer, 180.0F, 180.0F);
            int curseTime = CursedPlayer.getCurseTimer(closestPlayer);

            if (canPlayerSeeMe(closestPlayer) && !this.isReal()) {
                this.setInvisible(true);
                setDisappearTimer(40);
            } else {
                if (getDisappearTimer() > 0) {
                    setDisappearTimer(getDisappearTimer() - 1);
                } else {
                    this.setInvisible(false);
                }
            }

            // Handle teleport cooldown
            if (getTeleportCooldown() > 0) {
                setTeleportCooldown(getTeleportCooldown() - 1);
            }

            if (getStareTimer() > 0) {
                setStareTimer(getStareTimer() - 1);

                if (getStareTimer() == 0) {
                    this.swing(InteractionHand.MAIN_HAND);
                    this.triggerAnim("attack_controller", "Attack");
                    setAttackTimer(12);
                }
            }

            if (getAttackTimer() > 0) {
                setAttackTimer(getAttackTimer() - 1);

                if (getAttackTimer() == 0) {
                    DamageSource scp513Damage = new DamageSource(this.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(SCPDamageTypes.SCP_513_STRANGLE));

                    closestPlayer.hurt(scp513Damage, 9999.0F);
                }
            }

            if (this.isReal() && this.distanceTo(closestPlayer) < 5.0F && this.random.nextInt(100) < 5 && !(curseTime >= 4800)) {
                closestPlayer.playSound(SoundEvents.WARDEN_NEARBY_CLOSE, 0.5F, 0.5F);
            }

            // Phase 1: No entity appears, only sound effects
            if (curseTime >= 300 && curseTime <= 360 && !this.entityData.get(PHASE1_TRIGGERED)) {
                this.entityData.set(PHASE1_TRIGGERED, true);

                closestPlayer.level().playLocalSound(
                        closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(),
                        SoundEvents.AMBIENT_CAVE.get(), SoundSource.PLAYERS,
                        1.0F, 1.0F, true
                );

                closestPlayer.displayClientMessage(Component.literal("You feel like something is watching you..."), true);
            }

            // Phase 2: Appear in the far distance
            if (curseTime < 1800 && getTeleportCooldown() == 0) {
                teleportInDistance(closestPlayer, 40, 60);
                setTeleportCooldown(100);
                return;
            }

            if (curseTime >= 2000 && this.random.nextInt(100) < 3) {
                closestPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40, 0, false, false));
                closestPlayer.level().playSound(closestPlayer, closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(),
                        SoundEvents.WARDEN_NEARBY_CLOSE, SoundSource.PLAYERS, 1.5F, 0.7F);
            }

            if (!this.isReal() && (curseTime >= 3000 && curseTime <= 3500)) {
                chargeAtPlayer(closestPlayer, this);
            }

            if ((curseTime >= 3000 && curseTime <= 4000) && this.random.nextInt(100) < 5) {
                closestPlayer.level().playSound(null, closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(),
                        SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.5F);
            }

            // Phase 3: Appear closer but still unpredictable
            if (curseTime < 3600 && getTeleportCooldown() == 0) {
                teleportInDistance(closestPlayer, 20, 30);
                setTeleportCooldown(80);
                return;
            }

            if (curseTime >= 3600 && closestPlayer.isSleeping() && this.isReal()) {
                teleportNearBed(closestPlayer);
                this.level().playSound(null, closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(),
                        SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.5F, 0.7F);
                closestPlayer.displayClientMessage(Component.literal("Your chest tightens... you can't breathe..."), true);
                DamageSource scp513Damage = new DamageSource(this.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(SCPDamageTypes.SCP_513_STRANGLE));
                closestPlayer.hurt(scp513Damage, 4.0F);
                closestPlayer.stopSleeping();

                setTeleportCooldown(80);
            }

            if (curseTime >= 3800 && curseTime < 4500 && this.isReal()) {
                if (getTeleportCooldown() == 0) {
                    teleportDirectlyBehind(closestPlayer);
                    setTeleportCooldown(100);
                }
                if (this.isReal() && canPlayerSeeMe(closestPlayer)) {
                    this.setInvisible(true);
                }
            }

            if (curseTime >= 4000) {
                closestPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
                closestPlayer.level().playLocalSound(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(),
                        SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.5F, 0.5F, true);
            }

            if (curseTime >= 4400 && this.random.nextInt(100) < 3) {
                closestPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false));
            }

            if (curseTime >= 4800 && this.isReal()) {
                if (getTeleportCooldown() == 0) {
                    this.setInvisible(false);
                    teleportDirectlyBehind(closestPlayer);
                    this.facePlayer(closestPlayer);
                    forcePlayerToLook(closestPlayer, this);
                    closestPlayer.lookAt(EntityAnchorArgument.Anchor.EYES, this.position());
                    attackPlayer(closestPlayer);
                    setTeleportCooldown(80);
                }
            }
        }
    }

    private Player findClosestCursedPlayer() {
        List<Player> players = this.level().getEntitiesOfClass(Player.class, new AABB(this.getX() - 30, this.getY() - 10, this.getZ() - 30, this.getX() + 30, this.getY() + 10, this.getZ() + 30));
        for (Player player : players) {
            if (CursedPlayer.isCursed(player)) {
                return player;
            }
        }
        return null;
    }

    private void attackPlayer(Player player) {
        if (!this.isReal() || getStareTimer() > 0) {
            return;
        }
        player.displayClientMessage(Component.literal("You feel an overwhelming force tighten around your throat..."), true);

        setStareTimer(40);
        forcePlayerToLook(player, this);

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3, false, false));

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_DEATH, SoundSource.HOSTILE, 2.0F, 0.5F);

        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(),
                SoundEvents.WARDEN_ROAR, SoundSource.PLAYERS, 1.5F, 0.8F, true);

    }

    private boolean canPlayerSeeMe(Player player) {
        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 toEntity = this.position().subtract(player.position()).normalize();

        double dotProduct = lookVec.dot(toEntity);

        if (dotProduct <= 0.995) {
            return false;
        }

        Vec3 playerEyePos = player.getEyePosition();
        Vec3 scpPos = this.position().add(0, this.getEyeHeight(), 0);

        BlockHitResult hitResult = this.level().clip(new ClipContext(
                playerEyePos, scpPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        return hitResult.getType() == HitResult.Type.MISS;
    }

    private void chargeAtPlayer(Player player, SCP_513_1_Entity entity) {
        Vec3 direction = player.position().subtract(this.position()).normalize();
        Vec3 movement = direction.scale(0.5);

        entity.setDeltaMovement(movement);

        entity.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.WARDEN_STEP, SoundSource.HOSTILE, 1.0F, 1.0F);

        entity.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.PHANTOM_FLAP, SoundSource.HOSTILE, 1.0F, 1.3F);

        if (entity.distanceTo(player) < 2.0) {
            entity.remove(RemovalReason.DISCARDED);
        }
    }

    private void forcePlayerToLook(Player player, SCP_513_1_Entity target) {
        Vec3 direction = target.position().subtract(player.position()).normalize();

        float yaw = (float) (Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90F);
        float pitch = (float) (Math.toDegrees(-Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z))));

        player.setXRot(pitch);
        player.setYRot(yaw);
        player.setYHeadRot(yaw);
        player.setYBodyRot(yaw);
        player.xRotO = pitch;
        player.yRotO = yaw;

        player.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
    }


    private boolean areAllCursedPlayersDead() {
        List<? extends Player> allPlayers = this.level().players();
        for (Player player : allPlayers) {
            if (CursedPlayer.isCursed(player) && !player.isDeadOrDying()) {
                return false;
            }
        }
        return true;
    }

    private void teleportInDistance(Player player, int minDistance, int maxDistance) {
        BlockPos safePos = findSafeSpawnPos(player.blockPosition(), minDistance, maxDistance);
        this.moveTo(safePos.getX(), safePos.getY(), safePos.getZ());

        facePlayer(player);
    }

    private void teleportDirectlyBehind(Player player) {
        Vec3 lookVec = player.getLookAngle().normalize();
        BlockPos targetPos = player.blockPosition().offset(-(int) Math.round(lookVec.x * 2), 0, -(int) Math.round(lookVec.z * 2));

        int playerY = player.blockPosition().getY();
        int bestY = findNearestSafeY(targetPos, playerY);

        this.moveTo(targetPos.getX(), bestY, targetPos.getZ());
        facePlayer(player);
    }

    private BlockPos findSafeSpawnPos(BlockPos origin, int minDistance, int maxDistance) {
        Random random = new Random();

        for (int attempts = 10; attempts > 0; attempts--) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = minDistance + random.nextInt(maxDistance - minDistance);

            int xOffset = (int) (Math.cos(angle) * distance);
            int zOffset = (int) (Math.sin(angle) * distance);

            BlockPos potentialPos = origin.offset(xOffset, 0, zOffset);
            int groundY = this.level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, potentialPos).getY();
            BlockPos finalPos = new BlockPos(potentialPos.getX(), groundY, potentialPos.getZ());

            if (this.level().getBlockState(finalPos).isAir() && this.level().getBlockState(finalPos.above()).isAir()) {
                return finalPos;
            }
        }
        return origin;
    }

    private int findNearestSafeY(BlockPos targetPos, int playerY) {
        int yRange = 3;

        for (int yOffset = 0; yOffset <= yRange; yOffset++) {
            BlockPos checkAbove = targetPos.above(yOffset);
            BlockPos checkBelow = targetPos.below(yOffset);

            if (this.level().getBlockState(checkAbove).isAir() && this.level().getBlockState(checkAbove.above()).isAir()) {
                return checkAbove.getY();
            }

            if (this.level().getBlockState(checkBelow).isAir() && this.level().getBlockState(checkBelow.above()).isAir()) {
                return checkBelow.getY();
            }
        }

        return playerY;
    }

    private void teleportNearBed(Player player) {
        BlockPos bedPos = player.blockPosition().offset(0, 0, -2);
        BlockPos safePos = findSafeSpawnPos(bedPos, 0, 3);

        this.moveTo(safePos.getX(), safePos.getY(), safePos.getZ());
    }


    public void facePlayer(Player player) {
        Vec3 direction = player.position().subtract(this.position()).normalize();

        double yaw = Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90;
        double pitch = Math.toDegrees(-Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)));

        this.setYRot((float) yaw);
        this.setXRot((float) pitch);
        this.setYHeadRot((float) yaw);
        this.setYBodyRot((float) yaw);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "attack_controller", 3, this::attackPredicate));
    }

    private <E extends SCP_513_1_Entity & GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        double speed = this.getDeltaMovement().horizontalDistanceSqr();

        if (speed > 0.0001) {
            return event.setAndContinue(WALK);
        } else {
            return event.setAndContinue(IDLE);
        }
    }

    private <E extends SCP_513_1_Entity & GeoAnimatable> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging) {
            return event.setAndContinue(ATTACK);
        }
        return PlayState.STOP;
    }


    public boolean isReal() {
        return this.entityData.get(IS_REAL);
    }

    public void setReal(boolean real) {
        this.entityData.set(IS_REAL, real);
    }

    public int getDisappearTimer() {
        return this.entityData.get(DISAPPEAR_TIMER);
    }

    public int getTeleportCooldown() {
        return this.entityData.get(TELEPORT_COOLDOWN);
    }

    public void setDisappearTimer(int value) {
        this.entityData.set(DISAPPEAR_TIMER, value);
    }

    public void setTeleportCooldown(int value) {
        this.entityData.set(TELEPORT_COOLDOWN, value);
    }

    public int getAttackTimer() {
        return this.entityData.get(ATTACK_TIMER);
    }

    public void setAttackTimer(int value) {
        this.entityData.set(ATTACK_TIMER, value);
    }

    public int getStareTimer() {
        return this.entityData.get(STARE_TIMER);
    }

    public void setStareTimer(int value) {
        this.entityData.set(STARE_TIMER, value);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Player attacker = null;

        if (source.getEntity() instanceof Player) {
            attacker = (Player) source.getEntity();
        }

        if (attacker != null) {
            return false;
        }

        if(source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypeTags.IS_DROWNING) || source.is(DamageTypeTags.IS_FREEZING)  || source.is(DamageTypeTags.IS_PROJECTILE)  || source.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
