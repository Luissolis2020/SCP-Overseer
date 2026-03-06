package net.luis.scpmod.capability;

import net.luis.scpmod.network.MadnessSyncPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

public class MadnessCapability implements INBTSerializable<CompoundTag> {
    public static final Capability<MadnessCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private int madnessLevel = 0;

    public int getMadnessLevel() {
        return madnessLevel;
    }

    public void setMadnessLevel(int level) {
        this.madnessLevel = Math.min(level, 5); // Cap madness at 5
    }

    public void increaseMadness(ServerPlayer player) {
        if (madnessLevel < 5) {
            madnessLevel++;
        }
        syncMadness(player);
    }

    public void decreaseMadness(ServerPlayer player) {
        if (madnessLevel > 0) {
            madnessLevel--;
        }
        syncMadness(player);
    }

    private void syncMadness(ServerPlayer player) {
        SCPModNetwork.sendToPlayer(new MadnessSyncPacket(madnessLevel), player);
        player.getPersistentData().putInt("scp_668_madness", madnessLevel);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("madness_level", madnessLevel);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("madness_level")) {
            madnessLevel = tag.getInt("madness_level");
        }
    }
}
