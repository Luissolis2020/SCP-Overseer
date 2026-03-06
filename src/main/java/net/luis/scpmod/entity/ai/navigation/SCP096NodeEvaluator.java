package net.luis.scpmod.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;

public class SCP096NodeEvaluator extends WalkNodeEvaluator {

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = level.getBlockState(pos);

        if (!state.isAir() && state.getDestroySpeed(level, pos) != -1.0F) {
            return BlockPathTypes.DANGER_OTHER;
        }

        return super.getBlockPathType(level, x, y, z);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int x, int y, int z, Mob mob) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = level.getBlockState(pos);

        if (!state.isAir() && state.getDestroySpeed(level, pos) != -1.0F) {
            return BlockPathTypes.DANGER_OTHER;
        }

        return super.getBlockPathType(level, x, y, z, mob);
    }
}
