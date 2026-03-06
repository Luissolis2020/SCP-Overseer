package net.luis.scpmod.facility.building;

import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.facility.construction.ConstructionState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class FacilityRoomInstance {
    public final BlockPos origin;
    public final FacilityBlueprint blueprint;

    public ConstructionSite constructionSite;
    public ConstructionState state = ConstructionState.PLANNED;

    private boolean isBuilt = false;
    private boolean isActive = false;

    private UUID owner;
    private Rotation rotation = Rotation.NONE;

    public static final class WorldConnector {
        public final String name;
        public final BlockPos worldPos;
        public final Direction worldDir;
        public final int width, height;
        public WorldConnector(String name, BlockPos worldPos, Direction worldDir, int width, int height) {
            this.name = name;
            this.worldPos = worldPos.immutable();
            this.worldDir = worldDir;
            this.width = width;
            this.height = height;
        }
    }
    private List<WorldConnector> worldConnectors = List.of();

    private final Map<String, AABB> zoneBounds = new HashMap<>();

    public FacilityRoomInstance(FacilityBlueprint blueprint, BlockPos origin) {
        this(blueprint, origin, Rotation.NONE);
    }

    public FacilityRoomInstance(FacilityBlueprint blueprint, BlockPos origin, Rotation rotation) {
        this.blueprint = blueprint;
        this.origin = origin.immutable();
        this.rotation = rotation;
        resolveZones();
        recomputeConnectors();
    }

    private void resolveZones() {
        zoneBounds.clear();
        if (blueprint.zoneBounds == null) return;

        for (Map.Entry<String, int[]> entry : blueprint.zoneBounds.entrySet()) {
            int[] raw = entry.getValue();
            if (raw.length != 6) {
                System.err.println("[SCPMod] Invalid zone bounds for: " + entry.getKey() + " in " + blueprint.id);
                continue;
            }

            BlockPos a = rotateLocal(new BlockPos(raw[0], raw[1], raw[2]), rotation, blueprint.width, blueprint.depth);
            BlockPos b = rotateLocal(new BlockPos(raw[3], raw[4], raw[5]), rotation,blueprint.width, blueprint.depth);

            BlockPos min = new BlockPos(
                    Math.min(a.getX(), b.getX()),
                    Math.min(a.getY(), b.getY()),
                    Math.min(a.getZ(), b.getZ())
            );
            BlockPos max = new BlockPos(
                    Math.max(a.getX(), b.getX()),
                    Math.max(a.getY(), b.getY()),
                    Math.max(a.getZ(), b.getZ())
            );

            zoneBounds.put(entry.getKey(), new AABB(origin.offset(min), origin.offset(max)));
        }
    }

    public void recomputeConnectors() {
        if (blueprint.getConnectors().isEmpty()) { worldConnectors = List.of(); return; }
        List<WorldConnector> out = new ArrayList<>(blueprint.getConnectors().size());
        for (FacilityBlueprint.Connector c : blueprint.getConnectors()) {
            BlockPos rLocal = rotateLocal(c.localPos, rotation, blueprint.width, blueprint.depth);
            Direction rDir = rotateDirection(c.dir, rotation);
            out.add(new WorldConnector(c.name, origin.offset(rLocal), rDir, c.width, c.height));
        }
        worldConnectors = Collections.unmodifiableList(out);
    }

    public List<WorldConnector> getWorldConnectors() { return worldConnectors; }
    public Rotation getRotation() { return rotation; }
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
        resolveZones();
        recomputeConnectors();
    }

    private static BlockPos rotateLocal(BlockPos p, Rotation rot, int w, int d) {
        int x = p.getX(), y = p.getY(), z = p.getZ();
        return switch (rot) {
            case NONE -> p;
            case CLOCKWISE_90 -> new BlockPos(d - 1 - z, y, x);
            case CLOCKWISE_180 -> new BlockPos(w - 1 - x, y, d - 1 - z);
            case COUNTERCLOCKWISE_90 -> new BlockPos(z, y, w - 1 - x);
        };
    }
    private static Direction rotateDirection(Direction d, Rotation rot) {
        if (d.getAxis() == Direction.Axis.Y) return d; // keep UP/DOWN unchanged
        return switch (rot) {
            case NONE -> d;
            case CLOCKWISE_90 -> d.getClockWise();
            case CLOCKWISE_180 -> d.getOpposite();
            case COUNTERCLOCKWISE_90 -> d.getCounterClockWise();
        };
    }

    public boolean isComplete() {
        return state == ConstructionState.BUILT || state == ConstructionState.ACTIVE;
    }

    public AABB getZoneBounds(String zoneName) {
        return zoneBounds.get(zoneName);
    }

    public void linkConstructionSite(ConstructionSite site) {
        this.constructionSite = site;
    }

    public ConstructionSite getConstructionSite() {
        return this.constructionSite;
    }

    public Map<String, AABB> getAllZones() {
        return zoneBounds;
    }

    public void setBuilt(boolean built) {
        this.isBuilt = built;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public String getDisplayState() {
        if (!isBuilt()) {
            return switch (state) {
                case PLANNED -> "PLANNED";
                case UNDER_CONSTRUCTION -> "UNDER_CONSTRUCTION";
                default -> "UNKNOWN";
            };
        }
        return isActive() ? "ACTIVE" : "BUILT";
    }

    public boolean isUnderConstruction() {
        return state == ConstructionState.UNDER_CONSTRUCTION;
    }

    public void markBuiltAndActive() {
        this.isBuilt = true;
        this.isActive = true;
        this.state = ConstructionState.ACTIVE;
    }

    public void setOwner(UUID owner) { this.owner = owner; }
    public UUID getOwner() { return owner; }

}
