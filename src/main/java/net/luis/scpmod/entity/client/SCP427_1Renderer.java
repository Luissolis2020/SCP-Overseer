package net.luis.scpmod.entity.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.SCP427_1_Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SCP427_1Renderer extends MobRenderer<SCP427_1_Entity, SCP_427_1_Model<SCP427_1_Entity>> {

    public SCP427_1Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP_427_1_Model<>(context.bakeLayer(ModModelLayers.SCP_427_1_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SCP427_1_Entity scp4271Entity) {
        return ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "textures/entity/scp_427_1.png");
    }
}
