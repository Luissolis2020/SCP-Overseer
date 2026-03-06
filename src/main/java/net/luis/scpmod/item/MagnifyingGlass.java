package net.luis.scpmod.item;

import net.luis.scpmod.anomaly.AnomalyField;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.pda.PDADataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MagnifyingGlass extends Item {

    private static final String SCAN_TARGET_POS = "scpmod_scan_target_pos";
    private static final int SCAN_TIME_TICKS = 40; // 2 seconds

    public MagnifyingGlass(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;

        // Save target to player persistent data (server-side)
        player.getPersistentData().putLong(SCAN_TARGET_POS, ctx.getClickedPos().asLong());

        // Start the "use" action (this shows the progress bar)
        player.startUsingItem(ctx.getHand());

        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return SCAN_TIME_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level.isClientSide) return stack;
        if (!(entity instanceof ServerPlayer player)) return stack;
        if (!(level instanceof net.minecraft.server.level.ServerLevel sl)) return stack;

        if (!player.getPersistentData().contains(SCAN_TARGET_POS)) return stack;

        BlockPos pos = BlockPos.of(player.getPersistentData().getLong(SCAN_TARGET_POS));

        if (!AnomalyField.isAnomalous(sl, pos)) {
            player.displayClientMessage(Component.literal("No anomalous signature detected."), true);
            return stack;
        }

        BlockState state = level.getBlockState(pos);
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String blockId = id.toString();

        boolean isNew = PDADataHandler.markBlockScannedUnique(player, blockId);
        if (isNew) {
            PDADataHandler.markBlockScanned(player, blockId);
            // maybe mission progress here later
        } else {
            player.displayClientMessage(Component.literal("Already documented."), true);
        }

        var zone = AnomalyField.zoneAt(sl, pos);
        if (zone != null) {
            BlockPos playerPos = player.blockPosition();

            if (AnomalyField.isInCore(playerPos, zone)) {
                if (!PDADataHandler.isZoneCleared(player, zone.id())) {
                    PDADataHandler.clearZone(player, zone.id());

                    int tier = zone.tier();
                    ItemStack sample = AnomalySampleItem.ofTier(ModItems.ANOMALY_SAMPLE.get(), tier);

                    if (!player.getInventory().add(sample)) {
                        player.drop(sample, false);
                    }

                    player.displayClientMessage(
                            Component.literal("Sample secured (Tier " + tier + "). Deliver to Research Table."),
                            true
                    );

                } else {
                    player.displayClientMessage(Component.literal("This field has already been logged."), true);
                }
            } else if (!PDADataHandler.isZoneCleared(player, zone.id())) {
                double core = AnomalyField.coreRadius(zone);
                double dist = AnomalyField.distXZ(playerPos, zone.center());
                player.displayClientMessage(
                        Component.literal("Signature detected, but core not reached (" + (int)(dist - core) + "m)."),
                        true
                );
            } else {
                player.displayClientMessage(Component.literal("This field has already been logged."), true);
            }
        }

        if (!PDADataHandler.isTabUnlocked(player, "BLOCKS")) {
            PDADataHandler.unlockTab(player, "BLOCKS");
        }

        SCPModNetwork.syncPDAStateTo(player, true);
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide && entity instanceof Player p) {
            p.getPersistentData().remove(SCAN_TARGET_POS);
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }


}
