package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.entity.ai.SCP173StalkGoal;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.research.ResearchEntry;
import net.luis.scpmod.util.SCPDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SCP173Entity extends PathfinderMob implements AnomalyEntity {

    public SCP173Entity(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_173.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-173";
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SCP173StalkGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isAnyPlayerWatching() {
        // Consider players within 20 blocks
        for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20))) {
            if (player.hasLineOfSight(this)) {
                Vec3 playerEyes = player.getEyePosition(1.0F);
                Vec3 lookDirection = player.getViewVector(1.0F);
                // Calculate vector from player to SCP-173's eyes
                Vec3 toSCP = this.position().add(0, this.getEyeHeight(), 0).subtract(playerEyes).normalize();
                double dot = lookDirection.dot(toSCP);
                // If the dot product is high, the player is looking nearly directly at SCP-173.
                if (dot > 0.7) {
                    return true;
                }
            }
        }
        return false;
    }

    public void attackPlayer(Player player) {
        if (!this.level().isClientSide()) {
            DamageSource scp173Damage = new DamageSource(
                    this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(SCPDamageTypes.SCP_173)
            );
            player.hurt(scp173Damage, player.getMaxHealth());
        }
        this.playSound(SoundEvents.BONE_BLOCK_BREAK, 1.5F, 0.8F);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.onGround() && !this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.2, this.getDeltaMovement().z);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {}

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.LAVA) ||
                source.is(DamageTypes.DROWN) || source.is(DamageTypes.CACTUS) ||
                source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0, 0.5, 0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.setMaxUpStep(1.1f);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }
}
