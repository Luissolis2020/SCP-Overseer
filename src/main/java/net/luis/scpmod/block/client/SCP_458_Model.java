package net.luis.scpmod.block.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.entity.SCP_458_Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SCP_458_Model extends DefaultedBlockGeoModel<SCP_458_Entity> {

    public SCP_458_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_458"));
    }
}
