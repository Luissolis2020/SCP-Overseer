package net.luis.scpmod.anomaly;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class AnomalyFieldSavedData extends SavedData {

    public static final String DATA_NAME = "scpmod_anomaly_fields";

    public record Zone(int id, BlockPos center, int radius, int tier) {
        public boolean contains(BlockPos pos) {
            // XZ distance only for nicer gameplay (multi-floor friendly)
            double dx = pos.getX() - center.getX();
            double dz = pos.getZ() - center.getZ();
            return (dx * dx + dz * dz) <= (radius * radius);
        }
    }

    private final List<Zone> zones = new ArrayList<>();

    public List<Zone> getZones() {
        return zones;
    }

    public static AnomalyFieldSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnomalyFieldSavedData::load,   // Function<CompoundTag, AnomalyFieldSavedData>
                AnomalyFieldSavedData::new,    // Supplier<AnomalyFieldSavedData>
                DATA_NAME
        );
    }

    public void ensureGenerated(ServerLevel level) {
        if (!zones.isEmpty()) return;

        BlockPos spawn = level.getSharedSpawnPos();
        RandomSource r = RandomSource.create(level.getSeed() ^ 0x5A17C0DEL);

        int zoneCount = 3;              // tweak later
        int minDist = 80;               // from spawn
        int maxDist = 220;
        int minRadius = 28;
        int maxRadius = 48;

        for (int i = 0; i < zoneCount; i++) {
            double angle = r.nextDouble() * Math.PI * 2.0;
            int dist = Mth.nextInt(r, minDist, maxDist);

            int x = spawn.getX() + (int)Math.round(Math.cos(angle) * dist);
            int z = spawn.getZ() + (int)Math.round(Math.sin(angle) * dist);
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

            BlockPos center = new BlockPos(x, y, z);
            int radius = Mth.nextInt(r, minRadius, maxRadius);
            int tier = 1;

            zones.add(new Zone(i, center, radius, tier));
        }

        setDirty();
    }

    public static AnomalyFieldSavedData load(CompoundTag tag) {
        AnomalyFieldSavedData data = new AnomalyFieldSavedData();
        ListTag list = tag.getList("zones", Tag.TAG_COMPOUND);

        for (int idx = 0; idx < list.size(); idx++) {
            CompoundTag z = list.getCompound(idx);

            int id = z.contains("id") ? z.getInt("id") : idx; // backwards compat
            BlockPos center = new BlockPos(z.getInt("x"), z.getInt("y"), z.getInt("z"));
            int radius = z.getInt("r");
            int tier = z.getInt("tier");

            data.zones.add(new Zone(id, center, radius, tier));
        }
        return data;
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Zone zone : zones) {
            CompoundTag z = new CompoundTag();
            z.putInt("id", zone.id());
            z.putInt("x", zone.center.getX());
            z.putInt("y", zone.center.getY());
            z.putInt("z", zone.center.getZ());
            z.putInt("r", zone.radius());
            z.putInt("tier", zone.tier());
            list.add(z);
        }
        tag.put("zones", list);
        return tag;
    }
}
