package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;

public class SCPBlockEntry extends ResearchEntry {
    public SCPBlockEntry(ResourceLocation id, boolean visibleByDefault, Classification classification) {
        super(id, visibleByDefault, classification);
    }

    @Override
    public EntryType getType() {
        return EntryType.BLOCK;
    }
}
