package net.luis.scpmod.pocket;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class StripPocketConnectorProcessor extends StructureProcessor {
    public static final StripPocketConnectorProcessor INSTANCE = new StripPocketConnectorProcessor();
    public static final Codec<StripPocketConnectorProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo process(
            LevelReader level, BlockPos jigsawOrigin, BlockPos pivot,
            StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current,
            StructurePlaceSettings settings, StructureTemplate template) {

        if (current.state().getBlock() instanceof PocketConnectorBlock) {
            return new StructureTemplate.StructureBlockInfo(current.pos(), Blocks.AIR.defaultBlockState(), null);
        }
        return current;
    }

    @Override public StructureProcessorType<?> getType() {
        return SCPProcessors.STRIP_POCKET_CONNECTOR.get();
    }
}