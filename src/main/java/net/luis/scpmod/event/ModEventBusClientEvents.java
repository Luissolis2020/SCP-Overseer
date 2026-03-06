package net.luis.scpmod.event;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.ModBlocks;
import net.luis.scpmod.entity.client.Mat173Model;
import net.luis.scpmod.entity.client.ModModelLayers;
import net.luis.scpmod.entity.client.SCP_427_1_Model;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SCP_173_LAYER, Mat173Model::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SCP_427_1_LAYER, SCP_427_1_Model::createBodyLayer);
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRATE.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LIGHT_CONTAINMENT_LIGHT.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORRODED_LIGHT.get(), RenderType.cutoutMipped());
        });
    }

}
