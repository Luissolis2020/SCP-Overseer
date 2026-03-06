package net.luis.scpmod.item;

import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.entity.custom.SCP427_1_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.UUID;

public class SCP427 extends Item implements ICurio {
    public SCP427(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
        return CuriosApi.createCurioProvider(this);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        ICurio.super.onEquip(slotContext, prevStack);

        LivingEntity entity = slotContext.entity();
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
        entity.sendSystemMessage(Component.literal("You feel a strange energy from SCP-427..."));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack) {
        ICurio.super.onUnequip(slotContext, prevStack);
        slotContext.entity().sendSystemMessage(Component.literal("You feel weaker without SCP-427..."));
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        return ICurio.super.canEquip(slotContext) && slotContext.identifier().equals("necklace");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        return CuriosApi.getCuriosInventory(player).map(curios -> {
            return curios.getStacksHandler("necklace").map(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack slotStack = handler.getStacks().getStackInSlot(i);

                    if (slotStack.isEmpty()) {
                        handler.getStacks().setStackInSlot(i, stack.copy());
                        stack.shrink(1);
                        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                    }
                }
                return InteractionResultHolder.fail(stack);
            }).orElse(InteractionResultHolder.fail(stack));
        }).orElse(InteractionResultHolder.fail(stack));
    }

    @Override
    public boolean canUnequip(SlotContext slotContext) {
        LivingEntity entity = slotContext.entity();
        CompoundTag playerData = entity.getPersistentData();
        int mutationLevel = playerData.getInt("scp_427_mutation");
        return mutationLevel < 7;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        ICurio.super.curioTick(slotContext);
        LivingEntity entity = slotContext.entity();

        if (!entity.level().isClientSide()) {
            CompoundTag playerData = entity.getPersistentData();

            if (!playerData.contains("scp_427_mutation")) {
                playerData.putInt("scp_427_mutation", 0);
            }

            int mutationLevel = playerData.getInt("scp_427_mutation");

            // Apply SCP-427 effects
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, false));

            if (mutationLevel >= 5) {
                entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 1, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1, false, false));
            }

            if (mutationLevel >= 8) {
                entity.sendSystemMessage(Component.literal("Your body is no longer human..."));

                if (entity instanceof Player player) {
                    player.sendSystemMessage(Component.literal("You have become something... inhuman."));
                    player.getPersistentData().putBoolean("scp_427_stuck", true);

                    CuriosApi.getCuriosInventory(player).ifPresent(curios -> {
                        curios.getCurios().forEach((slotType, slot) -> {
                            for (int i = 0; i < slot.getStacks().getSlots(); i++) {
                                ItemStack stack = slot.getStacks().getStackInSlot(i);
                                if (stack.getItem() == ModItems.SCP_427.get()) {
                                    stack.shrink(1);
                                    break;
                                }
                            }
                        });
                    });

                    player.hurt(player.damageSources().generic(), Float.MAX_VALUE);

                    SCP427_1_Entity entity427 = ModEntities.SCP_427_1.get().create(player.level());
                    if (entity427 != null) {
                        entity427.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                        player.level().addFreshEntity(entity427);

                    }
                }
                playerData.putBoolean("scp_427_transformed", true);
            }

            if (entity.level().getGameTime() % 600 == 0) {
                mutationLevel++;
                playerData.putInt("scp_427_mutation", mutationLevel);
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
        return ICurio.super.getAttributeModifiers(slotContext, uuid);
    }
}
