package net.luis.scpmod.block.entity;

import net.luis.scpmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SCP_458_Entity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("Opening");
    public static final RawAnimation FINISH = RawAnimation.begin().thenPlayAndHold("Finished");
    public static final RawAnimation CLOSE = RawAnimation.begin().thenPlayAndHold("Closing");

    private int cooldownTicks = 0;
    private int closeDelay = -1;
    private Phase phase = Phase.CLOSED;

    public SCP_458_Entity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCP_458_Entity.get(), pos, state);
    }

    public enum Phase {
        CLOSED,
        OPENED,
        FINISHED
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "procedure", 0, this::procedurePredicate)
                .triggerableAnim("opening", OPEN)
                .triggerableAnim("closing", CLOSE)
                .triggerableAnim("finished", FINISH));

    }

    private <T extends GeoAnimatable> PlayState procedurePredicate(AnimationState<T> state) {
        return PlayState.CONTINUE;
    }

    public void tick() {
        if (cooldownTicks > 0) cooldownTicks--;

        if (closeDelay > 0) {
            closeDelay--;
        } else if (closeDelay == 0) {
            spawnPizzaOnGround();
            this.triggerAnim("procedure", "closing");
            this.phase = Phase.CLOSED;
            this.closeDelay = -1;
        }
    }

    public boolean canActivate() {
        return cooldownTicks <= 0;
    }

    public void tryActivate(Player player) {
        if (!canActivate()) return;

        switch (phase) {
            case CLOSED -> {
                this.phase = Phase.OPENED;
                this.triggerAnim("procedure", "opening");
                this.playSteamEffect();
            }
            case OPENED -> {
                this.phase = Phase.FINISHED;
                this.closeDelay = 15;
                this.triggerAnim("procedure", "finished");
                this.cooldownTicks = 100;
            }
        }

        this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }


    private void spawnPizzaOnGround() {
        if (level.isClientSide) return;

        ItemStack pizza = new ItemStack(ModItems.SCP_458_Slice.get(), 8);
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.75;
        double z = worldPosition.getZ() + 0.5;

        ItemEntity dropped = new ItemEntity(level, x, y, z, pizza);
        dropped.setDeltaMovement(0, 0.15, 0);
        level.addFreshEntity(dropped);

        level.playSound(
                null,
                x, y, z,
                SoundEvents.ITEM_PICKUP,
                net.minecraft.sounds.SoundSource.BLOCKS,
                0.5f,
                1.0f
        );
    }

    private void playSteamEffect() {
        if (level.isClientSide) return;

        level.playSound(
                null,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.75,
                worldPosition.getZ() + 0.5,
                SoundEvents.FIRE_EXTINGUISH,
                net.minecraft.sounds.SoundSource.BLOCKS,
                0.7f,
                1.4f
        );

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 4; i++) {
                double px = worldPosition.getX() + 0.5 + (level.random.nextGaussian() * 0.1);
                double py = worldPosition.getY() + 0.9;
                double pz = worldPosition.getZ() + 0.5 + (level.random.nextGaussian() * 0.1);
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        px, py, pz,
                        1,
                        0, 0, 0,
                        0.01
                );
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
