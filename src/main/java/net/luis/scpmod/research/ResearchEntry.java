package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public abstract class ResearchEntry {
    protected final ResourceLocation id;
    protected final boolean visibleByDefault;
    protected final Classification classification;
    protected final Map<ResearchStage, String> stageDescriptions = new EnumMap<>(ResearchStage.class);

    public ResearchEntry(ResourceLocation id, boolean visibleByDefault, Classification classification) {
        this.id = id;
        this.visibleByDefault = visibleByDefault;
        this.classification = classification;
    }

    public void setDescription(ResearchStage stage, String description) {
        stageDescriptions.put(stage, description);
    }

    public String getDescription(ResearchStage stage) {
        return stageDescriptions.getOrDefault(stage, "No information available.");
    }

    public String getDisplayName(ResearchStage stage) {
        if (stage == ResearchStage.UNDISCOVERED) {
            return "???";
        }
        String raw = id.getPath().toUpperCase();
        return raw.replace('_', '-');
    }

    public ResourceLocation getId() {
        return id;
    }

    public boolean isVisibleByDefault() {
        return visibleByDefault;
    }

    public Classification getClassification() {
        return classification;
    }

    public abstract EntryType getType();

    public enum EntryType {
        ENTITY,
        ITEM,
        BLOCK
    }
}
