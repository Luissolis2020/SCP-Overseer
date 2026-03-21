package net.luis.scpmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;

public class FacilityDebugCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("facility_debug_zones")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    FacilityManager manager = FacilityManager.get(source.getLevel());

                    if (manager.getRooms().isEmpty()) {
                        source.sendSuccess(() -> Component.literal("No active facility rooms."), false);
                        return 1;
                    }

                    for (FacilityRoomInstance room : manager.getRooms()) {
                        source.sendSuccess(() -> Component.literal("Facility: " + room.blueprint.displayName + " at " + room.origin), false);

                        for (var entry : room.getAllZones().entrySet()) {
                            String zoneName = entry.getKey();
                            AABB bounds = entry.getValue();
                            source.sendSuccess(() -> Component.literal(" - Zone: " + zoneName + " -> " + formatBounds(bounds)), false);
                        }
                    }

                    return 1;
                }));

    }

    private static String formatBounds(AABB box) {
        return String.format("(%d, %d, %d) to (%d, %d, %d)",
                (int) box.minX, (int) box.minY, (int) box.minZ,
                (int) box.maxX, (int) box.maxY, (int) box.maxZ);
    }
}
