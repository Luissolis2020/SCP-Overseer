package net.luis.scpmod.entity.client;

import net.luis.scpmod.entity.custom.SCP_2295_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCP_2295Renderer extends GeoEntityRenderer<SCP_2295_Entity> {

    public SCP_2295Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP_2295_Model());
    }
}
