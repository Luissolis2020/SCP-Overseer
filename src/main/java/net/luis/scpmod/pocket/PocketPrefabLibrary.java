package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PocketPrefabLibrary {

    private static boolean loaded = false;

    private static @Nullable PocketPrefab start;
    private static final List<WeightedPrefab> exits = new ArrayList<>();
    private static final List<WeightedPrefab> halls = new ArrayList<>();
    private static final List<WeightedPrefab> rooms = new ArrayList<>();
    private static final List<WeightedPrefab> deadEnds = new ArrayList<>();
    private static final List<WeightedPrefab> traps = new ArrayList<>();

    public static void invalidate() {
        loaded = false;
        start = null;
        exits.clear();
        halls.clear();
        rooms.clear();
        deadEnds.clear();
        traps.clear();
    }

    public static void ensureLoaded(MinecraftServer server) {
        if (loaded) return;

        PocketPrefabConfig cfg = PocketPrefabRegistry.getConfig();
        if (cfg == null) {
            System.out.println("[SCPMod] PocketPrefabLibrary: config is null (prefabs.json not loaded yet?)");
            return;
        }

        StructureTemplateManager stm = server.getStructureManager();

        // START
        if (cfg.start == null || cfg.start.id == null) {
            throw new IllegalStateException("prefabs.json is missing 'start.id'");
        }
        start = loadPrefab(stm, cfg.start.rl(), false);

        if (cfg.exit != null) {
            for (PocketPrefabConfig.Entry e : cfg.exit) {
                if (e == null || e.id == null) continue;
                exits.add(new WeightedPrefab(loadPrefab(stm, e.rl(), true), Math.max(1, e.weight)));
            }
        }

        addWeighted(stm, halls, cfg.halls, false);
        addWeighted(stm, rooms, cfg.rooms, false);
        addWeighted(stm, deadEnds, cfg.deadEnds, false);
        addWeighted(stm, traps, cfg.traps, false);

        loaded = true;

        System.out.println("[SCPMod] PocketPrefabLibrary loaded:"
                + " start=" + start.id
                + " exits=" + exits.size()
                + " halls=" + halls.size()
                + " rooms=" + rooms.size()
                + " deadEnds=" + deadEnds.size()
                + " traps=" + traps.size());
    }

    private static void addWeighted(StructureTemplateManager stm,
                                    List<WeightedPrefab> out,
                                    @Nullable List<PocketPrefabConfig.Entry> entries,
                                    boolean isExit) {
        if (entries == null) return;
        for (PocketPrefabConfig.Entry e : entries) {
            if (e == null || e.id == null) continue;
            out.add(new WeightedPrefab(loadPrefab(stm, e.rl(), isExit), Math.max(1, e.weight)));
        }
    }

    private static PocketPrefab loadPrefab(StructureTemplateManager stm, ResourceLocation id, boolean isExit) {
        Optional<StructureTemplate> opt = stm.get(id);
        if (opt.isEmpty()) {
            throw new IllegalStateException("Missing structure template for " + id
                    + " (expected at data/<namespace>/structures/" + id.getPath() + ".nbt)");
        }
        PocketPrefab p = new PocketPrefab(id, opt.get(), isExit);
        System.out.println("[SCPMod] Prefab " + id + " connectors=" + p.connectors.size() + " " + p.connectors);
        return p;
    }

    public static BlockPos getDefaultStartSpawn(BlockPos startOrigin) {
        // Spawn roughly inside the room; tweak as needed for your template
        return startOrigin.offset(6, 1, 6);
    }

    // ---------- weighted picking ----------

    public static PocketPrefab pickExit(net.minecraft.util.RandomSource rand) {
        return pickWeighted(rand, exits);
    }
    public static PocketPrefab pickHall(net.minecraft.util.RandomSource rand) {
        return pickWeighted(rand, halls);
    }
    public static PocketPrefab pickRoom(net.minecraft.util.RandomSource rand) {
        return pickWeighted(rand, rooms);
    }
    public static PocketPrefab pickDeadEnd(net.minecraft.util.RandomSource rand) {
        return pickWeighted(rand, deadEnds);
    }
    public static @Nullable PocketPrefab pickTrapOrNull(net.minecraft.util.RandomSource rand) {
        if (traps.isEmpty()) return null;
        return pickWeighted(rand, traps);
    }

    private static PocketPrefab pickWeighted(net.minecraft.util.RandomSource rand, List<WeightedPrefab> list) {
        if (list.isEmpty()) throw new IllegalStateException("Tried to pick from empty weighted list.");
        int total = 0;
        for (WeightedPrefab wp : list) total += wp.weight();
        int roll = rand.nextInt(total);
        int acc = 0;
        for (WeightedPrefab wp : list) {
            acc += wp.weight();
            if (roll < acc) return wp.prefab();
        }
        return list.get(list.size() - 1).prefab(); // fallback
    }

    // ---------- getters ----------

    public static PocketPrefab getStart() {
        if (start == null) throw new IllegalStateException("PocketPrefabLibrary not loaded");
        return start;
    }

    public static boolean hasExits() { return !exits.isEmpty(); }
    public static boolean hasDeadEnds() { return !deadEnds.isEmpty(); }
    public static boolean hasRooms() { return !rooms.isEmpty(); }
    public static boolean hasHalls() { return !halls.isEmpty(); }

    public record WeightedPrefab(PocketPrefab prefab, int weight) {}

    private PocketPrefabLibrary() {}
}
