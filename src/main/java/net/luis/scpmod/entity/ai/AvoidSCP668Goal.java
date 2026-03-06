package net.luis.scpmod.entity.ai;

import net.luis.scpmod.item.ModItems;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class AvoidSCP668Goal extends Goal {
    private final PathfinderMob mob;
    private Player fearedPlayer;
    private final double speedMultiplier;
    private final double fearDistance;

    public AvoidSCP668Goal(PathfinderMob mob, double speedMultiplier, double fearDistance) {
        this.mob = mob;
        this.speedMultiplier = speedMultiplier;
        this.fearDistance = fearDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Find the nearest player holding SCP-668 within the fear range
        for (Player player : mob.level().players()) {
            if (player.distanceTo(mob) <= fearDistance && isHoldingSCP668(player)) {
                fearedPlayer = player;
                return true;
            }
        }
        fearedPlayer = null;
        return false;
    }

    @Override
    public void start() {
        moveAwayFromPlayer();
    }

    @Override
    public boolean canContinueToUse() {
        return fearedPlayer != null && fearedPlayer.distanceTo(mob) <= fearDistance;
    }

    @Override
    public void tick() {
        moveAwayFromPlayer();
    }

    private void moveAwayFromPlayer() {
        if (fearedPlayer != null) {
            double dx = mob.getX() - fearedPlayer.getX();
            double dz = mob.getZ() - fearedPlayer.getZ();
            double escapeX = mob.getX() + dx;
            double escapeZ = mob.getZ() + dz;

            mob.getNavigation().moveTo(escapeX, mob.getY(), escapeZ, speedMultiplier);
        }
    }

    private boolean isHoldingSCP668(Player player) {
        return player.getMainHandItem().is(ModItems.SCP_668.get()) ||
                player.getOffhandItem().is(ModItems.SCP_668.get());
    }
}
