package net.luis.scpmod.entity.ai;

import net.luis.scpmod.entity.custom.SCP173Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class SCP173StalkGoal extends Goal {
    private final SCP173Entity scp173;
    private int attackCooldown = 0;

    public SCP173StalkGoal(SCP173Entity scp173) {
        this.scp173 = scp173;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return scp173.getTarget() != null;
    }

    @Override
    public void tick() {
        if (scp173.getTarget() != null) {
            Player player = (Player) scp173.getTarget();
            boolean isWatched = scp173.isAnyPlayerWatching();
            double distance = scp173.distanceTo(player);

            if (isWatched) {
                scp173.getNavigation().stop();
                scp173.setDeltaMovement(0, scp173.getDeltaMovement().y, 0);
            } else {
                if (distance > 1.5) {
                    scp173.getNavigation().moveTo(player, 1.2);
                } else {
                    if (attackCooldown <= 0) {
                        scp173.attackPlayer(player);
                        attackCooldown = 20;
                    } else {
                        attackCooldown--;
                    }
                }
            }
        }
    }
}
