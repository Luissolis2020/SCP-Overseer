package net.luis.scpmod.item;

import net.luis.scpmod.capability.DrinkCounterCapability;
import net.luis.scpmod.network.DrinkCounterSyncPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;

public class SCP109 extends Item {
    private static final int MAX_DRINKS_BEFORE_NAUSEA = 5;
    private static final int DRINK_RESET_TIME = 200;

    public SCP109(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        if (!level.isClientSide) {

            if (level.dimension() == Level.NETHER) {
                BlockPos effectPos = pos.relative(context.getClickedFace());
                level.playSound(null, effectPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (blockState.is(Blocks.WATER_CAULDRON)) {
                int currentLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);

                if (currentLevel < LayeredCauldronBlock.MAX_FILL_LEVEL) {
                    level.setBlockAndUpdate(pos, blockState.setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL));
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }

            if (blockState.is(Blocks.CAULDRON)) {
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL));
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            BlockPos placePos = pos.relative(context.getClickedFace());
            if (level.getBlockState(placePos).canBeReplaced()) {
                level.setBlock(placePos, Blocks.WATER.defaultBlockState(), 3);
                level.playSound(null, placePos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        HitResult hitResult = player.pick(5, 0, false);
        if (hitResult.getType() == HitResult.Type.MISS) {
            player.startUsingItem(hand);
            level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                LazyOptional<DrinkCounterCapability> cap = player.getCapability(DrinkCounterCapability.INSTANCE);
                cap.ifPresent(counter -> {
                    long currentTime = level.getGameTime();

                    if (currentTime - counter.getLastDrinkTime() > DRINK_RESET_TIME) {
                        counter.setDrinkCount(0);
                    }

                    counter.setDrinkCount(counter.getDrinkCount() + 1);
                    counter.setLastDrinkTime(currentTime);

                    if (player instanceof ServerPlayer serverPlayer) {
                        SCPModNetwork.sendToPlayer(new DrinkCounterSyncPacket(counter.getDrinkCount()), serverPlayer);
                    }

                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 100, 0, false, false));

                    if (player.hasEffect(MobEffects.POISON)) {
                        player.removeEffect(MobEffects.POISON);
                    }

                    if (counter.getDrinkCount() >= MAX_DRINKS_BEFORE_NAUSEA) {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
                        counter.setDrinkCount(0);
                    }
                });
            }
        }
        return stack;
    }

}
