package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.network.ConfirmFacilityPlacementPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class FacilityConfirmCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("confirm_facility")
                        .executes(ctx -> {
                            if (!ClientPreviewState.isActive()) {
                                Minecraft.getInstance().player.sendSystemMessage(
                                        Component.literal("No active preview!")
                                );
                                return 0;
                            }
                            SCPModNetwork.sendToServer(new ConfirmFacilityPlacementPacket(
                                    ClientPreviewState.getPreviewPos(),
                                    ClientPreviewState.getStructureId(),
                                    ClientPreviewState.getSettings().getRotation()
                            ));

                            Minecraft.getInstance().player.sendSystemMessage(
                                    Component.literal("Requesting structure confirmation...")
                            );

                            ClientPreviewState.deactivate();

                            return 1;
                        })
        );
    }
}
