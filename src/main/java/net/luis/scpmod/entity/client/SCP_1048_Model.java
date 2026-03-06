package net.luis.scpmod.entity.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.SCP_1048_Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SCP_1048_Model extends DefaultedEntityGeoModel<SCP_1048_Entity> {

    public SCP_1048_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_1048"));
    }

    @Override
    public ResourceLocation getTextureResource(SCP_1048_Entity animatable) {
        if (animatable.hasMaterialTexture()) {
            return animatable.getMaterialTexture();
        }

        return super.getTextureResource(animatable);
    }
}
