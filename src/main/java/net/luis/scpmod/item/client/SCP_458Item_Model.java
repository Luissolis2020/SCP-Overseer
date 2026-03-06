package net.luis.scpmod.item.client;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.item.SCP_458Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SCP_458Item_Model extends DefaultedBlockGeoModel<SCP_458Item> {
    public SCP_458Item_Model() {
        super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_458"));
    }
}
