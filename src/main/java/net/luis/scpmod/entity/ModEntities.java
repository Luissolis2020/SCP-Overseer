package net.luis.scpmod.entity;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SCPMod.MOD_ID);

    public static final RegistryObject<EntityType<SCP173Entity>> SCP_173 = ENTITY_TYPES.register("scp173", () ->
            EntityType.Builder.of(SCP173Entity::new, MobCategory.MONSTER).sized(0.8f, 2.0f).build("scp173"));
    public static final RegistryObject<EntityType<SCP427_1_Entity>> SCP_427_1 = ENTITY_TYPES.register("scp427_1", () ->
            EntityType.Builder.of(SCP427_1_Entity::new, MobCategory.MONSTER).sized(1f, 3.0f).build("scp427_1"));
    public static final RegistryObject<EntityType<SCP_096_Entity>> SCP_096 = ENTITY_TYPES.register("scp_096", () ->
            EntityType.Builder.of(SCP_096_Entity::new, MobCategory.MONSTER).sized(1f, 4f).build("scp_096"));
    public static final RegistryObject<EntityType<SCP_513_1_Entity>> SCP_513_1 = ENTITY_TYPES.register("scp_513_1", () ->
            EntityType.Builder.of(SCP_513_1_Entity::new, MobCategory.MONSTER).sized(1f, 3.0f).clientTrackingRange(64).build("scp_513_1"));
    public static final RegistryObject<EntityType<SCP_1048_Entity>> SCP_1048 = ENTITY_TYPES.register("scp_1048", () ->
            EntityType.Builder.of(SCP_1048_Entity::new, MobCategory.CREATURE).sized(0.6f, 1f).build("scp_1048"));
    public static final RegistryObject<EntityType<SCP_2295_Entity>> SCP_2295 = ENTITY_TYPES.register("scp_2295", () ->
            EntityType.Builder.of(SCP_2295_Entity::new, MobCategory.CREATURE).sized(0.6f, 1f).build("scp_2295"));
    public static final RegistryObject<EntityType<SCP_049_Entity>> SCP_049 = ENTITY_TYPES.register("scp_049", () ->
            EntityType.Builder.of(SCP_049_Entity::new, MobCategory.CREATURE).sized(0.6f, 2f).build("scp_049"));
    public static final RegistryObject<EntityType<SCP_106_Entity>> SCP_106 = ENTITY_TYPES.register("scp_106", () ->
            EntityType.Builder.of(SCP_106_Entity::new, MobCategory.MONSTER).sized(1f, 3f).build("scp_106"));
    public static final RegistryObject<EntityType<SCP_999_Entity>> SCP_999 = ENTITY_TYPES.register("scp_999", () ->
            EntityType.Builder.of(SCP_999_Entity::new, MobCategory.CREATURE).sized(1f, 1f).build("scp_999"));

    public static final RegistryObject<EntityType<BuilderNPC>> BUILDER_NPC = ENTITY_TYPES.register("builder_npc", () ->
            EntityType.Builder.of(BuilderNPC::new, MobCategory.MISC).sized(0.6F, 1.8F).build("builder_npc"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
