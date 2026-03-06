package net.luis.scpmod.event;

import net.luis.scpmod.SCPMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID)
public class PlayerSleepEventHandler {
    private static final String USAGE_COUNT_TAG = "scp_500_usage";

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!event.wakeImmediately()) {
                CompoundTag playerData = player.getPersistentData();

                if (playerData.contains(USAGE_COUNT_TAG)) {
                    playerData.putInt(USAGE_COUNT_TAG, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void allowForcedSleep(SleepingTimeCheckEvent event) {
        Player player = event.getEntity();

        if (player.getPersistentData().getBoolean("scp2295_force_sleep")) {
            event.setResult(Event.Result.ALLOW);  // Allows sleeping anytime (even daytime)
        }
    }

    @SubscribeEvent
    public static void preventEarlyWakeup(PlayerSleepInBedEvent event) {
        Player player = event.getEntity();

        if (player.getPersistentData().getBoolean("scp2295_force_sleep")) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM); // Prevents wake-up
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BedBlock) {
            if (world.isNight() && !player.getPersistentData().getBoolean("scp2295_force_sleep")) {
                event.setCanceled(true);
            }
        }
    }

}
