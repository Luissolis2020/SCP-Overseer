package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.facility.building.FacilityBlueprint;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FacilityPlaceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("place_facility_test")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    ServerLevel level = source.getLevel();
                    BlockPos pos = BlockPos.containing(source.getPosition()).offset(-3, 0, -3).above(1);

                    ResourceLocation id = ResourceLocation.parse("scpmod:containment_basic");
                    FacilityBlueprint blueprint = FacilityBlueprintLoader.get(id);
                    if (blueprint == null) {
                        source.sendFailure(Component.literal("Blueprint not found: " + id));
                        return 0;
                    }

                    StructureTemplate template = level.getServer().getStructureManager().getOrCreate(blueprint.getStructureFileLocation());
                    if (template == null) {
                        source.sendFailure(Component.literal("Structure file not found: " + blueprint.getStructureFileLocation()));
                        return 0;
                    }

                    level.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                    source.sendSuccess(() -> Component.literal("Size: " + template.getSize()), false);

                    // Place structure
                    template.placeInWorld(
                            level,
                            pos,
                            pos,
                            new StructurePlaceSettings().setIgnoreEntities(false),
                            level.getRandom(),
                            15
                    );

                    source.sendSuccess(() -> Component.literal("Placed: " + blueprint.displayName), false);
                    return 1;
                }));
    }

}
