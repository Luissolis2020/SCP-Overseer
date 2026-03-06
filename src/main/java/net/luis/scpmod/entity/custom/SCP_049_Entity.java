package net.luis.scpmod.entity.custom;

import com.eliotlash.mclib.utils.MathHelper;
import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.research.ResearchManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class SCP_049_Entity extends Monster implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation ATTACK = RawAnimation.begin().then("hand_on_shoulder(attack)", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation REANIMATION = RawAnimation.begin().then("reanimation", Animation.LoopType.PLAY_ONCE);

    private boolean isReanimating = false;
    private int reanimationTimer = 0;
    private int attackAnimCooldown = 0;

    @Nullable
    private LivingEntity reanimationTarget = null;
    private LivingEntity pendingKillTarget = null;
    private int pendingKillTimer = 0;
    private int attackToReanimateDelay = 0;

    public SCP_049_Entity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_049.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-049";
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 12.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (attackAnimCooldown <= 0 && target instanceof Villager villager) {
            this.triggerAnim("procedureController", "attack");
            attackAnimCooldown = 35;

            pendingKillTarget = villager;
            pendingKillTimer = 25;
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (attackAnimCooldown > 0) {
            attackAnimCooldown--;
        }

        if (pendingKillTimer > 0) {
            pendingKillTimer--;

            if (pendingKillTimer == 0 && pendingKillTarget != null && pendingKillTarget.isAlive()) {
                pendingKillTarget.hurt(this.damageSources().mobAttack(this), Float.MAX_VALUE);

                this.reanimationTarget = pendingKillTarget;
                this.attackToReanimateDelay = 5;
                pendingKillTarget = null;
            }
        }

        if (attackToReanimateDelay > 0) {
            attackToReanimateDelay--;

            if (attackToReanimateDelay == 0 && reanimationTarget != null) {
                this.faceTarget(reanimationTarget);

                isReanimating = true;
                reanimationTimer = 55; // 2.75 seconds
                this.triggerAnim("procedureController", "reanimation");
            }
        }

        if (isReanimating) {
            reanimationTimer--;

            if (reanimationTimer <= 0 && reanimationTarget != null) {
                if (!level().isClientSide) {
                    Zombie zombie = EntityType.ZOMBIE.create(level());
                    if (zombie != null) {
                        zombie.moveTo(reanimationTarget.getX(), reanimationTarget.getY(), reanimationTarget.getZ(),
                                reanimationTarget.getYRot(), reanimationTarget.getXRot());
                        zombie.setCustomName(Component.literal("SCP-049-2"));
                        level().addFreshEntity(zombie);
                    }
                }

                reanimationTarget = null;
                isReanimating = false;
            }

            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            this.getNavigation().stop();
            this.faceTarget(reanimationTarget);
        }
    }

    private void faceTarget(Entity target) {
        if (target == null) return;
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float yaw = (float)(Mth.wrapDegrees(Math.toDegrees(Math.atan2(dz, dx))) - 90);
        this.setYRot(yaw);
        this.setYHeadRot(yaw);
        this.yBodyRot = yaw;
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || isReanimating;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "procedureController", 0, this::procedurePredicate)
                .triggerableAnim("attack", ATTACK)
                .triggerableAnim("reanimation", REANIMATION));
    }

    private <E extends SCP_049_Entity & GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        double speed = this.getDeltaMovement().horizontalDistanceSqr();

        if (speed > 0.000001) {
            return event.setAndContinue(WALK);
        }

        return event.setAndContinue(IDLE);
    }

    private <E extends SCP_049_Entity & GeoAnimatable> PlayState procedurePredicate(AnimationState<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
