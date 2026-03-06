package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;

public class SCPResearchEntry extends ResearchEntry {

    public SCPResearchEntry(ResourceLocation id, boolean visibleByDefault, Classification classification) {
        super(id, visibleByDefault, classification);
    }

    @Override
    public EntryType getType() {
        return EntryType.ENTITY;
    }
}

