package net.luis.scpmod.pocket;

import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class PocketPrefab {

    public record Connector(BlockPos local, Direction facing, PocketConnectorBlock.ConnectorType type) {}

    public final ResourceLocation id;
    public final StructureTemplate template;
    public final List<Connector> connectors;
    public final boolean isExit;

    public PocketPrefab(ResourceLocation id, StructureTemplate template, boolean isExit) {
        this.id = id;
        this.template = template;
        this.isExit = isExit;
        this.connectors = Collections.unmodifiableList(extract(template));
    }

    private static List<Connector> extract(StructureTemplate template) {
        List<Connector> out = new ArrayList<>();
        StructureTemplateAccessor acc = (StructureTemplateAccessor) (Object) template;

        // Deduplicate connectors across palettes
        HashSet<BlockPos> seen = new HashSet<>();

        for (StructureTemplate.Palette pal : acc.getPalettes()) {
            for (StructureTemplate.StructureBlockInfo info : pal.blocks()) {
                if (!(info.state().getBlock() instanceof PocketConnectorBlock)) continue;

                if (!seen.add(info.pos())) continue;

                var st = info.state();
                out.add(new Connector(
                        info.pos(),
                        st.getValue(PocketConnectorBlock.FACING),
                        st.getValue(PocketConnectorBlock.TYPE)
                ));
            }
        }
        return out;
    }


    public BlockPos size() {
        var sz = template.getSize();
        return new BlockPos(sz.getX(), sz.getY(), sz.getZ());
    }
}
