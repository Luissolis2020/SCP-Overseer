package net.luis.scpmod.item;

import net.luis.scpmod.client.BuildPreviewState;
import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.pda_gui.screens.PDAMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;

public class PDA extends Item {

    public PDA(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            if (ClientPreviewState.isActive()) {
                if (ClientPreviewState.getPreviewState() == BuildPreviewState.FREE_VIEW) {
                    ClientPreviewState.setPreviewState(BuildPreviewState.PLACEMENT_LOCKED);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }
            Minecraft.getInstance().setScreen(new PDAMenuScreen());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
