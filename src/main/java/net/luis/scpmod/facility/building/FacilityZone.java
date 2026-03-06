package net.luis.scpmod.facility.building;

import net.minecraft.core.BlockPos;

public class FacilityZone {
    public final String name;
    public final BlockPos minCorner;
    public final BlockPos maxCorner;

    public FacilityZone(String name, BlockPos min, BlockPos max) {
        this.name = name;
        this.minCorner = min;
        this.maxCorner = max;
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= minCorner.getX() && pos.getX() <= maxCorner.getX()
                && pos.getY() >= minCorner.getY() && pos.getY() <= maxCorner.getY()
                && pos.getZ() >= minCorner.getZ() && pos.getZ() <= maxCorner.getZ();
    }
}

