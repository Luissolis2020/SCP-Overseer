package net.luis.scpmod.entity;

import net.luis.scpmod.research.ResearchEntry;
import net.luis.scpmod.research.ResearchTargetProvider;
import net.minecraft.resources.ResourceLocation;

public interface AnomalyEntity extends ResearchTargetProvider {

    ResourceLocation getResearchId();
    String getAnomalyDisplayName();

    @Override
    default String getResearchDisplayName() {
        return getAnomalyDisplayName();
    }

    @Override
    default ResearchEntry.EntryType getEntryType() {
        return ResearchEntry.EntryType.ENTITY;
    }
}

