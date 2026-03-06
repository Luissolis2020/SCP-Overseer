package net.luis.scpmod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MadnessProvider extends CapabilityProvider<MadnessProvider> {
    private final MadnessCapability madness = new MadnessCapability();
    private final LazyOptional<MadnessCapability> optional = LazyOptional.of(() -> madness);

    public MadnessProvider() {
        super(MadnessProvider.class);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        return MadnessCapability.INSTANCE.orEmpty(cap, optional);
    }

    public static MadnessCapability get(Player player) {
        return player.getCapability(MadnessCapability.INSTANCE).orElse(new MadnessCapability());
    }

    public CompoundTag serializeNBT() {
        return madness.serializeNBT();
    }

    public void deserializeNBT(CompoundTag tag) {
        madness.deserializeNBT(tag);
    }
}
