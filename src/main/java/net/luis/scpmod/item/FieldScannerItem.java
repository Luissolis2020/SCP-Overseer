package net.luis.scpmod.item;

import net.luis.scpmod.anomaly.AnomalyField;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FieldScannerItem extends Item {

    public FieldScannerItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        if (!(player instanceof ServerPlayer sp) || !(level instanceof ServerLevel sl)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        var zone = AnomalyField.nearestZone(sl, sp.blockPosition(), sp);
        if (zone == null) {
            if (AnomalyField.hasUnclearedZones(sl, sp)) {
                sp.displayClientMessage(Component.literal("Scanner returns only static."), true);
            } else {
                sp.displayClientMessage(Component.literal("No remaining signatures detected."), true);
                sp.playNotifySound(SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.25f, 1.4f);
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        double dist = AnomalyField.distXZ(sp.blockPosition(), zone.center());
        double edgeDist = AnomalyField.edgeDistance(sp.blockPosition(), zone);

        String msg;
        float pitch;

        if (AnomalyField.isInCore(sp.blockPosition(), zone)) {
            msg = "SIGNAL: CORE";
            pitch = 1.50f;
        } else if (edgeDist <= 4) { msg = "SIGNAL: VERY STRONG"; pitch = 1.35f; }
        else if (edgeDist <= 16) { msg = "SIGNAL: STRONG"; pitch = 1.20f; }
        else if (edgeDist <= 40) { msg = "SIGNAL: MODERATE"; pitch = 1.05f; }
        else if (edgeDist <= 80) { msg = "SIGNAL: WEAK"; pitch = 0.90f; }
        else { msg = "SIGNAL: FAINT"; pitch = 0.75f; }

        sp.displayClientMessage(Component.literal(msg), true);
        sp.playNotifySound(SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 0.25f, pitch);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
