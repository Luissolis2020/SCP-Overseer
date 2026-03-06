package net.luis.scpmod.event;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.ModEntities;
import net.luis.scpmod.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SCP_173.get(), SCP173Entity.createAttributes().build());
        event.put(ModEntities.SCP_427_1.get(), SCP427_1_Entity.createAttributes().build());
        event.put(ModEntities.SCP_096.get(), SCP_096_Entity.createAttributes().build());
        event.put(ModEntities.SCP_513_1.get(), SCP_513_1_Entity.createAttributes().build());
        event.put(ModEntities.SCP_1048.get(), SCP_1048_Entity.createAttributes().build());
        event.put(ModEntities.SCP_2295.get(), SCP_2295_Entity.createAttributes().build());
        event.put(ModEntities.SCP_049.get(), SCP_049_Entity.createAttributes().build());
        event.put(ModEntities.SCP_106.get(), SCP_106_Entity.createAttributes().build());
        event.put(ModEntities.SCP_999.get(), SCP_999_Entity.createAttributes().build());

        event.put(ModEntities.BUILDER_NPC.get(), BuilderNPC.createAttributes().build());
    }

}
