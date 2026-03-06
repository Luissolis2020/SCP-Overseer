package net.luis.scpmod.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.luis.scpmod.util.SCPDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

import java.util.Random;

public class SCP207 extends Item {

    public SCP207(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            applySCP207Effect(player);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        if (entity instanceof Player player && !player.isCreative()) {
            ItemStack emptyBottle = new ItemStack(ModItems.SCP_207_EMPTY.get());

            if (stack.getCount() == 1) {
                return emptyBottle;
            } else {
                stack.shrink(1);

                if (!player.getInventory().add(emptyBottle)) {
                    player.drop(emptyBottle, false);
                }
            }
        }
        return stack;
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    private void applySCP207Effect(Player player) {
        int currentCount = player.getPersistentData().getInt("scp_207_count");
        int newCount = Math.min(currentCount + 1, 10);
        player.getPersistentData().putInt("scp_207_count", newCount);

        int speedLevel = Math.min(newCount, 4);
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, speedLevel));
    }

    public static void tickEffect(Player player) {
        int count = player.getPersistentData().getInt("scp_207_count");

        if (count > 0 && player.level().getGameTime() % 600 == 0) { // Every 30 sec
            float damage = Math.min(count * 2.0F, 20.0F); // Max 10 hearts (20 HP)
            DamageSource scp207Damage = new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SCPDamageTypes.SCP_207));

            player.hurt(scp207Damage, damage);
            spawnParticles(player);

            if (count >= 6) {
                applyHyperactivityEffects(player);
            }
        }
    }

    private static void spawnParticles(Player player) {
        for (int i = 0; i < 5; i++) {
            double x = player.getX() + (Math.random() - 0.5) * 2.0;
            double y = player.getY() + Math.random() * 2.0;
            double z = player.getZ() + (Math.random() - 0.5) * 2.0;

            player.level().addParticle(ParticleTypes.CRIT, x, y, z, 0, 0, 0);
        }
    }

    private static void applyHyperactivityEffects(Player player) {
        Random random = new Random();

        // 50% chance to get Nausea
        if (random.nextBoolean()) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }

        // 30% chance to randomly jump
        if (random.nextInt(10) < 3) {
            player.setDeltaMovement(player.getDeltaMovement().x, 0.6, player.getDeltaMovement().z);
            player.jumpFromGround();
        }

        // 20% chance for a speed burst
        if (random.nextInt(10) < 2) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 4));
        }
    }
}
