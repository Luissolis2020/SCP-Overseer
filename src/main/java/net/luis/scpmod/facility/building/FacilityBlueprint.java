package net.luis.scpmod.facility.building;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.Locale;

public class FacilityBlueprint {
    public ResourceLocation id;
    public String displayName;
    public int width, height, depth;
    public String structureFile;

    @SerializedName("requiredMaterials")
    public Map<String, Integer> requiredMaterialsRaw;
    public transient Map<Item, Integer> requiredMaterials = new HashMap<>();

    @SerializedName("zones")
    public Map<String, int[]> zoneBounds;

    @SerializedName("connectors")
    public List<RawConnector> connectorsRaw;

    public transient List<Connector> connectors = List.of();

    public static final class RawConnector {
        public String name;
        @SerializedName("pos")
        public int[] pos;                // [x,y,z]
        public String dir;               // "NORTH/EAST/SOUTH/WEST/UP/DOWN"
        public Integer width;
        public Integer height;
    }

    public static final class Connector {
        public final String name;
        public final BlockPos localPos;  // local space (pre-rotation)
        public final Direction dir;      // local facing (pre-rotation)
        public final int width, height;  // aperture size
        public Connector(String name, BlockPos localPos, Direction dir, int width, int height) {
            this.name = name;
            this.localPos = localPos.immutable();
            this.dir = dir;
            this.width = width;
            this.height = height;
        }
        @Override public String toString() {
            return "Connector{" + name + " @" + localPos + " " + dir + " " + width + "x" + height + "}";
        }
    }

    public void resolveMaterials() {
        if (requiredMaterialsRaw == null) {
            System.err.println("[SCPMod] Blueprint " + id + " has no requiredMaterials defined.");
            return;
        }
        for (Map.Entry<String, Integer> entry : requiredMaterialsRaw.entrySet()) {
            Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(entry.getKey()));
            if (item != null) {
                requiredMaterials.put(item, entry.getValue());
            } else {
                System.err.println("[SCPMod] Unknown item: " + entry.getKey() + " in blueprint " + id);
            }
        }
    }

    public void resolveConnectors() {
        if (connectorsRaw == null || connectorsRaw.isEmpty()) {
            this.connectors = List.of();
            return;
        }
        List<Connector> out = new ArrayList<>(connectorsRaw.size());
        for (RawConnector rc : connectorsRaw) {
            if (rc == null || rc.pos == null || rc.pos.length != 3 || rc.dir == null) {
                System.err.println("[SCPMod] Invalid connector in " + id + ": " + rc);
                continue;
            }
            Direction d;
            try {
                d = Direction.valueOf(rc.dir.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                System.err.println("[SCPMod] Bad connector dir '" + rc.dir + "' in " + id);
                continue;
            }
            String n = (rc.name != null && !rc.name.isEmpty())
                    ? rc.name
                    : (d + "_" + rc.pos[0] + "_" + rc.pos[1] + "_" + rc.pos[2]);
            int w = rc.width != null ? rc.width : 1;
            int h = rc.height != null ? rc.height : 2;
            out.add(new Connector(n, new BlockPos(rc.pos[0], rc.pos[1], rc.pos[2]), d, w, h));
        }
        this.connectors = Collections.unmodifiableList(out);
    }

    public List<Connector> getConnectors() { return connectors; }

    public ResourceLocation getStructureFileLocation() {
        return ResourceLocation.parse(this.structureFile);
    }

    public int[] getZone(String name) {
        return zoneBounds != null ? zoneBounds.get(name) : null;
    }

    public String getDisplayName() {
        return displayName;
    }
}
