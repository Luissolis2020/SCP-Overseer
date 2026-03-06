package net.luis.scpmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.scpmod.entity.custom.CursedPlayer;
import net.luis.scpmod.entity.custom.SCP_513_1_Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SCP_513_1Renderer extends GeoEntityRenderer<SCP_513_1_Entity> {
    public SCP_513_1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP_513_1_Model());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public void render(SCP_513_1_Entity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            boolean isCursed = CursedPlayer.isCursed(player);

            if (!isCursed) {
                return;
            }
        }

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }


}
