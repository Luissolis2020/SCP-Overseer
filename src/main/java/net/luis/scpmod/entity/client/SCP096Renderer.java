package net.luis.scpmod.entity.client;

import net.luis.scpmod.entity.custom.SCP_096_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCP096Renderer extends GeoEntityRenderer<SCP_096_Entity> {

    public SCP096Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP_096_Model());
    }
}
