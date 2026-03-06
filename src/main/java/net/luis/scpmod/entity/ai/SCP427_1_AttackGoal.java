package net.luis.scpmod.entity.ai;

import net.luis.scpmod.entity.custom.SCP427_1_Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SCP427_1_AttackGoal extends MeleeAttackGoal {
    private final SCP427_1_Entity entity;
    private int attackDelay = 8;
    private int ticksUntilNextAttack = 8;
    private boolean shouldCountTillNextAttack = false;

    public SCP427_1_AttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((SCP427_1_Entity) pMob);
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 8;
        ticksUntilNextAttack = 8;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);}

    protected boolean isTimeToAttack() {return this.ticksUntilNextAttack <= 0;}

    protected boolean isTimeToStartAttackAnimation() {return this.ticksUntilNextAttack <= attackDelay;}

    protected int getTickUntilNextAttack() {return this.ticksUntilNextAttack;}

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}
