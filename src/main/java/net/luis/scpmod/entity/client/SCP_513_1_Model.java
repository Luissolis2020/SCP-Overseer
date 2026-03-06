package net.luis.scpmod.entity.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.SCP_513_1_Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SCP_513_1_Model extends DefaultedEntityGeoModel<SCP_513_1_Entity> {
    public SCP_513_1_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_513_1"));
    }
}
