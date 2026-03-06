package net.luis.scpmod.block.entity;

import net.luis.scpmod.item.AnomalySampleItem;
import net.luis.scpmod.network.SCPModNetwork;
import net.luis.scpmod.pda.PDADataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

import static net.minecraft.world.level.block.Block.popResource;

public class ResearchTableBlockEntity extends BlockEntity {

    private static final int DURATION_TICKS = 200; // 10 seconds
    private static final int GRANT_INTERVAL = 20;  // 1 second
    private static final int OFFLINE_TIMEOUT_TICKS = 20 * 60 * 5; // 5 minutes

    private ItemStack sample = ItemStack.EMPTY;
    private int progressTicks = 0;

    private UUID owner;
    private int totalResearch = 0;
    private int grantedSoFar = 0;
    private int offlineTicks = 0;

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE_ENTITY.get(), pos, state);
    }

    public boolean tryInsertSample(ServerPlayer player, ItemStack held) {
        if (!sample.isEmpty()) {
            player.displayClientMessage(Component.literal("Research Table is busy."), true);
            return false;
        }

        int tier = AnomalySampleItem.getTier(held);
        totalResearch = totalForTier(tier);
        owner = player.getUUID();

        // consume exactly 1
        ItemStack one = held.copy();
        one.setCount(1);
        held.shrink(1);

        sample = one;
        progressTicks = 0;
        grantedSoFar = 0;
        offlineTicks = 0;

        setChanged();
        player.displayClientMessage(Component.literal("Research started (Tier " + tier + ")."), true);
        return true;
    }

    public void tickServer() {
        if (level == null) return;
        if (sample.isEmpty() || owner == null) return;

        ServerLevel sl = (ServerLevel) level;
        ServerPlayer player = sl.getServer().getPlayerList().getPlayer(owner);

        // Owner offline
        if (player == null) {
            offlineTicks++;

            if (offlineTicks >= OFFLINE_TIMEOUT_TICKS) {
                // Cancel the job and drop the sample so table isn't permanently busy
                if (!sample.isEmpty()) {
                    BlockPos dropPos = worldPosition.above();
                    popResource(sl, dropPos, sample);
                }
                resetWork();
                setChanged();
            }
            return;
        }

        offlineTicks = 0;
        progressTicks++;

        // drip every second
        if (progressTicks % GRANT_INTERVAL == 0) {
            int totalSeconds = DURATION_TICKS / GRANT_INTERVAL; // 10
            int secondsDone = progressTicks / GRANT_INTERVAL;   // 1..10

            int targetGranted = (totalResearch * secondsDone) / totalSeconds;
            int delta = targetGranted - grantedSoFar;

            if (delta > 0) {
                PDADataHandler.addResearchPoints(player, delta);
                grantedSoFar += delta;

                boolean didUnlock = checkUnlockResearchTab(player);
                if (!didUnlock) {
                    SCPModNetwork.syncResearchPointsTo(player);
                }

                // update PDA live
                SCPModNetwork.syncResearchPointsTo(player);
                setChanged();
            }
        }

        // finish
        if (progressTicks >= DURATION_TICKS) {
            int remaining = totalResearch - grantedSoFar;
            if (remaining > 0) {
                PDADataHandler.addResearchPoints(player, remaining);
                grantedSoFar += remaining;
            }

            boolean didUnlock = checkUnlockResearchTab(player);

            resetWork();
            setChanged();

            if (!didUnlock) {
                SCPModNetwork.syncResearchPointsTo(player);
            }
        }

    }

    public void sendStatusTo(ServerPlayer player) {
        if (sample.isEmpty()) {
            player.displayClientMessage(Component.literal("Research Table: Idle."), true);
        } else {
            int pct = (int) ((progressTicks / (double) DURATION_TICKS) * 100.0);
            player.displayClientMessage(Component.literal("Research Table: " + pct + "%"), true);
        }
    }

    private static int totalForTier(int tier) {
        return switch (tier) {
            case 1 -> 100;
            case 2 -> 250;
            case 3 -> 600;
            default -> 100;
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (!sample.isEmpty()) tag.put("Sample", sample.save(new CompoundTag()));
        tag.putInt("Progress", progressTicks);
        tag.putInt("Total", totalResearch);
        tag.putInt("Granted", grantedSoFar);
        tag.putInt("OfflineTicks", offlineTicks);
        if (owner != null) tag.putUUID("Owner", owner);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        sample = tag.contains("Sample") ? ItemStack.of(tag.getCompound("Sample")) : ItemStack.EMPTY;
        progressTicks = tag.getInt("Progress");
        totalResearch = tag.getInt("Total");
        grantedSoFar = tag.getInt("Granted");
        offlineTicks = tag.getInt("OfflineTicks");
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
    }

    private void resetWork() {
        sample = ItemStack.EMPTY;
        progressTicks = 0;
        offlineTicks = 0;
        owner = null;
        totalResearch = 0;
        grantedSoFar = 0;
    }

    private boolean checkUnlockResearchTab(ServerPlayer player) {
        if (!PDADataHandler.isTabUnlocked(player, "RESEARCH")
                && PDADataHandler.getResearchPoints(player) >= 50) {

            PDADataHandler.unlockTab(player, "RESEARCH");
            player.displayClientMessage(Component.literal("PDA Update: Research tab unlocked."), true);

            SCPModNetwork.syncPDAStateTo(player, true);
            return true;
        }
        return false;
    }

}

