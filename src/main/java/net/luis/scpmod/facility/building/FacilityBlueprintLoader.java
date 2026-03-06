package net.luis.scpmod.facility.building;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

public class FacilityBlueprintLoader extends SimpleJsonResourceReloadListener {
    public static final Map<ResourceLocation, FacilityBlueprint> BLUEPRINTS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().create();

    public FacilityBlueprintLoader() {
        super(GSON, "blueprints");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectMap, ResourceManager manager, ProfilerFiller profiler) {
        System.out.println("[SCPMod] Blueprint entries in objectMap: " + objectMap.keySet());
        BLUEPRINTS.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectMap.entrySet()) {
            try {
                FacilityBlueprint blueprint = GSON.fromJson(entry.getValue(), FacilityBlueprint.class);
                blueprint.id = entry.getKey();
                blueprint.resolveMaterials();
                blueprint.resolveConnectors();
                BLUEPRINTS.put(entry.getKey(), blueprint);

                System.out.println("[SCPMod] Loaded blueprint: " + blueprint.displayName + " (" + entry.getKey() + "), connectors=" + blueprint.getConnectors().size());
            } catch (Exception e) {
                System.err.println("[SCPMod] Failed to load blueprint " + entry.getKey() + ": " + e.getMessage());
            }
        }
        System.out.println("[SCPMod] Total blueprints loaded: " + BLUEPRINTS.size());
    }

    public static FacilityBlueprint get(ResourceLocation id) { return BLUEPRINTS.get(id); }
}
