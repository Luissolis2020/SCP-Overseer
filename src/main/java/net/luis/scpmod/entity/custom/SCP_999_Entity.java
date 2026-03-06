package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SCP_999_Entity extends PathfinderMob implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> TICKLE_TICKS =
            SynchedEntityData.defineId(SCP_999_Entity.class, EntityDataSerializers.INT);

    private static final int TICKLE_DURATION = 40;

    public SCP_999_Entity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TICKLE_TICKS, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 12.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public ResourceLocation getResearchId() {
        return null;
    }

    @Override
    public String getAnomalyDisplayName() {
        return "";
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.isSpectator()) {
            startTickle();
            if (!level().isClientSide) {
                this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 0.8f, 1.25f);
                // a couple hearts for vibes
                for (int i = 0; i < 5; i++)
                    level().addParticle(net.minecraft.core.particles.ParticleTypes.HEART,
                            getX() + (random.nextDouble()-0.5)*0.6,
                            getY() + 0.8 + random.nextDouble()*0.4,
                            getZ() + (random.nextDouble()-0.5)*0.6,
                            0, 0.02, 0);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public void startTickle() {
        // refresh/stack a bit
        setTickleTicks(Math.min(getTickleTicks() + 20, TICKLE_DURATION));
    }

    public int getTickleTicks() { return this.entityData.get(TICKLE_TICKS); }
    public void setTickleTicks(int v) { this.entityData.set(TICKLE_TICKS, v); }

    @Override
    public void tick() {
        super.tick();
        int t = getTickleTicks();
        if (t > 0) setTickleTicks(t - 1);
    }

    /** Alpha boost for the translucent shell pass (0.0–1.0). */
    public float getShellAlpha() {
        // base 0.70, add up to +0.25 while tickled, with a tiny pulse
        float k = getTickleTicks() / (float) TICKLE_DURATION; // 0..1
        float pulse = (float)(0.04 * Math.sin((tickCount + 0.0) * 0.6));
        return 0.70f + 0.25f * k + pulse;
    }

    // ---------- GeckoLib animation ----------
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> {
            if (getTickleTicks() > 0) {
                state.setAndContinue(RawAnimation.begin().thenLoop("wiggle"));
            } else {
                state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
            }
            return PlayState.CONTINUE; // <- with the import above
        }));
    }
}
