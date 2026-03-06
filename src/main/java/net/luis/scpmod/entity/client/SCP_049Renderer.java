package net.luis.scpmod.entity.client;

import net.luis.scpmod.entity.custom.SCP_049_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SCP_049Renderer extends GeoEntityRenderer<SCP_049_Entity> {
    public SCP_049Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP_049_Model());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
