package net.luis.scpmod.event;

import net.luis.scpmod.entity.ai.AvoidSCP668Goal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SCP668MobFearHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof PathfinderMob mob) {
            if (shouldFearSCP668(mob)) {
                mob.goalSelector.addGoal(2, new AvoidSCP668Goal(mob, 1, 10.0));
            }
        }
    }

    private static boolean shouldFearSCP668(PathfinderMob mob) {
        return mob.getType() == EntityType.ZOMBIE ||
                mob.getType() == EntityType.SKELETON ||
                mob.getType() == EntityType.SPIDER ||
                mob.getType() == EntityType.CREEPER ||
                mob.getType() == EntityType.CAVE_SPIDER ||
                mob.getType() == EntityType.HUSK ||
                mob.getType() == EntityType.PIGLIN ||
                mob.getType() == EntityType.PIGLIN_BRUTE ||
                mob.getType() == EntityType.WITCH ||
                mob.getType() == EntityType.WITHER_SKELETON ||
                mob.getType() == EntityType.ZOMBIE_VILLAGER ||
                mob.getType() == EntityType.PILLAGER ||
                mob.getType() == EntityType.VILLAGER ||
                mob.getType() == EntityType.WANDERING_TRADER;
    }
}
