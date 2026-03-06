package net.luis.scpmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SCPDocument extends Item {

    public SCPDocument(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand){
        if (!world.isClientSide) {
            player.sendSystemMessage(Component.literal("You read the SCP Document..."));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
