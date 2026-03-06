package net.luis.scpmod.datagen.loot;

import net.luis.scpmod.block.ModBlocks;
import net.luis.scpmod.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
        this.dropSelf(ModBlocks.SCP_124.get());
        this.dropSelf(ModBlocks.SCP_458.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_UPPER.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_MIDDLE.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_LOWER.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_CEILING.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_FLOOR.get());
        this.dropSelf(ModBlocks.LIGHT_CONTAINMENT_LIGHT.get());
        this.dropSelf(ModBlocks.GRATE.get());
        this.add(ModBlocks.CORRODED_CEILING.get(), block -> noDrop());
        this.add(ModBlocks.CORRODED_FLOOR.get(), block -> noDrop());
        this.add(ModBlocks.CORRODED_LOWER.get(), block -> noDrop());
        this.add(ModBlocks.CORRODED_MIDDLE.get(), block -> noDrop());
        this.add(ModBlocks.CORRODED_UPPER.get(), block -> noDrop());
        this.add(ModBlocks.CORRODED_LIGHT.get(), block -> noDrop());
//        this.add(ModBlocks.CORRODED_SLAB.get(), block -> noDrop());
//        this.add(ModBlocks.CORRODED_STAIRS.get(), block -> noDrop());
        this.dropSelf(ModBlocks.RESEARCH_TABLE.get());

        this.add(ModBlocks.POCKET_CONNECTOR_BLOCK.get(), block -> noDrop());
        this.add(ModBlocks.POCKET_EXIT_BLOCK.get(), block -> noDrop());


        this.add(ModBlocks.SAPPHIRE_ORE.get(), block -> createCopperLikeOreDrops(ModBlocks.SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
