package net.luis.scpmod.item;

import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.entity.custom.CursedPlayer;
import net.luis.scpmod.entity.custom.SCP_513_1_Entity;
import net.luis.scpmod.item.client.SCP513_Renderer;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.network.SyncCursedPacket;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;

public class SCP513 extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final String COOLDOWN_TAG = "scp513_cooldown";

    public SCP513(Properties pProperties) {
        super(pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        int cooldown = tag.getInt("scp513_cooldown");
        if (cooldown > 0) {
            player.displayClientMessage(Component.literal("The bell resonates... but nothing happens."), true);
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 2.0F, 0.5F);

            boolean spawnedEntity = false;

            for (Player target : level.players()) {
                if (target.distanceTo(player) <= 20) {
                    if (target instanceof ServerPlayer serverPlayer) {
                        if (!CursedPlayer.isCursed(serverPlayer)) {
                            CursedPlayer.setCursed(serverPlayer, true);
                            SCPModNetwork.sendToPlayer(new SyncCursedPacket(true), serverPlayer);

                            if (!spawnedEntity && level.random.nextInt(100) < 50) {
                                SCP_513_1_Entity scp513_1 = new SCP_513_1_Entity(ModEntities.SCP_513_1.get(), level);
                                level.addFreshEntity(scp513_1);
                                spawnedEntity = true;
                            }
                        }
                    }
                }
            }

            tag.putInt("scp513_cooldown", 200);
            stack.setTag(tag);
            player.getCooldowns().addCooldown(this, 200);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            int cooldown = tag.getInt(COOLDOWN_TAG);

            if (cooldown > 0) {
                tag.putInt(COOLDOWN_TAG, cooldown - 1);
                stack.setTag(tag);
            }
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().then("Ring", Animation.LoopType.PLAY_ONCE));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SCP513_Renderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SCP513_Renderer();
                }
                return renderer;
            }
        });
    }
}
