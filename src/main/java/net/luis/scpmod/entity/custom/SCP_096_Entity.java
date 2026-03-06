package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.entity.ai.SCP_096_AttackGoal;
import net.luis.scpmod.entity.ai.navigation.SCP096Navigation;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.research.ResearchManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SCP_096_Entity extends Monster implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation SAD_IDLE = RawAnimation.begin().thenLoop("sad_idle");
    public static final RawAnimation SAD_WALK = RawAnimation.begin().thenLoop("sad_walk");
    public static final RawAnimation GETTING_MAD = RawAnimation.begin().then("getting_mad", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation MAD_RUN = RawAnimation.begin().thenLoop("mad_run");
    public static final RawAnimation MAD_ATTACK = RawAnimation.begin().then("mad_attack", Animation.LoopType.PLAY_ONCE);

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(SCP_096_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(SCP_096_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANGRY_COOLDOWN = SynchedEntityData.defineId(SCP_096_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PATH_RECALCULATION_TIMER = SynchedEntityData.defineId(SCP_096_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STUCK_TIMER = SynchedEntityData.defineId(SCP_096_Entity.class, EntityDataSerializers.INT);

    private int digCooldown = 0;

    public final Set<UUID> targets = new HashSet<>();

    public SCP_096_Entity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.5f);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_096.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-096";
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SCP096Navigation(this, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGRY, false);
        this.entityData.define(TRIGGERED, false);
        this.entityData.define(ANGRY_COOLDOWN, 0);
        this.entityData.define(PATH_RECALCULATION_TIMER, 0);
        this.entityData.define(STUCK_TIMER, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(Attributes.FOLLOW_RANGE, 10000.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SCP_096_AttackGoal(this, 2.5f, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5));
    }

    public int getPathRecalculationTimer() {
        return this.entityData.get(PATH_RECALCULATION_TIMER);
    }

    public void setPathRecalculationTimer(int value) {
        this.entityData.set(PATH_RECALCULATION_TIMER, value);
    }

    public int getStuckTimer() {
        return this.entityData.get(STUCK_TIMER);
    }

    public void setStuckTimer(int value) {
        this.entityData.set(STUCK_TIMER, value);
    }


    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.entityData.set(ANGRY, angry);
    }

    public boolean isTriggered() {
        return this.entityData.get(TRIGGERED);
    }

    public void setTriggered(boolean triggered) {
        this.entityData.set(TRIGGERED, triggered);
    }

    public int getAngryCooldown() {
        return this.entityData.get(ANGRY_COOLDOWN);
    }

    public void setAngryCooldown(int cooldown) {
        this.entityData.set(ANGRY_COOLDOWN, cooldown);
    }

    private boolean isPlayerLookingAtMe(Player player) {
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

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {

            if (this.getTarget() == null && !this.targets.isEmpty()) {
                Player closest = getClosestTarget();
                if (closest != null && closest.isAlive()) {
                    this.setTarget(closest);
                    //System.out.println("Reacquired target: " + closest.getName().getString());
                }
            }

            if (digCooldown > 0) digCooldown--;

            if (!isAngry() && !isTriggered()) {
                for (Player player : this.level().players()) {
                    if (!player.isCreative() && isPlayerLookingAtMe(player)) {
                        setTriggered(true);
                        setAngryCooldown(196);
                        setAngry(true);
                        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                        targets.add(player.getUUID());
                        this.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ());
                        break;
                    }
                }
            }

            Player closestTarget = getClosestTarget();

            if (!isAngry() && !targets.isEmpty()) {
                for (UUID uuid : targets) {
                    Player player = this.level().getPlayerByUUID(uuid);
                    if (player != null && player.isAlive() && !player.isCreative()) {
                        setAngry(true);
                        setAngryCooldown(0);
                        setTarget(player);
                        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                        break;
                    }
                }
            }

            if (closestTarget != null) {
                setTarget(closestTarget);
            }

            if (isAngry()) {
                for (Player player : this.level().players()) {
                    if (!player.isCreative() && isPlayerLookingAtMe(player)) {
                        targets.add(player.getUUID());
                    }
                }

                if (getAngryCooldown() > 0) {
                    setAngryCooldown(getAngryCooldown() - 1);
                } else if (getAngryCooldown() == 0) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                    if (targets.isEmpty()) {
                        calmDown();
                    }
                }
            }

            if (this.getPathRecalculationTimer() >= 20) {
                this.getNavigation().recomputePath();
                this.setPathRecalculationTimer(0);
            } else {
                this.setPathRecalculationTimer(this.getPathRecalculationTimer() + 1);
            }
        }
    }

    public Player getClosestTarget() {
        double closestDistance = Double.MAX_VALUE;
        Player closestPlayer = null;

        for (UUID playerUUID : targets) {
            Player player = this.level().getPlayerByUUID(playerUUID);
            if (player != null) {
                double distance = this.distanceTo(player);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPlayer = player;
                }
            }
        }
        return closestPlayer;
    }

    public void calmDown() {
        setAngry(false);
        setTriggered(false);
        setAngryCooldown(0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
        targets.clear();
    }

    public void breakObstructingBlocks() {
        BlockPos basePos = this.blockPosition();

        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = basePos.offset(x, y, z);
                    BlockState state = this.level().getBlockState(checkPos);
                    if (!state.isAir() && state.getDestroySpeed(this.level(), checkPos) != -1.0F) {
                        this.level().destroyBlock(checkPos, false);
                    }
                }
            }
        }

        if (this.getTarget() != null) {
            Vec3 direction = this.getTarget().position().subtract(this.position()).normalize();
            for (int y = 0; y < 4; y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        Vec3 offset = direction.scale(1.5).add(x, y, z);
                        BlockPos pos = basePos.offset(Mth.floor(offset.x), Mth.floor(offset.y), Mth.floor(offset.z));
                        BlockState state = this.level().getBlockState(pos);

                        if (!state.isAir() && state.getDestroySpeed(this.level(), pos) != -1.0F) {
                            this.level().destroyBlock(pos, false);
                        }
                    }
                }
            }
        }

        this.getNavigation().recomputePath();
    }

    public void digStaircaseUp() {
        if (this.level().isClientSide || this.digCooldown-- > 0) return;

        BlockPos pos = this.blockPosition();
        BlockPos targetPos = this.getTarget() != null ? this.getTarget().blockPosition() : pos;

        int dx = targetPos.getX() - pos.getX();
        int dz = targetPos.getZ() - pos.getZ();
        int dy = targetPos.getY() - pos.getY();
        int stepX = Integer.signum(dx);
        int stepZ = Integer.signum(dz);

        if (Math.abs(dx) <= 1 && Math.abs(dz) <= 1 && dy > 1) {
            int maxHeight = Math.min(dy, 12);
            BlockPos bestSpot = null;

            for (int y = 1; y <= maxHeight; y++) {
                boolean space = true;

                for (int x = -1; x <= 1 && space; x++) {
                    for (int z = -1; z <= 1 && space; z++) {
                        BlockPos check = pos.offset(x, y, z);
                        BlockState state = this.level().getBlockState(check);

                        if (!state.isAir() && state.getDestroySpeed(this.level(), check) != -1.0F) {
                            this.level().destroyBlock(check, false);
                        }

                        if (!this.level().getBlockState(check).isAir()) {
                            space = false;
                        }
                    }
                }

                if (space) {
                    BlockPos check = pos.above(y);
                    if (this.level().getBlockState(check.above()).isAir()) {
                        bestSpot = check;
                    }
                }
            }

            if (bestSpot != null) {
                this.setPos(bestSpot.getX() + 0.5, bestSpot.getY(), bestSpot.getZ() + 0.5);
                this.setDeltaMovement(Vec3.ZERO);
            }

            this.digCooldown = 10;
            return;
        }

        if (dx == 0 && dz == 0 && dy <= 1) {
            if (dy == 1) {
                BlockPos above = pos.above();
                if (level().getBlockState(above).isAir() && level().getBlockState(above.above()).isAir()) {
                    this.setPos(this.getX(), this.getY() + 1, this.getZ());
                    return;
                }
            }
            return;
        }
        for (int i = 0; i < 3; i++) {
            BlockPos stairCenter = pos.offset(stepX * i, i, stepZ * i);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = 0; y < 3; y++) {
                        BlockPos check = stairCenter.offset(x, y, z);
                        BlockState state = this.level().getBlockState(check);

                        if (!state.isAir() && state.getDestroySpeed(this.level(), check) != -1.0F) {
                            this.level().destroyBlock(check, false);
                        }
                    }
                }
            }
        }

        // Nudge if stuck
        if (this.getDeltaMovement().lengthSqr() < 0.01) {
            this.setDeltaMovement(new Vec3(stepX * 0.25, 0.1, stepZ * 0.25));
            this.hasImpulse = true;
        }

        if (!this.getNavigation().isInProgress()) {
            BlockPos stairHead = pos.offset(stepX * 3, 3, stepZ * 3);

            if (this.level().getBlockState(stairHead).isAir() &&
                    this.level().getBlockState(stairHead.above()).isAir()) {

                Path path = this.getNavigation().createPath(stairHead, 0);
                if (path != null) {
                    this.getNavigation().moveTo(path, 1.2);
                    if (!this.getNavigation().isInProgress()) {
                        this.setPos(stairHead.getX() + 0.5, stairHead.getY(), stairHead.getZ() + 0.5);
                    }
                } else if (this.getDeltaMovement().lengthSqr() < 0.01) {
                    this.setPos(stairHead.getX() + 0.5, stairHead.getY(), stairHead.getZ() + 0.5);
                }
            }
        }

        this.digCooldown = 10;
    }

    public void digUpwardTowardPlayer(Player target) {
        BlockPos selfPos = this.blockPosition();
        BlockPos targetPos = target.blockPosition();

        int dy = targetPos.getY() - selfPos.getY();

        if (dy > 4) {
            boolean anyBroken = false;

            for (int i = 1; i <= 4; i++) {
                BlockPos toBreak = selfPos.above(i);
                BlockState state = level().getBlockState(toBreak);

                if (!state.isAir() && state.getDestroySpeed(level(), toBreak) != -1.0F) {
                    level().destroyBlock(toBreak, false);
                    anyBroken = true;
                }
            }

            if (anyBroken) {
                this.getNavigation().recomputePath();
            }

            boolean spaceClear = true;
            for (int i = 1; i <= 4; i++) {
                if (!level().getBlockState(selfPos.above(i)).isAir()) {
                    spaceClear = false;
                    break;
                }
            }
            if (spaceClear) {
                this.setDeltaMovement(this.getDeltaMovement().x, 0.42, this.getDeltaMovement().z); // simulate jump
                this.hasImpulse = true;
            }
        }
    }

    public void digDown() {
        BlockPos belowPos = this.blockPosition().below();
        BlockState state = this.level().getBlockState(belowPos);

        if (!state.isAir() && state.getDestroySpeed(this.level(), belowPos) != -1.0F) {
            this.level().destroyBlock(belowPos, false);
            this.getNavigation().recomputePath();
        }
    }

    public void forceEscape() {
        if (this.getStuckTimer() >= 40) {
            BlockPos newPos = this.blockPosition().offset(this.getDirection().getStepX() * 5, 1, this.getDirection().getStepZ() * 5);
            this.teleportTo(newPos.getX(), newPos.getY(), newPos.getZ());
            this.setStuckTimer(0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate));
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitVec, InteractionHand hand) {
        if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            calmDown();
            player.sendSystemMessage(Component.literal("SCP-096 has calmed down."));
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(player, hitVec, hand);
    }


    private <E extends SCP_096_Entity & GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        double speed = this.getDeltaMovement().horizontalDistanceSqr();

        if (this.isAngry()) {
            if (getAngryCooldown() > 0) {
                return event.setAndContinue(GETTING_MAD);
            } else {
                return event.setAndContinue(MAD_RUN);
            }
        }

        if (!this.isTriggered()) {
            if (speed > 0.0001) {
                return event.setAndContinue(SAD_WALK);
            } else {
                return event.setAndContinue(SAD_IDLE);
            }
        }

        return PlayState.CONTINUE;
    }

    private <E extends SCP_096_Entity & GeoAnimatable> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging) {
            return event.setAndContinue(MAD_ATTACK);
        }
        return PlayState.STOP;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean("IsAngry", this.isAngry());
        tag.putBoolean("IsTriggered", this.isTriggered());

        ListTag targetList = new ListTag();
        for (UUID targetUUID : targets) {
            targetList.add(StringTag.valueOf(targetUUID.toString()));
        }
        tag.put("Targets", targetList);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("IsAngry")) {
            this.setAngry(tag.getBoolean("IsAngry"));
        }
        if (tag.contains("IsTriggered")) {
            this.setTriggered(tag.getBoolean("IsTriggered"));
        }
        if (tag.contains("Targets")) {
            ListTag targetList = tag.getList("Targets", Tag.TAG_STRING);
            for (int i = 0; i < targetList.size(); i++) {
                targets.add(UUID.fromString(targetList.getString(i)));
            }
        }
        if (this.isAngry()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(2.5);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypeTags.IS_DROWNING)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(Fluids.LAVA);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

}
