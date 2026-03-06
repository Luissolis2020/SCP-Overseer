package net.luis.scpmod.worldgen.dimension;

import net.luis.scpmod.SCPMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<DimensionType> POCKET_DIM_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "pocket_dim_type"));

    public static final ResourceKey<Level> POCKET_DIM_LEVEL =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "pocket_dimension"));

    public static final ResourceKey<LevelStem> POCKET_LEVEL_STEM =
            ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "pocket_dimension"));

    public static void bootstrap(BootstapContext<LevelStem> context) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureSet> structures = context.lookup(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);

        Holder.Reference<DimensionType> pocketType = dimensionTypes.getOrThrow(ModDimensions.POCKET_DIM_TYPE);
        Holder<Biome> biome = biomes.getOrThrow(Biomes.THE_VOID); // or PLAINS if you want sky

        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                Optional.empty(),  // No structure overrides
                biome,
                List.of()          // No lakes
        );

        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        settings.getLayersInfo().add(new FlatLayerInfo(5, Blocks.STONE));
        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.AIR));
        settings.updateLayers(); // VERY IMPORTANT!

        context.register(ModDimensions.POCKET_LEVEL_STEM, new LevelStem(
                pocketType,
                new FlatLevelSource(settings)
        ));
    }

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(ModDimensions.POCKET_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000L),         // fixed_time (midnight)
                false,                           // has_skylight
                true,                            // has_ceiling
                false,                           // ultrawarm
                false,                           // natural
                1.0,                             // coordinate_scale
                false,                           // bed_works
                false,                           // respawn_anchor_works
                0,                               // min_y
                256,                             // height
                256,                             // logical_height
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn (TagKey<Block>)
                ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"), // effectsLocation
                0.0f,                            // ambient light
                new DimensionType.MonsterSettings(
                        false,                      // piglinSafe
                        false,                      // hasRaids
                        UniformInt.of(0, 15),       // monsterSpawnLightTest
                        0                           // monsterSpawnBlockLightLimit
                )
        ));
    }

    public static ServerLevel getPocketDimension(MinecraftServer server) {
        return server.getLevel(POCKET_DIM_LEVEL);
    }
}


