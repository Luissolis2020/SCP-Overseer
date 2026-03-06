package net.luis.scpmod.block.client;

import net.luis.scpmod.block.entity.SCP_458_Entity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SCP_458Renderer extends GeoBlockRenderer<SCP_458_Entity> {

    public SCP_458Renderer(BlockEntityRendererProvider.Context context) {
        super(new SCP_458_Model());
    }
}
