package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public final class PocketMath {
    public static BlockPos rotate(BlockPos p, Rotation r, BlockPos size) {
        int x = p.getX();
        int y = p.getY();
        int z = p.getZ();

        int maxX = size.getX() - 1;
        int maxZ = size.getZ() - 1;

        return switch (r) {
            case NONE -> p;

            // (x, z) -> (z, maxX - x)
            case CLOCKWISE_90 -> new BlockPos(z, y, maxX - x);

            // (x, z) -> (maxX - x, maxZ - z)
            case CLOCKWISE_180 -> new BlockPos(maxX - x, y, maxZ - z);

            // (x, z) -> (maxZ - z, x)
            case COUNTERCLOCKWISE_90 -> new BlockPos(maxZ - z, y, x);
        };
    }


    public static Direction rotate(Direction d, Rotation r) {
        return switch (r) {
            case NONE -> d;
            case CLOCKWISE_90 -> d.getClockWise();
            case CLOCKWISE_180 -> d.getOpposite();
            case COUNTERCLOCKWISE_90 -> d.getCounterClockWise();
        };
    }
}
