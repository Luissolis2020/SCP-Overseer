package net.luis.scpmod.entity.ai;

import net.luis.scpmod.capability.MadnessProvider;
import net.luis.scpmod.entity.custom.CursedPlayer;
import net.luis.scpmod.entity.custom.SCP_2295_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.List;

public class SCP_2295_HealGoal extends Goal {
    private final SCP_2295_Entity scp2295;
    private Player targetPlayer;
    private BlockPos bedPosition;
    private boolean hasStartedComeHere = false;
    private boolean hasStartedHealing = false;
    private boolean hasStartedCrying = false;

    public SCP_2295_HealGoal(SCP_2295_Entity scp2295) {
        this.scp2295 = scp2295;
    }

    @Override
    public boolean canUse() {
        if (scp2295.isHealing()) return false; // Already healing

        scp2295.tickCooldowns();

        List<Player> players = scp2295.level().getEntitiesOfClass(Player.class, scp2295.getBoundingBox().inflate(10.0));

        for (Player player : players) {

            if (scp2295.isPlayerOnCooldown(player)) {
               // System.out.println("SCP-2295: Ignoring " + player.getName().getString() + " (on cooldown)");
                continue;
            }

            if (player.getHealth() < player.getMaxHealth() && !player.isCreative()) {
               // System.out.println("SCP-2295: Found injured player: " + player.getName().getString());

                targetPlayer = player;
                boolean foundBed = findNearestBed();

//                if (!foundBed) {
//                    System.out.println("SCP-2295: No bed found!");
//                } else {
//                    System.out.println("SCP-2295: Found bed at " + bedPosition);
//                }

                return foundBed;
            }
        }
        return false;
    }

    private boolean findNearestBed() {
        BlockPos closestBed = null;
        double closestDistance = Double.MAX_VALUE;

        // Search in a 10x4x10 area
        for (int x = -10; x <= 10; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -10; z <= 10; z++) {
                    BlockPos checkPos = scp2295.blockPosition().offset(x, y, z);
                    BlockState state = scp2295.level().getBlockState(checkPos);

                    if (state.getBlock() instanceof BedBlock) {
                        double distance = checkPos.distSqr(scp2295.blockPosition());

                        // Find the closest bed
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestBed = checkPos;
                        }
                    }
                }
            }
        }

        if (closestBed != null) {
            BlockState bedState = scp2295.level().getBlockState(closestBed);
            boolean isHead = bedState.getValue(BedBlock.PART) == BedPart.HEAD;

            // Find the FOOT of the bed
            bedPosition = isHead ? closestBed.relative(bedState.getValue(BedBlock.FACING).getOpposite()) : closestBed;

            //System.out.println("SCP-2295: Found bed at " + bedPosition);
            return true;
        }

        //System.out.println("SCP-2295: No bed found in range.");
        return false;
    }

    @Override
    public void start() {
        if (targetPlayer != null && bedPosition != null) {
            //System.out.println("SCP-2295: Walking to bed at " + bedPosition);
            scp2295.getNavigation().moveTo(bedPosition.getX(), bedPosition.getY(), bedPosition.getZ(), 0.4);
            scp2295.setHealing(targetPlayer, bedPosition);
        }
    }

    @Override
    public boolean canContinueToUse() {
        // Continue if he hasn't reached the bed yet
        if (!scp2295.blockPosition().closerThan(bedPosition, 0.1)) {
            return true;
        }

        // Continue if there's still a player to heal
        return targetPlayer != null && targetPlayer.isAlive() && targetPlayer.getHealth() < targetPlayer.getMaxHealth();
    }

    @Override
    public void tick() {
        if (bedPosition == null || targetPlayer == null) {
            stop();
            return;
        }

        if (!scp2295.blockPosition().closerThan(bedPosition, 0.1)) {
            scp2295.getNavigation().moveTo(bedPosition.getX(), bedPosition.getY(), bedPosition.getZ(), 0.4);
            return;
        }

        scp2295.getNavigation().stop();
        scp2295.getLookControl().setLookAt(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), 30.0F, 30.0F);

        if (!targetPlayer.getPersistentData().getBoolean("scp2295_force_sleep")) {
            targetPlayer.getPersistentData().putBoolean("scp2295_force_sleep", true);
        }

        if (!scp2295.isWaiting() && !hasStartedComeHere) {
            scp2295.setWaiting(true);
            scp2295.setComeHereAnimationTime(100);
            hasStartedComeHere = true;
        }

        if (scp2295.getComeHereAnimationTime() > 0) {
            return;
        }

        targetPlayer.startSleeping(bedPosition);

        if (shouldCry(targetPlayer) || hasStartedCrying) {
            startCrying();
        } else {
            startHealing();
        }

    }

    @Override
    public void stop() {
        scp2295.setHealingState(false);
        scp2295.setWaiting(false);
        scp2295.setSad(false);
        scp2295.setHealing(null, null);
        scp2295.setComeHereAnimationTime(0);
        scp2295.setHealAnimationTime(0);
        scp2295.setSadAnimationTime(0);
    }

    private boolean shouldCry(Player player) {
        return player.hasEffect(net.minecraft.world.effect.MobEffects.WITHER) ||
                MadnessProvider.get(player).getMadnessLevel() > 0 ||
                CursedPlayer.isCursed(player) ||
                player.getType().toString().contains("undead");
    }

    private void startCrying() {
        if (!hasStartedCrying) {
            scp2295.setWaiting(false);
            scp2295.setSad(true);
            scp2295.setSadAnimationTime(440);
            targetPlayer.displayClientMessage(Component.literal("SCP-2295 seems heartbroken."), true);
            hasStartedCrying = true;
        }

        if (scp2295.getSadAnimationTime() > 0) {
            return;
        }

        scp2295.addCryingCooldown(targetPlayer);
        targetPlayer.stopSleeping();
        targetPlayer.getPersistentData().remove("scp2295_force_sleep");

        scp2295.setHealingState(false);
        scp2295.setHealing(null, null);
        targetPlayer = null;
        hasStartedComeHere = false;
        hasStartedHealing = false;
        hasStartedCrying = false;

        scp2295.getNavigation().moveTo(
                scp2295.getOriginalPosition().getX(),
                scp2295.getOriginalPosition().getY(),
                scp2295.getOriginalPosition().getZ(),
                0.4
        );

    }

    private void startHealing() {
        if (!hasStartedHealing) {
            scp2295.setWaiting(false);
            scp2295.setHealingState(true);
            scp2295.setHealAnimationTime(460);
            targetPlayer.displayClientMessage(Component.literal("You feel warm and safe..."), true);
            hasStartedHealing = true;
        }
        // Show fade-to-black effect
        if (scp2295.getHealAnimationTime() == 40) {
            targetPlayer.displayClientMessage(Component.literal("Your vision fades..."), true);
        }

        if (scp2295.getHealAnimationTime() > 0) {
            return;
        }

        // Complete healing
        targetPlayer.setHealth(targetPlayer.getMaxHealth());
        targetPlayer.removeAllEffects();

        targetPlayer.stopSleeping();
        targetPlayer.getPersistentData().remove("scp2295_force_sleep");

        // Reset SCP-2295's state
        scp2295.setHealingState(false);
        hasStartedComeHere = false;
        hasStartedHealing = false;
        hasStartedCrying = false;
        scp2295.setHealing(null, null);

        if (!canUse()) {
            scp2295.getNavigation().moveTo(
                    scp2295.getOriginalPosition().getX(),
                    scp2295.getOriginalPosition().getY(),
                    scp2295.getOriginalPosition().getZ(),
                    0.4);
            scp2295.setWaiting(false);
            scp2295.setHealingState(false);
        }
    }
}
