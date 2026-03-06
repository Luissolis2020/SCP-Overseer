package net.luis.scpmod.entity.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.SCP_106_Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SCP_106_Model extends DefaultedEntityGeoModel<SCP_106_Entity> {

    public SCP_106_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_106"));
    }
}
