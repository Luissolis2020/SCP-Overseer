package net.luis.scpmod.event;

import net.luis.scpmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = "scpmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCP124EventHandler {

    private static final Map<ResourceKey<Level>, Set<BlockPos>> trackedByDim = new HashMap<>();

    private static Set<BlockPos> getTracked(ResourceKey<Level> dim) {
        return trackedByDim.computeIfAbsent(dim, k -> new HashSet<>());
    }

    public static void trackBlock(ServerLevel level, BlockPos pos) {
        getTracked(level.dimension()).add(pos.immutable());
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide || event.phase != TickEvent.Phase.END) return;
        ServerLevel level = (ServerLevel) event.level;
        Set<BlockPos> tracked = getTracked(level.dimension());
        Set<BlockPos> toRemove = new HashSet<>();

        for (BlockPos pos : tracked) {
            BlockState currentState = level.getBlockState(pos);
            if (currentState.is(Blocks.DIRT)
                    || currentState.is(Blocks.GRASS_BLOCK)
                    || currentState.is(Blocks.COARSE_DIRT)
                    || currentState.is(Blocks.PODZOL)
                    || currentState.is(Blocks.ROOTED_DIRT)
                    || currentState.is(Blocks.MOSS_BLOCK)
                    || currentState.is(Blocks.MYCELIUM)
                    || currentState.is(Blocks.DIRT_PATH)) {
                level.setBlock(pos, ModBlocks.SCP_124.get().defaultBlockState(), 3);
                toRemove.add(pos);
            } else if (currentState.is(ModBlocks.SCP_124.get())) {
                toRemove.add(pos);
            }
        }
        tracked.removeAll(toRemove);
    }

    @SubscribeEvent
    public static void onTreeGrow(SaplingGrowTreeEvent event) {
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos saplingPos = event.getPos();

        final int radius = 4;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos soilPos = saplingPos.offset(dx, -1, dz);
                if (level.getBlockState(soilPos).is(ModBlocks.SCP_124.get())) {
                    trackBlock(level, soilPos);
                }
            }
        }
    }

}
