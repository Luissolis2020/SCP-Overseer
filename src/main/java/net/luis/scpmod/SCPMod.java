package net.luis.scpmod;

import com.mojang.logging.LogUtils;
import net.luis.scpmod.block.ModBlocks;
import net.luis.scpmod.block.client.SCP_458Renderer;
import net.luis.scpmod.block.entity.ModBlockEntities;
import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.entity.client.*;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.luis.scpmod.item.ModCreativeModTabs;
import net.luis.scpmod.item.ModItems;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.progression.mission.MissionReloadListener;
import net.luis.scpmod.pocket.PocketPrefabConfigLoader;
import net.luis.scpmod.pocket.SCPProcessors;
import net.luis.scpmod.research.ResearchProjectReloadListener;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SCPMod.MOD_ID)
public class SCPMod {
    public static final String MOD_ID = "scpmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SCPMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);

        SCPProcessors.init(modEventBus);
        modEventBus.addListener(this::commonSetup);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
    }


    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new FacilityBlueprintLoader());
        event.addListener(new PocketPrefabConfigLoader());
        event.addListener(new ResearchProjectReloadListener());
        event.addListener(new MissionReloadListener());

        System.out.println("[SCPMod] Registered FacilityBlueprintLoader + PocketPrefabConfigLoader + ResearchProjectReloadListener + MissionReloadListener.");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SCPModNetwork.registerPackets();
            ModItems.registerResearchableItems();
            ModBlocks.registerResearchableBlocks();
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.SCP_173.get(), SCP173Renderer::new);
            EntityRenderers.register(ModEntities.SCP_427_1.get(), SCP427_1Renderer::new);
            EntityRenderers.register(ModEntities.SCP_096.get(), SCP096Renderer::new);
            EntityRenderers.register(ModEntities.SCP_513_1.get(), SCP_513_1Renderer::new);
            EntityRenderers.register(ModEntities.SCP_1048.get(), SCP_1048Renderer::new);
            EntityRenderers.register(ModEntities.SCP_2295.get(), SCP_2295Renderer::new);
            EntityRenderers.register(ModEntities.SCP_049.get(), SCP_049Renderer::new);
            EntityRenderers.register(ModEntities.SCP_106.get(), SCP_106Renderer::new);
            EntityRenderers.register(ModEntities.SCP_999.get(), SCP_999Renderer::new);

            EntityRenderers.register(ModEntities.BUILDER_NPC.get(), BuilderNPCRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.SCP_458_Entity.get(), SCP_458Renderer::new);
            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCP_458.get(), RenderType.cutout());
        }
    }
}
