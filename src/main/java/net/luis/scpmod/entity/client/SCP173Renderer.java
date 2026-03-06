package net.luis.scpmod.entity.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.SCP173Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SCP173Renderer extends MobRenderer<SCP173Entity, Mat173Model<SCP173Entity>> {

    public SCP173Renderer(EntityRendererProvider.Context context) {
        super(context, new Mat173Model<>(context.bakeLayer(ModModelLayers.SCP_173_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SCP173Entity entity) {
        return ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "textures/entity/173mat.png");
    }

}
