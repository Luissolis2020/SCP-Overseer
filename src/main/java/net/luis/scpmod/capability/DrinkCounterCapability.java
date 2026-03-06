package net.luis.scpmod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DrinkCounterCapability {
    public static final Capability<DrinkCounterCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private int drinkCount = 0;
    private long lastDrinkTime = 0;

    public int getDrinkCount() {
        return drinkCount;
    }

    public void setDrinkCount(int count) {
        this.drinkCount = count;
    }

    public long getLastDrinkTime() {
        return lastDrinkTime;
    }

    public void setLastDrinkTime(long time) {
        this.lastDrinkTime = time;
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("DrinkCount", drinkCount);
        tag.putLong("LastDrinkTime", lastDrinkTime);
        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        this.drinkCount = tag.getInt("DrinkCount");
        this.lastDrinkTime = tag.getLong("LastDrinkTime");
    }
}
