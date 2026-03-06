package net.luis.scpmod.item;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SCPMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SCP_TAB = CREATIVE_MODE_TABS.register("scp_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SCP_DOCUMENT.get()))
                    .title(Component.translatable("creativetab.scp_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.PDA.get());
                        pOutput.accept(ModItems.MAGNIFYING_GLASS.get());
                        pOutput.accept(ModItems.FIELD_SCANNER.get());
                        pOutput.accept(ModItems.ANOMALY_SAMPLE.get());
                        pOutput.accept(ModBlocks.RESEARCH_TABLE.get());
                        pOutput.accept(ModItems.SCP_DOCUMENT.get());
                        pOutput.accept(ModItems.SCP_109.get());
                        pOutput.accept(ModBlocks.SCP_124.get());
                        pOutput.accept(ModItems.SCP_207.get());
                        pOutput.accept(ModItems.SCP_207_EMPTY.get());
                        pOutput.accept(ModItems.SCP_427.get());
                        pOutput.accept(ModItems.SCP_458_Item.get());
                        pOutput.accept(ModItems.SCP_458_Slice.get());
                        pOutput.accept(ModItems.SCP_500.get());
                        pOutput.accept(ModItems.SCP_513.get());
                        pOutput.accept(ModItems.SCP_668.get());
                        pOutput.accept(ModItems.SCP_1025.get());
                        pOutput.accept(ModItems.SCP_049_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_096_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_106_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_173_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_427_1_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_513_1_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_999_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_1048_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SCP_2295_SPAWN_EGG.get());
                        pOutput.accept(ModItems.BUILDER_NPC_SPAWN_EGG.get());
                        pOutput.accept(ModItems.SAPPHIRE.get());
                        pOutput.accept(ModItems.RAW_SAPPHIRE.get());

                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_LOWER.get());
                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_MIDDLE.get());
                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_UPPER.get());
                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_FLOOR.get());
                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_CEILING.get());
                        pOutput.accept(ModBlocks.LIGHT_CONTAINMENT_LIGHT.get());
                        pOutput.accept(ModBlocks.GRATE.get());

                        pOutput.accept(ModBlocks.CORRODED_CEILING.get());
                        pOutput.accept(ModBlocks.CORRODED_FLOOR.get());
                        pOutput.accept(ModBlocks.CORRODED_LOWER.get());
                        pOutput.accept(ModBlocks.CORRODED_MIDDLE.get());
                        pOutput.accept(ModBlocks.CORRODED_UPPER.get());
//                        pOutput.accept(ModBlocks.CORRODED_STAIRS.get());
//                        pOutput.accept(ModBlocks.CORRODED_SLAB.get());
                        pOutput.accept(ModBlocks.CORRODED_LIGHT.get());

                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
                        pOutput.accept(ModBlocks.SAPPHIRE_ORE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
