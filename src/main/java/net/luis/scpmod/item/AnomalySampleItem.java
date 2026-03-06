package net.luis.scpmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AnomalySampleItem extends Item {
    public static final String TAG_TIER = "Tier";

    public AnomalySampleItem(Properties props) {
        super(props);
    }

    public static ItemStack ofTier(Item item, int tier) {
        ItemStack stack = new ItemStack(item);
        stack.getOrCreateTag().putInt(TAG_TIER, tier);
        return stack;
    }

    public static int getTier(ItemStack stack) {
        if (!stack.hasTag()) return 1;
        return Math.max(1, stack.getTag().getInt(TAG_TIER));
    }
}

