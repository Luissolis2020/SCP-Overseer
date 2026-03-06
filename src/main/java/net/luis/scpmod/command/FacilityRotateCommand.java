package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.client.ClientPreviewState;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class FacilityRotateCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("rotate_facility")
                        .executes(ctx -> {
                            if (!ClientPreviewState.isActive()) {
                                Minecraft.getInstance().player.sendSystemMessage(
                                        Component.literal("No active preview to rotate.")
                                );
                                return 0;
                            }

                            ClientPreviewState.rotateClockwise();
                            Minecraft.getInstance().player.sendSystemMessage(
                                    Component.literal("Rotated structure preview.")
                            );
                            return 1;
                        })
        );
    }
}
