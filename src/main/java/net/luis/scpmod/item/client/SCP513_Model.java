package net.luis.scpmod.item.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.item.SCP513;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class SCP513_Model extends DefaultedItemGeoModel<SCP513> {

    public SCP513_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_513"));
    }
}
