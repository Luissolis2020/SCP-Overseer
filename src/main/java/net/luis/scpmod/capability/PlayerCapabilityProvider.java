package net.luis.scpmod.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber
public class PlayerCapabilityProvider implements ICapabilityProvider {
    private final DrinkCounterCapability instance = new DrinkCounterCapability();
    private final LazyOptional<DrinkCounterCapability> lazyOptional = LazyOptional.of(() -> instance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == DrinkCounterCapability.INSTANCE ? lazyOptional.cast() : LazyOptional.empty();
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof net.minecraft.world.entity.player.Player) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath("scpmod", "drink_counter"), new PlayerCapabilityProvider());
        }
    }
}
