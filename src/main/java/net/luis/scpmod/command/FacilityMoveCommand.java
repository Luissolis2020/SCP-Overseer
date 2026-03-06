package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.luis.scpmod.client.ClientPreviewState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;

public class FacilityMoveCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("move_facility")
                        .then(Commands.argument("direction", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("north").suggest("south").suggest("east")
                                            .suggest("west").suggest("up").suggest("down");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String direction = StringArgumentType.getString(ctx, "direction");
                                    if (!ClientPreviewState.isActive()) {
                                        ctx.getSource().sendFailure(Component.literal("No preview is active."));
                                        return 0;
                                    }

                                    BlockPos current = ClientPreviewState.getPreviewPos();
                                    Vec3i offset = switch (direction.toLowerCase()) {
                                        case "north" -> new Vec3i(0, 0, -1);
                                        case "south" -> new Vec3i(0, 0, 1);
                                        case "east" -> new Vec3i(1, 0, 0);
                                        case "west" -> new Vec3i(-1, 0, 0);
                                        case "up" -> new Vec3i(0, 1, 0);
                                        case "down" -> new Vec3i(0, -1, 0);
                                        default -> {
                                            ctx.getSource().sendFailure(Component.literal("Invalid direction: " + direction));
                                            yield null;
                                        }
                                    };

                                    if (offset == null) return 0;

                                    BlockPos newPos = current.offset(offset);
                                    ClientPreviewState.activate(
                                            newPos,
                                            ClientPreviewState.getTemplate(),
                                            ClientPreviewState.getSettings(),
                                            ClientPreviewState.getStructureId()
                                    );

                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Moved preview " + direction + " to " + newPos),
                                            false
                                    );
                                    return 1;
                                })
                        )
        );
    }
}
