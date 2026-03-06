package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;

public class SCPItemEntry extends ResearchEntry {
    public SCPItemEntry(ResourceLocation id, boolean visibleByDefault, Classification classification) {
        super(id, visibleByDefault, classification);
    }

    @Override
    public EntryType getType() {
        return EntryType.ITEM;
    }
}
