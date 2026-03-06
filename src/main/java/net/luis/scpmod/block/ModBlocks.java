package net.luis.scpmod.block;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.entity.CorrodedLightBlock;
import net.luis.scpmod.block.entity.LightContainmentLightBlock;
import net.luis.scpmod.item.ModItems;
import net.luis.scpmod.pocket.PocketConnectorBlock;
import net.luis.scpmod.pocket.PocketExitTriggerBlock;
import net.luis.scpmod.research.BlockResearchRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SCPMod.MOD_ID);

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock("sapphire_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> RAW_SAPPHIRE_BLOCK = registerBlock("raw_sapphire_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock(("sapphire_ore"),
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(2f).requiresCorrectToolForDrops(), UniformInt.of(3,6)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_LOWER = registerBlock("light_containment_lower",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_MIDDLE = registerBlock("light_containment_middle",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_UPPER = registerBlock("light_containment_upper",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_FLOOR = registerBlock("light_containment_floor",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_CEILING = registerBlock("light_containment_ceiling",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> LIGHT_CONTAINMENT_LIGHT = registerBlock("light_containment_light",
            LightContainmentLightBlock::new);
    public static final RegistryObject<Block> CORRODED_CEILING = registerBlock("corroded_ceiling",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CORRODED_FLOOR = registerBlock("corroded_floor",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CORRODED_LOWER = registerBlock("corroded_lower",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CORRODED_MIDDLE = registerBlock("corroded_middle",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CORRODED_UPPER = registerBlock("corroded_upper",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final RegistryObject<Block> CORRODED_LIGHT = registerBlock("corroded_light",
            CorrodedLightBlock::new);

    public static final RegistryObject<Block> RESEARCH_TABLE = registerBlock("research_table",
            () -> new ResearchTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).strength(2.0f)));

//    public static final RegistryObject<Block> CORRODED_STAIRS = registerBlock("corroded_stairs",
//            () -> new StairBlock(() -> ModBlocks.CORRODED_FLOOR.get().defaultBlockState(),
//                    BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
//    public static final RegistryObject<Block> CORRODED_SLAB = registerBlock("corroded_slab",
//            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BEDROCK).strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> POCKET_CONNECTOR_BLOCK = registerBlock("pocket_connector",
            PocketConnectorBlock::new);
    public static final RegistryObject<Block> POCKET_EXIT_BLOCK = registerBlock("pocket_exit_block", PocketExitTriggerBlock::new);

    public static final RegistryObject<Block> GRATE = registerBlock("grate",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(1.5f)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isValidSpawn((state, level, pos, entityType) -> false)
            )
    );

    public static final RegistryObject<Block> SCP_124 = registerBlock(("scp_124"),
            () -> new SCP_124(BlockBehaviour.Properties.of().randomTicks().strength(1.5F).sound(SoundType.GRASS)));
    public static final RegistryObject<Block> SCP_458 = BLOCKS.register("scp_458", () -> new SCP_458(BlockBehaviour.Properties.of().strength(1f)));

    public static void registerResearchableBlocks() {
        BlockResearchRegistry.register(SCP_124.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_124"));
        BlockResearchRegistry.register(SCP_458.get(), ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_458"));
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
