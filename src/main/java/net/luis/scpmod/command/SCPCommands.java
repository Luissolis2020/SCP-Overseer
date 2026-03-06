package net.luis.scpmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.SCPMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID)
public class SCPCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("resetmutation").requires(source -> source.hasPermission(2))
                .executes(context -> {
                    CommandSourceStack source = context.getSource();
                    if (source.getEntity() instanceof ServerPlayer player) {
                        player.getPersistentData().putInt("scp_427_mutation", 0);
                        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal("Mutation level reset to 0!"), true);
                        return Command.SINGLE_SUCCESS;
                    }
                    return 0;
                })
        );

        GenerateBlueprintCommand.register(dispatcher);
        FacilityDebugCommand.register(dispatcher);
    }
}

