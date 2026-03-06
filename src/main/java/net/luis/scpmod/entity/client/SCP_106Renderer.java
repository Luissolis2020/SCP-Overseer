package net.luis.scpmod.entity.client;

import net.luis.scpmod.entity.custom.SCP_106_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SCP_106Renderer extends GeoEntityRenderer<SCP_106_Entity> {

    public SCP_106Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP_106_Model());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
