package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;

public interface ResearchTargetProvider {
    ResourceLocation getResearchId();
    String getResearchDisplayName();
    ResearchEntry.EntryType getEntryType();
}
