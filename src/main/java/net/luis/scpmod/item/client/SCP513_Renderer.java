package net.luis.scpmod.item.client;

import net.luis.scpmod.item.SCP513;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SCP513_Renderer extends GeoItemRenderer<SCP513> {

    public SCP513_Renderer() {
        super(new SCP513_Model());
    }
}
