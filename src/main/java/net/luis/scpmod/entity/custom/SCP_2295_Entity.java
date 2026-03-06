package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.entity.ai.SCP_2295_HealGoal;
import net.luis.scpmod.research.ModResearchEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SCP_2295_Entity extends PathfinderMob implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().then("idle(sit)", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation COME_HERE = RawAnimation.begin().then("come_here", Animation.LoopType.HOLD_ON_LAST_FRAME);
    public static final RawAnimation HEAL = RawAnimation.begin().then("heal", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation CRY = RawAnimation.begin().then("cry", Animation.LoopType.PLAY_ONCE);

    private static final EntityDataAccessor<Boolean> HEALING = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WAITING = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SAD = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HEAL_ANIMATION_TIME = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COME_HERE_ANIMATION_TIME = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SAD_ANIMATION_TIME = SynchedEntityData.defineId(SCP_2295_Entity.class, EntityDataSerializers.INT);

    public final Map<UUID, Integer> cryingCooldowns = new HashMap<>();
    private boolean hasStoredOriginalPosition = false;

    public Player healingTarget = null;
    public BlockPos bedPos = null;
    private BlockPos originalPosition = null;

    public SCP_2295_Entity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_2295.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-2295";
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEALING, false);
        this.entityData.define(SAD, false);
        this.entityData.define(WAITING, false);
        this.entityData.define(HEAL_ANIMATION_TIME, 0);
        this.entityData.define(COME_HERE_ANIMATION_TIME, 0);
        this.entityData.define(SAD_ANIMATION_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SCP_2295_HealGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!hasStoredOriginalPosition && !this.level().isClientSide) {
            this.originalPosition = this.blockPosition();
            hasStoredOriginalPosition = true;
        }
    }

    public void setHealing(Player player, BlockPos bed) {
        this.healingTarget = player;
        this.bedPos = bed;
        this.entityData.set(HEALING, false);
    }

    public void setSad(boolean sad) {
        this.entityData.set(SAD, sad);
    }

    public boolean isSad() {
        return this.entityData.get(SAD);
    }

    public boolean isHealing() {
        return this.entityData.get(HEALING);
    }

    public void setHealingState(boolean healing) {
        this.entityData.set(HEALING, healing);
    }

    public boolean isWaiting() {
        return this.entityData.get(WAITING);
    }

    public void setWaiting(boolean waiting) {
        this.entityData.set(WAITING, waiting);
    }

    public BlockPos getOriginalPosition() {
        return this.originalPosition;
    }

    public int getHealAnimationTime() {
        return this.entityData.get(HEAL_ANIMATION_TIME);
    }

    public void setHealAnimationTime(int time) {
        this.entityData.set(HEAL_ANIMATION_TIME, time);
    }

    public int getComeHereAnimationTime() {
        return this.entityData.get(COME_HERE_ANIMATION_TIME);
    }

    public void setComeHereAnimationTime(int time) {
        this.entityData.set(COME_HERE_ANIMATION_TIME, time);
    }

    public int getSadAnimationTime() {
        return this.entityData.get(SAD_ANIMATION_TIME);
    }

    public void setSadAnimationTime(int time) {
        this.entityData.set(SAD_ANIMATION_TIME, time);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <E extends SCP_2295_Entity & GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        double speed = this.getDeltaMovement().horizontalDistanceSqr();

        if (isWaiting()) {
            return event.setAndContinue(COME_HERE);
        }

        if (isHealing()) {
            return event.setAndContinue(HEAL);
        }

        if (isSad()) {
            return event.setAndContinue(CRY);
        }

        if (speed > 0.000001) {
            return event.setAndContinue(WALK);
        }

        return event.setAndContinue(IDLE);
    }

    public void tickTimers() {
        if (getHealAnimationTime() > 0) {
            setHealAnimationTime(getHealAnimationTime() - 1);
        }
        if (getComeHereAnimationTime() > 0) {
            setComeHereAnimationTime(getComeHereAnimationTime() - 1);
        }

        if (getSadAnimationTime() > 0) {
            setSadAnimationTime(getSadAnimationTime() - 1);
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        tickTimers();

        if (getSadAnimationTime() == 0 && isSad()) {
            setSad(false);
        }
    }

    public void tickCooldowns() {
        cryingCooldowns.entrySet().removeIf(entry -> {
            int newTime = entry.getValue() - 1;
            if (newTime <= 0) {
                return true;
            }
            entry.setValue(newTime);
            return false;
        });
    }

    public void addCryingCooldown(Player player) {
        cryingCooldowns.put(player.getUUID(), 600);
    }

    public boolean isPlayerOnCooldown(Player player) {
        return cryingCooldowns.containsKey(player.getUUID());
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
