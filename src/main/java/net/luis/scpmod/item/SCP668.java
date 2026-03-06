package net.luis.scpmod.item;

import net.luis.scpmod.capability.MadnessCapability;
import net.luis.scpmod.capability.MadnessProvider;
import net.luis.scpmod.entity.custom.SCP173Entity;
import net.luis.scpmod.network.MadnessSyncPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

public class SCP668 extends Item {
    private static final int MAX_USES = 10; // The knife has 10 uses
    private static final int COOLDOWN_TICKS = 60; // 3-second cooldown
    private static final int MADNESS_TICK_RATE = 1200; // Every 60 seconds
    private static final int MAX_MADNESS = 5;

    public SCP668(Properties properties) {
        super(properties.durability(MAX_USES).fireResistant());
    }

    private boolean isImmuneTo668(LivingEntity entity) {
        return entity instanceof SCP173Entity ||
                entity instanceof EnderDragon;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (!isImmuneTo668(target)) {
                target.hurt(attacker.damageSources().genericKill(), target.getMaxHealth());

                Level level = attacker.level();
                level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.0F);

                spawnBloodParticles(target);

                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

                MadnessCapability madness = MadnessProvider.get(player);
                if (madness.getMadnessLevel() > 0) {
                    if (madness.getMadnessLevel() == 5) {
                        madness.setMadnessLevel(3);
                    } else {
                        if (player instanceof ServerPlayer serverPlayer) {
                            madness.decreaseMadness(serverPlayer);
                        }
                    }
                    player.sendSystemMessage(Component.literal("The knife feeds... Your mind clears slightly."));
                }
            } else {
                String entityName = target.getName().getString();
                player.sendSystemMessage(Component.literal("SCP-668 refuses to cut through " + entityName + "!"));
            }

            stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
        return true;
    }


    private void spawnBloodParticles(LivingEntity target) {
        Level level = target.level();
        for (int i = 0; i < 10; i++) {
            double x = target.getX() + (target.getRandom().nextDouble() - 0.5) * target.getBbWidth();
            double y = target.getY() + target.getRandom().nextDouble() * target.getBbHeight();
            double z = target.getZ() + (target.getRandom().nextDouble() - 0.5) * target.getBbWidth();
            level.addParticle(ParticleTypes.CRIT, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(entity instanceof ServerPlayer player)) return;

        boolean hasSCP427 = CuriosApi.getCuriosInventory(player).map(curios ->
                curios.getCurios().values().stream()
                        .anyMatch(handler -> {
                            ItemStack curioStack = handler.getStacks().getStackInSlot(0);
                            return !curioStack.isEmpty() && curioStack.getItem() == ModItems.SCP_427.get();
                        })
        ).orElse(false);

        if (hasSCP427) {
            return;
        }

        // Get Madness Capability
        MadnessCapability madness = MadnessProvider.get(player);
        int madnessLevel = madness.getMadnessLevel();

        if (level.getGameTime() % MADNESS_TICK_RATE == 0 && selected) {
            int newMadnessLevel = Math.min(madnessLevel + 1, 5);
            madness.setMadnessLevel(newMadnessLevel);
            SCPModNetwork.sendToPlayer(new MadnessSyncPacket(newMadnessLevel), player);
            player.sendSystemMessage(Component.literal("Kill..."));
        }
        applyMadnessEffects(player, madness.getMadnessLevel());
    }

    private void applyMadnessEffects(Player player, int madnessLevel) {
        switch (madnessLevel) {
            case 1 -> player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
            case 2 -> player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
            case 3 -> player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            case 4 -> player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 300, 1));
            case 5 -> player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

}
