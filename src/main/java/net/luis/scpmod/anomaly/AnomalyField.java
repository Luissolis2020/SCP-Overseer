package net.luis.scpmod.anomaly;

import net.luis.scpmod.pda.PDADataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class AnomalyField {

    public static boolean isAnomalous(ServerLevel level, BlockPos pos) {
        AnomalyFieldSavedData data = AnomalyFieldSavedData.get(level);
        data.ensureGenerated(level);
        for (var z : data.getZones()) {
            if (z.contains(pos)) return true;
        }
        return false;
    }

    public static double distXZ(BlockPos a, BlockPos b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx*dx + dz*dz);
    }

    public static AnomalyFieldSavedData.Zone nearestZone(ServerLevel level, BlockPos pos, ServerPlayer player) {
        var data = AnomalyFieldSavedData.get(level);
        data.ensureGenerated(level);

        AnomalyFieldSavedData.Zone best = null;
        double bestDist = Double.MAX_VALUE;

        for (var z : data.getZones()) {
            if (PDADataHandler.isZoneCleared(player, z.id())) continue;

            double d = distXZ(pos, z.center());
            if (d < bestDist) {
                bestDist = d;
                best = z;
            }
        }
        return best;
    }

    public static AnomalyFieldSavedData.Zone zoneAt(ServerLevel level, BlockPos pos) {
        AnomalyFieldSavedData data = AnomalyFieldSavedData.get(level);
        data.ensureGenerated(level);

        for (var z : data.getZones()) {
            if (z.contains(pos)) return z;
        }
        return null;
    }

    public static boolean hasUnclearedZones(ServerLevel level, ServerPlayer player) {
        var data = AnomalyFieldSavedData.get(level);
        data.ensureGenerated(level);

        for (var z : data.getZones()) {
            if (!PDADataHandler.isZoneCleared(player, z.id())) return true;
        }
        return false;
    }

    public static double edgeDistance(BlockPos pos, AnomalyFieldSavedData.Zone zone) {
        double dist = distXZ(pos, zone.center());
        return Math.max(0.0, dist - zone.radius());
    }

    public static double coreRadius(AnomalyFieldSavedData.Zone zone) {
        // Scales with zone size. Tune these numbers.
        // Example: core is 20% of the field radius, clamped to [5..10]
        return net.minecraft.util.Mth.clamp(zone.radius() * 0.20, 5.0, 10.0);
    }

    public static boolean isInCore(BlockPos pos, AnomalyFieldSavedData.Zone zone) {
        return distXZ(pos, zone.center()) <= coreRadius(zone);
    }



}
