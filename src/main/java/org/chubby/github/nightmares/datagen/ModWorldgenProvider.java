package org.chubby.github.nightmares.datagen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.chubby.github.nightmares.Constants;
import org.chubby.github.nightmares.common.worldgen.dimension.NightmareDimension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldgenProvider extends DatapackBuiltinEntriesProvider
{
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, Lifecycle.experimental() ,NightmareDimension::boostrap)
            .add(Registries.LEVEL_STEM, Lifecycle.experimental() ,NightmareDimension::levelStem);

    public ModWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, BUILDER, Set.of(Constants.MODID));
        System.out.println("YESSSSSSS");
    }
}
