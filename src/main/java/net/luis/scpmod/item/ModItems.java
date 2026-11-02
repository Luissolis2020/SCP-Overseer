package net.luis.scpmod.item;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.ModBlocks;
import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.research.ItemResearchRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SCPMod.MOD_ID);

    public static final RegistryObject<Item> SCP_DOCUMENT = ITEMS.register("scp_document", () -> new SCPDocument(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_500 = ITEMS.register("scp_500", () -> new SCP500(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SCP_427 = ITEMS.register("scp_427", () -> new SCP427(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_1025 = ITEMS.register("scp_1025", () -> new SCP1025(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_668 = ITEMS.register("scp_668", () -> new SCP668(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_207 = ITEMS.register("scp_207", () -> new SCP207(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SCP_207_EMPTY = ITEMS.register("scp_207_empty", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SCP_513 = ITEMS.register("scp_513", () -> new SCP513(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_109 = ITEMS.register("scp_109", () -> new SCP109(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCP_458_Item = ITEMS.register("scp_458", () -> new SCP_458Item(ModBlocks.SCP_458.get(), new Item.Properties()));
    public static final RegistryObject<Item> SCP_458_Slice = ITEMS.register("scp_458_slice", () -> new SCP458_Slice(new Item.Properties().food(ModFoods.SCP_458_SLICE).stacksTo(64)));

    public static final RegistryObject<Item> PDA = ITEMS.register("pda", () -> new PDA(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass", () -> new MagnifyingGlass(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FIELD_SCANNER = ITEMS.register("field_scanner", () -> new FieldScannerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANOMALY_SAMPLE = ITEMS.register("anomaly_sample", () -> new AnomalySampleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SCP_173_SPAWN_EGG = ITEMS.register("scp_173_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_173, 0xede3be, 0xc7b678, new Item.Properties()));
    public static final RegistryObject<Item> SCP_427_1_SPAWN_EGG = ITEMS.register("scp_427_1_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_427_1, 0xf97041, 0x8c2317, new Item.Properties()));
    public static final RegistryObject<Item> SCP_096_SPAWN_EGG = ITEMS.register("scp_096_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_096, 0xdedddd, 0xa1a1a1, new Item.Properties()));
    public static final RegistryObject<Item> SCP_513_1_SPAWN_EGG = ITEMS.register("scp_513_1_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_513_1, 0x202020, 0x535353, new Item.Properties()));
    public static final RegistryObject<Item> SCP_1048_SPAWN_EGG = ITEMS.register("scp_1048_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_1048, 0x875238, 0x935a3e, new Item.Properties()));
    public static final RegistryObject<Item> SCP_2295_SPAWN_EGG = ITEMS.register("scp_2295_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_2295, 0xbdd2a5, 0xf2f2f2, new Item.Properties()));
    public static final RegistryObject<Item> SCP_049_SPAWN_EGG = ITEMS.register("scp_049_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_049, 0x202020, 0xc1c1c1, new Item.Properties()));
    public static final RegistryObject<Item> SCP_106_SPAWN_EGG = ITEMS.register("scp_106_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_106, 0x352929, 0x161916, new Item.Properties()));
    public static final RegistryObject<Item> SCP_999_SPAWN_EGG = ITEMS.register("scp_999_spawn_egg", () -> new DebugSCPSpawnEggItem(ModEntities.SCP_999, 0xd97520, 0xdb7320, new Item.Properties()));

    public static final RegistryObject<Item> BUILDER_NPC_SPAWN_EGG = ITEMS.register("builder_npc_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BUILDER_NPC, 0x4d4d42, 0x99f5f1, new Item.Properties()));


    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_SAPPHIRE = ITEMS.register("raw_sapphire", () -> new Item(new Item.Properties()));

    public static void registerResearchableItems() {
        ItemResearchRegistry.register(SCP_500.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_500"));
        ItemResearchRegistry.register(SCP_427.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_427"));
        ItemResearchRegistry.register(SCP_109.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_109"));
        ItemResearchRegistry.register(SCP_207.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_207"));
        ItemResearchRegistry.register(SCP_1025.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_1025"));
        ItemResearchRegistry.register(SCP_668.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_668"));
        ItemResearchRegistry.register(SCP_513.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_513"));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
