package org.chubby.github.nightmares.common.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.chubby.github.nightmares.Constants;
import org.chubby.github.nightmares.common.init.ModDimensions;

import java.util.List;
import java.util.OptionalLong;

public class NightmareDimension
{
    public static final ResourceKey<LevelStem> NIGHTMARE_KEY = ResourceKey.create(
            Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath( Constants.MODID, "nightmare_stem")
    );
    public static final ResourceKey<DimensionType> NIGHTMARE_DIMENSION =
            ResourceKey.create(Registries.DIMENSION_TYPE,ResourceLocation.fromNamespaceAndPath( Constants.MODID, "nightmare_dimension"));



    public static void boostrap(BootstrapContext<DimensionType> ctx)
    {
        ctx.register(NIGHTMARE_DIMENSION,new DimensionType(
                OptionalLong.of(1200),
                false,
                true,
                false,
                true,
                1,
                false,
                true,
                -128,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.3F,
                new DimensionType.MonsterSettings(true,false, ConstantInt.of(7),3)
        ));

    }

    public static void levelStem(BootstrapContext<LevelStem> context)
    {
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);


        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(NIGHTMARE_DIMENSION), noiseBasedChunkGenerator);

        context.register(NIGHTMARE_KEY, stem);
    }
}
