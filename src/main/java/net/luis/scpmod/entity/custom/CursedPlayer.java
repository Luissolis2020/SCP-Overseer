package net.luis.scpmod.entity.custom;

import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.network.SyncCursedPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CursedPlayer {
    private static final String CURSED_TAG = "scp513_cursed";
    private static final String CURSE_TIMER_TAG = "scp513_curse_timer";
    private static final EntityDataAccessor<Boolean> REAL_SPAWNED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

    public static boolean hasRealSpawned(Player player) {
        if (!player.getEntityData().hasItem(REAL_SPAWNED)) {
            return false;
        }
        return player.getEntityData().get(REAL_SPAWNED);
    }

    public static void setRealSpawned(Player player, boolean spawned) {
        if (!player.getEntityData().hasItem(REAL_SPAWNED)) {
            player.getEntityData().define(REAL_SPAWNED, false);
        }
        player.getEntityData().set(REAL_SPAWNED, spawned);
    }

    public static boolean isCursed(Player player) {
        return player.getPersistentData().getBoolean(CURSED_TAG);
    }

    public static void setCursed(Player player, boolean value) {
        player.getPersistentData().putBoolean(CURSED_TAG, value);
    }

    public static int getCurseTimer(Player player) {
        return player.getPersistentData().getInt(CURSE_TIMER_TAG);
    }

    public static void setCurseTimer(Player player, int time) {
        player.getPersistentData().putInt(CURSE_TIMER_TAG, time);
    }

    private static BlockPos findSafeSpawnPos(Level level, BlockPos origin, int minDistance, int maxDistance) {
        Random random = new Random();

        for (int attempts = 10; attempts > 0; attempts--) { // Try 10 times
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = minDistance + random.nextInt(maxDistance - minDistance);

            int xOffset = (int) (Math.cos(angle) * distance);
            int zOffset = (int) (Math.sin(angle) * distance);

            BlockPos potentialPos = origin.offset(xOffset, 0, zOffset);
            int groundY = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, potentialPos).getY();
            BlockPos finalPos = new BlockPos(potentialPos.getX(), groundY, potentialPos.getZ());

            if (level.getBlockState(finalPos).isAir() && level.getBlockState(finalPos.above()).isAir()) {
                return finalPos;
            }
        }

        return origin;
    }

    public static void spawnSCP5131(Player player, boolean isKiller, int minDistance, int maxDistance) {
        Level level = player.level();

        if (!level.isClientSide) {
            SCP_513_1_Entity scp = ModEntities.SCP_513_1.get().create(level);
            if (scp != null) {
                BlockPos safePos = findSafeSpawnPos(level, player.blockPosition(), minDistance, maxDistance);
                scp.moveTo(safePos.getX(), safePos.getY(), safePos.getZ());
                scp.facePlayer(player);

                scp.setReal(isKiller);

                level.playSound(null, scp.getX(), scp.getY(), scp.getZ(),
                        SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.HOSTILE, 1.0F, 0.8F);

                level.addFreshEntity(scp);
            }
        }
    }

    public static void cureWithSCP500(Player player) {
        if (isCursed(player)) {
            setCursed(player, false);
            setCurseTimer(player, 0);
            setRealSpawned(player, false);

            if (player instanceof ServerPlayer serverPlayer) {
                SCPModNetwork.sendToPlayer(new SyncCursedPacket(false), serverPlayer);
            }

            player.displayClientMessage(Component.literal("You feel an overwhelming sense of relief..."), true);
        }
    }

}
