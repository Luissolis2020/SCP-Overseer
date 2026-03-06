package net.luis.scpmod.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Random;

public class SCP1025 extends Item {
    private static final Random random = new Random();

    public SCP1025(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        // Check if player is wearing SCP-427
        boolean hasSCP427 = CuriosApi.getCuriosInventory(player).map(curios -> {
            return curios.getCurios().values().stream()
                    .anyMatch(handler -> {
                        ItemStack stack = handler.getStacks().getStackInSlot(0); // Get first item in the slot
                        return !stack.isEmpty() && stack.getItem() == ModItems.SCP_427.get();
                    });
        }).orElse(false);

        if (!level.isClientSide()) {
            if (hasSCP427) {
                player.sendSystemMessage(Component.literal("SCP-427 protects you from the disease!"));
            } else {
                String disease = applyRandomDisease(player);
                player.sendSystemMessage(Component.literal("You read about: " + disease));
            }
        }

        spawnSoulParticles(level, player);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    private String applyRandomDisease(Player player) {
        int roll = random.nextInt(12);
        switch (roll) {
            case 0:
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
                playDiseaseSound(player, SoundEvents.WITHER_HURT);
                return "Tuberculosis";
            case 1:
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0));
                playDiseaseSound(player, SoundEvents.PLAYER_HURT);
                return "Common Cold";
            case 2:
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                playDiseaseSound(player, SoundEvents.SLIME_BLOCK_STEP);
                return "Food Poisoning";
            case 3:
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
                playDiseaseSound(player, SoundEvents.IRON_GOLEM_HURT);
                return "Heart Disease";
            case 4:
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
                playDiseaseSound(player, SoundEvents.SKELETON_HURT);
                return "Bone Cancer";
            case 5:
                player.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 3));
                playDiseaseSound(player, SoundEvents.LIGHTNING_BOLT_THUNDER);
                return "Sudden Death Syndrome";
            case 6:
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                playDiseaseSound(player, SoundEvents.ILLUSIONER_PREPARE_BLINDNESS);
                return "Migraine";
            case 7:
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                playDiseaseSound(player, SoundEvents.ENDERMAN_STARE);
                return "Vertigo";
            case 8:
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1));
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                playDiseaseSound(player, SoundEvents.WITHER_SPAWN);
                return "Pneumonia";
            case 9:
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 300, 0));
                playDiseaseSound(player, SoundEvents.AMBIENT_CAVE.get());
                return "Paranoia";
            case 10:
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
                playDiseaseSound(player, SoundEvents.SLIME_BLOCK_FALL);
                return "Skin Irritation";
            case 11:
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                playDiseaseSound(player, SoundEvents.BLAZE_AMBIENT);
                return "High Fever";
            default:
                return "Unknown Disease";
        }
    }

    private void playDiseaseSound(Player player, SoundEvent sound) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), sound, player.getSoundSource(), 1.0f, 1.0f);
    }

    private void spawnSoulParticles(Level level, Player player) {
        for (int i = 0; i < 10; i++) {
            double x = player.getX() + (random.nextDouble() - 0.5) * 2.0;
            double y = player.getY() + random.nextDouble() * 2.0;
            double z = player.getZ() + (random.nextDouble() - 0.5) * 2.0;

            level.addParticle(ParticleTypes.SOUL, x, y, z, 0, 0, 0);
        }
    }
}
