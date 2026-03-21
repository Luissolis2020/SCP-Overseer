package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.facility.FacilityOverlapChecker;
import net.luis.scpmod.network.ActivateFacilityPreviewPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FacilityPreviewCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("preview_facility")
                        .then(Commands.argument("structure", ResourceLocationArgument.id())
                                .suggests(StructureIdSuggestionProvider.INSTANCE)
                                .executes(ctx -> {
                                    ServerLevel level = ctx.getSource().getLevel();
                                    BlockPos pos = BlockPos.containing(ctx.getSource().getPosition());
                                    ResourceLocation structureId = ResourceLocationArgument.getId(ctx, "structure");

                                    StructureTemplate template = level.getServer()
                                            .getStructureManager()
                                            .getOrCreate(structureId);
                                    StructurePlaceSettings settings = new StructurePlaceSettings();

                                    if (FacilityOverlapChecker.isOverlapping(level, pos, template, settings)) {
                                        ctx.getSource().sendFailure(Component.literal("Preview blocked: overlaps another structure."));
                                        return 0;
                                    }

                                    SCPModNetwork.sendToPlayer(
                                            new ActivateFacilityPreviewPacket(pos, structureId),
                                            ctx.getSource().getPlayer()
                                    );

                                    return 1;
                                }))
        );
    }
}
