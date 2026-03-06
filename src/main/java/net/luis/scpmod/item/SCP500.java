package net.luis.scpmod.item;

import net.luis.scpmod.capability.MadnessCapability;
import net.luis.scpmod.capability.MadnessProvider;
import net.luis.scpmod.entity.custom.CursedPlayer;
import net.luis.scpmod.network.MadnessSyncPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SCP500 extends Item {
    private static final String USAGE_COUNT_TAG = "scp_500_usage";
    private static final String LAST_USED_TAG = "scp_500_last_used";

    public SCP500(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide && entity instanceof Player player) {
            CompoundTag playerData = player.getPersistentData();
            long currentTime = world.getGameTime();

            // Retrieve previous SCP-500 usage data
            int usageCount = playerData.getInt(USAGE_COUNT_TAG);
            long lastUsedTime = playerData.getLong(LAST_USED_TAG);

            // Increase usage count
            usageCount++;
            playerData.putInt(USAGE_COUNT_TAG, usageCount);
            playerData.putLong(LAST_USED_TAG, currentTime);

            //  Apply healing effects (but reduce effectiveness with overuse)
            if (usageCount <= 2) {
                // Normal healing (first 3 uses)
                player.removeAllEffects();
                MadnessCapability madness = MadnessProvider.get(player);
                madness.setMadnessLevel(0);
                SCPModNetwork.sendToPlayer(new MadnessSyncPacket(0), (ServerPlayer) player);
                CursedPlayer.cureWithSCP500(player);
                player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1)); // Instant heal
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1)); // 10s Regeneration
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 0)); // 2 min Resistance
                entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1)); // 2 min Absorption (extra hearts)
                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400, 0)); // 2 min Fire Resistance
            } else if (usageCount <= 4) {
                // Weaker healing (4-5 uses)
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0)); // Regen I for 10s
            } else {
                // Side effects after 6+ uses
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1)); // Nausea for 10s
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 1)); // Weakness for 20s
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 400, 1)); // Hunger for 20s
                player.sendSystemMessage(Component.literal("I don't feel so good..."));
            }

            player.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);

            player.getCooldowns().addCooldown(this, 100);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

}
