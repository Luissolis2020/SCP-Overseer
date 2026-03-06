package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

public class ClientResearchData {
    private static final Map<ResourceLocation, ResearchStage> researchStages = new HashMap<>();

    public static void setAllStages(Map<ResourceLocation, ResearchStage> newStages) {
        researchStages.clear();
        researchStages.putAll(newStages);
    }

    public static ResearchStage getStage(ResourceLocation id) {
        return researchStages.getOrDefault(id, ResearchStage.UNDISCOVERED);
    }

    public static boolean isDiscovered(ResourceLocation id) {
        return getStage(id).ordinal() >= ResearchStage.DISCOVERED.ordinal();
    }
}
