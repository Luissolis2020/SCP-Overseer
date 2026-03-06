package net.luis.scpmod.entity.ai;

import net.luis.scpmod.entity.custom.SCP_096_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SCP_096_AttackGoal extends MeleeAttackGoal {
    private final SCP_096_Entity scp096;

    public SCP_096_AttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.scp096 = (SCP_096_Entity) pMob;
    }

    @Override
    public boolean canUse() {
        return scp096.isAngry() && scp096.getAngryCooldown() == 0 && !scp096.targets.isEmpty() && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        Player target = scp096.getClosestTarget();

        if (target != null && scp096.isAngry() && scp096.getAngryCooldown() == 0) {
            if (!target.isAlive()) {
                scp096.targets.remove(target.getUUID());
                if (scp096.targets.isEmpty()) {
                    scp096.calmDown();
                }
                return;
            }

            scp096.setTarget(target);

            double distance = scp096.distanceTo(target);
            double speed = distance > 10 ? 2.2 : 1.5;

            scp096.getNavigation().moveTo(target, speed);

            Vec3 direction = target.position().subtract(scp096.position()).normalize();
            float targetYaw = (float) (Math.atan2(direction.z, direction.x) * (180 / Math.PI)) - 90;
            float newYaw = scp096.getYRot() + Mth.wrapDegrees(targetYaw - scp096.getYRot()) * 0.2F;
            scp096.setYRot(newYaw);
            scp096.setYHeadRot(newYaw);

            if (distance <= 2.5) {
                scp096.swing(InteractionHand.MAIN_HAND);
                scp096.doHurtTarget(target);
            }
        } else {
            scp096.getNavigation().stop();
        }
    }
}
