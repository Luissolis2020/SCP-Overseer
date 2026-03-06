package net.luis.scpmod.datagen;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SCPMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SAPPHIRE_BLOCK);
        blockWithItem(ModBlocks.RAW_SAPPHIRE_BLOCK);

        blockWithItem(ModBlocks.SAPPHIRE_ORE);

        blockWithItem(ModBlocks.SCP_124);

//        stairsBlock(((StairBlock) ModBlocks.CORRODED_STAIRS.get()), blockTexture(ModBlocks.CORRODED_FLOOR.get()));
//        slabBlock(((SlabBlock) ModBlocks.CORRODED_SLAB.get()), blockTexture(ModBlocks.CORRODED_FLOOR.get()), blockTexture(ModBlocks.CORRODED_FLOOR.get()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
