package net.luis.scpmod.research;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockResearchRegistry {

    private static final Map<Block, ResourceLocation> BLOCK_TO_RESEARCH_ID = new HashMap<>();

    public static void register(Block block, ResourceLocation id) {
        BLOCK_TO_RESEARCH_ID.put(block, id);
    }

    public static ResourceLocation getResearchId(Block block) {
        return BLOCK_TO_RESEARCH_ID.get(block);
    }

    public static boolean isResearchable(Block block) {
        return BLOCK_TO_RESEARCH_ID.containsKey(block);
    }
}
