package net.luis.scpmod.entity.client;

import net.luis.scpmod.entity.custom.SCP_1048_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCP_1048Renderer extends GeoEntityRenderer<SCP_1048_Entity> {

    public SCP_1048Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP_1048_Model());
    }
}
