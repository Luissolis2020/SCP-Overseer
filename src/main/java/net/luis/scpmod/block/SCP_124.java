package net.luis.scpmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.IPlantable;

public class SCP_124 extends FarmBlock {
    public SCP_124(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MOISTURE, 7));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state.setValue(MOISTURE, 7), 2);

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        // Handle Beetroot
        if (aboveBlock instanceof BeetrootBlock) {
            growBeetroot(level, abovePos, aboveState);
        }
        // Handle Normal Crops
        else if (aboveBlock instanceof CropBlock crop) {
            growCrops(level, abovePos, aboveState, crop);
        }
        // Handle Nether Wart
        else if (aboveBlock instanceof NetherWartBlock) {
            growNetherWart(level, abovePos, aboveState);
        }
        // Handle Sugarcane, Bamboo, and Cactus
        else if (aboveBlock == Blocks.SUGAR_CANE || aboveBlock == Blocks.BAMBOO || aboveBlock == Blocks.CACTUS) {
            growTallPlants(level, abovePos, aboveState);
        }
        // Handle Melon & Pumpkin Growth
        else if (aboveBlock instanceof StemBlock) {
            growMelonPumpkin(level, abovePos, aboveState);
        }
        // Handle Trees (Saplings)
        else if (aboveBlock instanceof SaplingBlock sapling) {
            growTree(level, abovePos, aboveState, sapling, random);
        }
        // Handle Mushrooms
        else if (aboveBlock == Blocks.BROWN_MUSHROOM || aboveBlock == Blocks.RED_MUSHROOM) {
            growMushroom(level, abovePos, aboveState, (MushroomBlock) aboveBlock, random);
        }
        // Fallback
        else if (aboveBlock instanceof BonemealableBlock bone) {
            if (bone.isValidBonemealTarget(level, abovePos, aboveState, level.isClientSide)) {
                if (bone.isBonemealSuccess(level, random, abovePos, aboveState)) {
                    bone.performBonemeal(level, random, abovePos, aboveState);
                    spawnGrowthParticles(level, abovePos);
                }
            }
        }
    }

    private void growCrops(ServerLevel level, BlockPos pos, BlockState state, CropBlock crop) {
        int maxAge = crop.getMaxAge();
        int currentAge = state.getValue(CropBlock.AGE);

        if (currentAge < maxAge) {
            level.setBlock(pos, state.setValue(CropBlock.AGE, maxAge), 2);
            spawnGrowthParticles(level, pos);
        }
    }

    private void growBeetroot(ServerLevel level, BlockPos pos, BlockState state) {
        IntegerProperty beetrootAge = BlockStateProperties.AGE_3;
        int maxAge = 3;

        if (state.hasProperty(beetrootAge)) {
            int currentAge = state.getValue(beetrootAge);

            if (currentAge < maxAge) {
                level.setBlock(pos, state.setValue(beetrootAge, maxAge), 2);
                spawnGrowthParticles(level, pos);
            }
        }
    }

    private void growNetherWart(ServerLevel level, BlockPos pos, BlockState state) {
        int maxAge = 3;
        if (state.hasProperty(NetherWartBlock.AGE)) {
            int currentAge = state.getValue(NetherWartBlock.AGE);

            if (currentAge < maxAge) {
                level.setBlock(pos, state.setValue(NetherWartBlock.AGE, maxAge), 2);
                spawnGrowthParticles(level, pos);
            }
        }
    }

    private void growTallPlants(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.BAMBOO_SAPLING)) {
            level.setBlock(pos, Blocks.BAMBOO.defaultBlockState(), 2);
            spawnGrowthParticles(level, pos);
        } else {
            BlockPos topPos = pos;
            int height = 1;

            while (level.getBlockState(topPos.above()).is(state.getBlock())) {
                topPos = topPos.above();
                height++;
            }

            // Set growth limits:
            int maxHeight = state.is(Blocks.CACTUS) ? 9 :
                    state.is(Blocks.SUGAR_CANE) ? 12 :
                            state.is(Blocks.BAMBOO) ? 48 : 0;

            // Check if it's below the height limit before growing
            if (level.isEmptyBlock(topPos.above()) && height < maxHeight) {
                level.setBlock(topPos.above(), state.getBlock().defaultBlockState(), 2);
                spawnGrowthParticles(level, topPos.above());
            }
        }
    }

    private void growMelonPumpkin(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.AGE_7)) {
            int maxAge = 7;
            int currentAge = state.getValue(BlockStateProperties.AGE_7);

            if (currentAge < maxAge) {
                level.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, maxAge), 2);
                spawnGrowthParticles(level, pos);
            }
        }
    }

    private void growTree(ServerLevel level, BlockPos pos, BlockState state, SaplingBlock sapling, RandomSource random) {
        sapling.performBonemeal(level, random, pos, state);
        spawnGrowthParticles(level, pos);
    }

    private void growMushroom(ServerLevel level, BlockPos pos, BlockState state, MushroomBlock mushroom, RandomSource random) {
        mushroom.growMushroom(level, pos, state, random);
        spawnGrowthParticles(level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction facing, IPlantable plantable) {
        return true;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    private void spawnGrowthParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5);
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5);
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.0);
        }
    }
}
