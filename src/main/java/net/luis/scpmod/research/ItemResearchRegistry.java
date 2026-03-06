package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemResearchRegistry {

    private static final Map<Item, ResourceLocation> ITEM_TO_RESEARCH_ID = new HashMap<>();

    public static void register(Item item, ResourceLocation id) {
        ITEM_TO_RESEARCH_ID.put(item, id);
    }

    public static ResourceLocation getResearchId(Item item) {
        return ITEM_TO_RESEARCH_ID.get(item);
    }

    public static boolean isResearchable(Item item) {
        return ITEM_TO_RESEARCH_ID.containsKey(item);
    }
}
