package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.AnomalyEntity;
import net.luis.scpmod.entity.ai.SCP_1048_CollectItems_Goal;
import net.luis.scpmod.research.ModResearchEntries;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SCP_1048_Entity extends PathfinderMob implements GeoEntity, AnomalyEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation BOWTIE = RawAnimation.begin().thenPlay("bowtie");
    public static final RawAnimation DANCE = RawAnimation.begin().thenLoop("jig");
    public static final RawAnimation DRAW = RawAnimation.begin().thenPlay("drawing");
    public static final RawAnimation HURT = RawAnimation.begin().thenPlay("hurt");
    public static final RawAnimation BUILD = RawAnimation.begin().thenPlay("build_a_bear");

    private static final EntityDataAccessor<Integer> HURT_ANIMATION_TICKS = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRAW_TIME = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DANCE_ANIMATION_TICKS = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BOWTIE_ANIMATION_TICKS = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BUILD_ANIMATION_TICKS = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<String> MATERIAL_TEXTURE = SynchedEntityData.defineId(SCP_1048_Entity.class, EntityDataSerializers.STRING);

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

    private RawAnimation currentAnimation = IDLE;

    private ResourceLocation materialTexture;

    public SCP_1048_Entity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
    }

    @Override
    public ResourceLocation getResearchId() {
        return ModResearchEntries.SCP_1048.getId();
    }

    @Override
    public String getAnomalyDisplayName() {
        return "SCP-1048";
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HURT_ANIMATION_TICKS, 0);
        this.entityData.define(DRAW_TIME, 0);
        this.entityData.define(DANCE_ANIMATION_TICKS, 0);
        this.entityData.define(BOWTIE_ANIMATION_TICKS, 0);
        this.entityData.define(BUILD_ANIMATION_TICKS, 0);
        this.entityData.define(MATERIAL_TEXTURE, "");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag list = new ListTag();
        for (ItemStack stack : inventory) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);
            list.add(itemTag);
        }
        tag.put("Inventory", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ListTag list = tag.getList("Inventory", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemTag = list.getCompound(i);
            inventory.set(i, ItemStack.of(itemTag));
        }
    }

    public void setCurrentAnimation(RawAnimation animation) {
        this.currentAnimation = animation;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));

        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0f));

        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return getDanceAnimationTicks() == 0 && getBowtieAnimationTicks() == 0 && getDrawTime() == 0;
            }
        });
        this.goalSelector.addGoal(4, new SCP_1048_CollectItems_Goal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (getDanceAnimationTicks() > 0 || getDrawTime() > 0 || getBowtieAnimationTicks() > 0 || getBuildAnimationTicks() > 0) {
            this.getNavigation().stop();
        }

        if (getHurtAnimationTicks() > 0) {
            setHurtAnimationTicks(getHurtAnimationTicks() - 1);
            setCurrentAnimation(HURT);
            return;
        }

        if (getDrawTime() > 0) {
            setDrawTime(getDrawTime() - 1);
            setCurrentAnimation(DRAW);
            return;
        }

        if (getDanceAnimationTicks() > 0) {
            setDanceAnimationTicks(getDanceAnimationTicks() - 1);
            setCurrentAnimation(DANCE);
            return;
        }

        if (getBowtieAnimationTicks() > 0) {
            setBowtieAnimationTicks(getBowtieAnimationTicks() - 1);
            setCurrentAnimation(BOWTIE);
            return;
        }

        if (getBuildAnimationTicks() > 0) {
            setBuildAnimationTicks(getBuildAnimationTicks() - 1);
            setCurrentAnimation(BUILD);

            if (getBuildAnimationTicks() == 30 && !this.level().isClientSide) {
                // TEMP: Spawn a cow as the SCP-1048-A placeholder in front of SCP-1048
                Cow cow = EntityType.COW.create(this.level());
                if (cow != null) {
                    double spawnX = this.getX() + this.getLookAngle().x * 1.5;
                    double spawnY = this.getY();
                    double spawnZ = this.getZ() + this.getLookAngle().z * 1.5;
                    cow.moveTo(spawnX, spawnY, spawnZ, this.getYRot(), 0);
                    this.level().addFreshEntity(cow);
                }
            }

            return;
        }

        double speed = this.getDeltaMovement().horizontalDistanceSqr();
        if (speed > 0.000001) {
            setCurrentAnimation(WALK);
        } else {
            setCurrentAnimation(IDLE);
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (getHurtAnimationTicks() > 0 || getDrawTime() > 0 || getDanceAnimationTicks() > 0 || getBowtieAnimationTicks() > 0) {
                return InteractionResult.PASS;
            }

            float chance = this.random.nextFloat();

            this.getNavigation().stop();
            this.getLookControl().setLookAt(player, 30.0F, 30.0F);

            if (getInventoryItemTotal() >= 9 && chance < 1f) {
                // Clear inventory for simplicity, or remove just 9 slots
                for (int i = 0; i < inventory.size(); i++) {
                    inventory.set(i, ItemStack.EMPTY);
                }

                setBuildAnimationTicks(193);
                setCurrentAnimation(BUILD);
                return InteractionResult.SUCCESS;
            }

//            if (chance < 0.3) {
//                setDanceAnimationTicks(252);
//            } else if (chance < 0.6) {
//                setBowtieAnimationTicks(24);
//            } else {
//                setDrawTime(600);
//            }

            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

//    @Override
//    public InteractionResult mobInteract(Player player, InteractionHand hand) {
//        if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
//            this.setMaterialTexture(ResourceLocation.fromNamespaceAndPath("scpmod", "textures/entity/scp_1048/scp_1048_dirt_variant.png"));
//            player.sendSystemMessage(Component.literal("Texture set to dirt!"));
//            return InteractionResult.SUCCESS;
//        }
//        return super.mobInteract(player, hand);
//    }

    public boolean addItemToInventory(ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack current = inventory.get(i);
            if (current.isEmpty()) {
                inventory.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result) {
            this.setHurtAnimationTicks(10);
        }
        return result;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }
    }

    public int getInventoryItemTotal() {
        return (int) inventory.stream().filter(stack -> !stack.isEmpty()).count();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <E extends SCP_1048_Entity & GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        return event.setAndContinue(this.currentAnimation);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public int getHurtAnimationTicks() {
        return this.entityData.get(HURT_ANIMATION_TICKS);
    }

    public void setHurtAnimationTicks(int value) {
        this.entityData.set(HURT_ANIMATION_TICKS, value);
    }

    public int getDrawTime() {
        return this.entityData.get(DRAW_TIME);
    }

    public void setDrawTime(int value) {
        this.entityData.set(DRAW_TIME, value);
    }

    public int getDanceAnimationTicks() {
        return this.entityData.get(DANCE_ANIMATION_TICKS);
    }

    public void setDanceAnimationTicks(int value) {
        this.entityData.set(DANCE_ANIMATION_TICKS, value);
    }

    public int getBowtieAnimationTicks() {
        return this.entityData.get(BOWTIE_ANIMATION_TICKS);
    }

    public void setBowtieAnimationTicks(int value) {
        this.entityData.set(BOWTIE_ANIMATION_TICKS, value);
    }

    public int getBuildAnimationTicks() {
        return this.entityData.get(BUILD_ANIMATION_TICKS);
    }

    public void setBuildAnimationTicks(int value) {
        this.entityData.set(BUILD_ANIMATION_TICKS, value);
    }

    public void setMaterialTexture(ResourceLocation texture) {
        this.entityData.set(MATERIAL_TEXTURE, texture.toString());
    }

    public ResourceLocation getMaterialTexture() {
        String path = this.entityData.get(MATERIAL_TEXTURE);
        return path.isEmpty() ? null : ResourceLocation.parse(path);
    }

    public boolean hasMaterialTexture() {
        return !this.entityData.get(MATERIAL_TEXTURE).isEmpty();
    }
}
