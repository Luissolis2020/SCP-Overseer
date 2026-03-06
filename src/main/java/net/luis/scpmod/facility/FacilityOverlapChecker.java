package net.luis.scpmod.facility;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class FacilityOverlapChecker {

    private static final Map<ResourceKey<Level>, Set<BoundingBox>> BOUNDS = new HashMap<>();

    private static Set<BoundingBox> getSet(Level level) {
        return BOUNDS.computeIfAbsent(level.dimension(), k -> new HashSet<>());
    }

    public static void clearFor(ServerLevel level) {
        getSet(level).clear();
    }

    public static boolean isOverlapping(ServerLevel level, BlockPos origin, StructureTemplate template, StructurePlaceSettings settings) {
        BoundingBox previewBox = computeBox(origin, template, settings);
        for (BoundingBox existing : getSet(level)) {
            if (existing.intersects(previewBox)) return true;
        }
        return false;
    }

    public static List<AABB> getOverlappingBoundingBoxes(Level level, BlockPos pos, StructureTemplate template, StructurePlaceSettings settings) {
        BoundingBox previewBox = computeBox(pos, template, settings);

        List<AABB> overlapping = new ArrayList<>();
        for (BoundingBox other : getSet(level)) {
            if (other.intersects(previewBox)) {
                overlapping.add(new AABB(
                        other.minX(), other.minY(), other.minZ(),
                        other.maxX() + 1, other.maxY() + 1, other.maxZ() + 1
                ));
            }
        }
        return overlapping;
    }

    public static void registerStructurePlacement(ServerLevel level, BlockPos origin, StructureTemplate template, StructurePlaceSettings settings) {
        getSet(level).add(computeBox(origin, template, settings));
    }

    private static BoundingBox computeBox(BlockPos origin, StructureTemplate template, StructurePlaceSettings settings) {
        return template.getBoundingBox(settings, origin);
    }
}

