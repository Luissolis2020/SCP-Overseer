package net.luis.scpmod.block.entity;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SCPMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SCP_458_Entity>> SCP_458_Entity =
            BLOCK_ENTITIES.register("scp_458_entity",
                    () -> BlockEntityType.Builder.of(SCP_458_Entity::new, ModBlocks.SCP_458.get()).build(null));

    public static final RegistryObject<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE_ENTITY =
            BLOCK_ENTITIES.register("research_table_entity",
                    () -> BlockEntityType.Builder.of(ResearchTableBlockEntity::new, ModBlocks.RESEARCH_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
