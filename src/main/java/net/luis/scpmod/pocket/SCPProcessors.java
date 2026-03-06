package net.luis.scpmod.pocket;

import net.luis.scpmod.SCPMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class SCPProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, SCPMod.MOD_ID);

    public static final RegistryObject<StructureProcessorType<StripPocketConnectorProcessor>> STRIP_POCKET_CONNECTOR =
            PROCESSORS.register("strip_pocket_connector", () -> () -> StripPocketConnectorProcessor.CODEC);

    public static void init(IEventBus modBus) {
        PROCESSORS.register(modBus);
    }

    private SCPProcessors() {}
}
