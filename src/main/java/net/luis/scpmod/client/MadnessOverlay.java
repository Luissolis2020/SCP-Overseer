package net.luis.scpmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.luis.scpmod.SCPMod;
import net.luis.scpmod.capability.MadnessProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MadnessOverlay {
    private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "textures/gui/madness_overlay.png");

    @SubscribeEvent
    public static void renderMadnessOverlay(RenderGuiOverlayEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int madnessLevel = MadnessProvider.get(player).getMadnessLevel();
        if (madnessLevel > 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            float alpha = 0.01F * madnessLevel; // Lowered for subtlety (max = 0.25)
            event.getGuiGraphics().setColor(1.0F, 1.0F, 1.0F, alpha);

            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            event.getGuiGraphics().blit(OVERLAY_TEXTURE, 0, 0, screenWidth, screenHeight, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

            RenderSystem.disableBlend();
        }
    }
}
