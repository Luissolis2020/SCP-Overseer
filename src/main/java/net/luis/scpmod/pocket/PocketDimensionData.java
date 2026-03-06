package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class PocketDimensionData extends SavedData {
    private static final String NAME = "scpmod_pocket_dim";

    public boolean generated = false;
    public BlockPos startSpawn = new BlockPos(0, 64, 0);

    public BlockPos exitPos = null;

    public static PocketDimensionData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PocketDimensionData::load,
                PocketDimensionData::new,
                NAME
        );
    }

    public static PocketDimensionData load(CompoundTag tag) {
        PocketDimensionData d = new PocketDimensionData();
        d.generated = tag.getBoolean("generated");
        if (tag.contains("sx")) {
            d.startSpawn = new BlockPos(tag.getInt("sx"), tag.getInt("sy"), tag.getInt("sz"));
        }
        if (tag.contains("ex")) {
            d.exitPos = new BlockPos(tag.getInt("ex"), tag.getInt("ey"), tag.getInt("ez"));
        }
        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("generated", generated);
        tag.putInt("sx", startSpawn.getX());
        tag.putInt("sy", startSpawn.getY());
        tag.putInt("sz", startSpawn.getZ());

        if (exitPos != null) {
            tag.putInt("ex", exitPos.getX());
            tag.putInt("ey", exitPos.getY());
            tag.putInt("ez", exitPos.getZ());
        }

        return tag;
    }
}
