package net.luis.scpmod.research;

import com.google.gson.*;
import net.luis.scpmod.SCPMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResearchProjectReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    public ResearchProjectReloadListener() {
        super(GSON, "research_projects"); // folder: data/<modid>/research_projects/*.json
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager rm, ProfilerFiller profiler) {
        Map<String, ResearchProjects.ProjectDef> defs = new LinkedHashMap<>();

        var sorted = map.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .toList();

        for (var entry : sorted) {
            ResourceLocation rl = entry.getKey();
            JsonElement el = entry.getValue();

            try {
                JsonObject obj = GsonHelper.convertToJsonObject(el, "research_project");

                String id = GsonHelper.getAsString(obj, "id");
                ResearchCategory category = ResearchCategory.valueOf(GsonHelper.getAsString(obj, "category"));
                String name = GsonHelper.getAsString(obj, "name");
                String desc = GsonHelper.getAsString(obj, "desc");
                int cost = GsonHelper.getAsInt(obj, "cost", 0);
                int sort = GsonHelper.getAsInt(obj, "sort", 0);

                List<String> requires = new ArrayList<>();
                if (obj.has("requires")) {
                    JsonArray arr = GsonHelper.getAsJsonArray(obj, "requires");
                    for (JsonElement r : arr) requires.add(r.getAsString());
                }

                JsonObject metaObj = obj.has("meta")
                        ? GsonHelper.getAsJsonObject(obj, "meta")
                        : new JsonObject();

                int tier = GsonHelper.getAsInt(metaObj, "tier", 1);
                boolean hidden = GsonHelper.getAsBoolean(metaObj, "hidden_until_prereq", false);
                boolean repeatable = GsonHelper.getAsBoolean(metaObj, "repeatable", false);

                if (defs.containsKey(id)) {
                    throw new IllegalStateException("Duplicate research project id '" + id + "' (from " + rl + ")");
                }

                defs.put(id, new ResearchProjects.ProjectDef(
                        id,
                        category,
                        name,
                        desc,
                        cost,
                        sort,
                        List.copyOf(requires),
                        tier,
                        hidden,
                        repeatable
                ));

            } catch (Exception ex) {
                SCPMod.LOGGER.error("Failed to load research project JSON: {}", rl, ex);
            }
        }

        ResearchProjects.setLoadedDefinitions(defs);
        ResearchProjects.validate();
        SCPMod.LOGGER.info("Loaded {} research projects from JSON", defs.size());
    }
}
