package net.luis.scpmod.research;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.progression.unlock.UnlockFlag;
import net.luis.scpmod.progression.unlock.UnlockService;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class ResearchProjects {

    @FunctionalInterface
    public interface Effect {
        void apply(ServerPlayer player);
    }

    /** Data-only definition loaded from JSON */
    public record ProjectDef(
            String id,
            ResearchCategory category,
            String name,
            String desc,
            int cost,
            int sort,
            List<String> requires,
            int tier,
            boolean hiddenUntilPrereq,
            boolean repeatable
    ) {}

    /** Server-authoritative runtime project = JSON def + code effect */
    public record Project(
            String id,
            ResearchCategory category,
            String name,
            String desc,
            int cost,
            List<String> requires,
            Effect effect
    ) {}

    /** Lightweight DTO for syncing to client */
    public record ProjectInfo(
            String id,
            ResearchCategory category,
            String name,
            String desc,
            int cost,
            int sort,
            List<String> requires,
            int tier,
            boolean hiddenUntilPrereq,
            boolean repeatable
    ) {}

    private static Map<String, ProjectDef> LOADED_DEFS = new LinkedHashMap<>();

    private static final Map<String, Effect> EFFECTS = new HashMap<>();
    private static final Set<String> WARNED_NO_EFFECT = new HashSet<>();

    static {
        EFFECTS.put("facility_access", player -> UnlockService.grant(player, UnlockFlag.TAB_FACILITY));

        EFFECTS.put("field_scanner_upgrade_1", player -> {
            // TODO later: set a player flag / upgrade level.
        });

        EFFECTS.put("reinforced_blocks_1", player -> {
            // TODO later: unlock recipes/tags permission
        });

        EFFECTS.put("job_assignment_terminal", player -> {
            // TODO later: unlock staff UI / block / functionality
        });
    }

    /** Called by the JSON reload listener */
    public static void setLoadedDefinitions(Map<String, ProjectDef> defs) {
        LOADED_DEFS = new LinkedHashMap<>(defs);
    }

    public static Project get(String id) {
        ProjectDef d = LOADED_DEFS.get(id);
        if (d == null) return null;

        Effect fx = EFFECTS.get(id);
        if (fx == null) {
            fx = player -> {};
            if (WARNED_NO_EFFECT.add(id)) {
                SCPMod.LOGGER.warn("Research project '{}' has no effect registered (will do nothing on purchase).", id);
            }
        }

        return new Project(d.id(), d.category(), d.name(), d.desc(), d.cost(), d.requires(), fx);
    }

    public static Collection<Project> all() {
        List<Project> out = new ArrayList<>();
        for (String id : LOADED_DEFS.keySet()) {
            Project p = get(id);
            if (p != null) out.add(p);
        }
        return List.copyOf(out);
    }

    public static List<ProjectInfo> getCatalogSnapshot() {
        List<ProjectInfo> out = new ArrayList<>();
        for (ProjectDef d : LOADED_DEFS.values()) {
            out.add(new ProjectInfo(
                    d.id(),
                    d.category(),
                    d.name(),
                    d.desc(),
                    d.cost(),
                    d.sort(),
                    List.copyOf(d.requires()),
                    d.tier(),
                    d.hiddenUntilPrereq(),
                    d.repeatable()
            ));
        }
        return List.copyOf(out);
    }

    public static void validate() {
        for (ProjectDef d : LOADED_DEFS.values()) {
            for (String req : d.requires()) {
                if (!LOADED_DEFS.containsKey(req)) {
                    SCPMod.LOGGER.warn("Research project '{}' requires missing id '{}'", d.id(), req);
                }
            }
        }
    }
}
