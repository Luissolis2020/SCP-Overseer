package net.luis.scpmod.entity.ai.navigation;

import net.luis.scpmod.entity.custom.SCP_096_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class SCP096Navigation extends GroundPathNavigation {

    public SCP096Navigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxNodes) {
        SCP096NodeEvaluator evaluator = new SCP096NodeEvaluator();
        PathFinder pathFinder = new PathFinder(evaluator, maxNodes);

        this.nodeEvaluator = evaluator;
        this.nodeEvaluator.setCanFloat(true);
        this.nodeEvaluator.setCanPassDoors(true);

        return pathFinder;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.mob instanceof SCP_096_Entity scp096 && scp096.isAngry() && scp096.getAngryCooldown() <= 0) {

            Player target = (Player) scp096.getTarget();
            if (target != null && target.isAlive()) {
                BlockPos scpPos = scp096.blockPosition();
                BlockPos targetPos = target.blockPosition();
                int dy = targetPos.getY() - scpPos.getY();

                //System.out.println("dy = " + dy + ", distance = " + scp096.distanceTo(target));

                if (dy > 2) {
                    scp096.digStaircaseUp();
                } else if (dy < -3) {
                    scp096.digDown();
                } else {
                    scp096.breakObstructingBlocks();
                }

                if (dy > 4 && scp096.distanceTo(target) <= 2.5) {
                    scp096.digUpwardTowardPlayer(target);
                }
            }
        }
    }


}
