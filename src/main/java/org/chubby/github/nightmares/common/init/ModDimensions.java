package org.chubby.github.nightmares.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.nightmares.Constants;
import org.chubby.github.nightmares.common.worldgen.dimension.NightmareDimension;

public class ModDimensions
{
    public static final DeferredRegister<DimensionType> TYPE =
            DeferredRegister.create(Registries.DIMENSION_TYPE, Constants.MODID);



    public static void register(IEventBus bus)
    {
        TYPE.register(bus);
    }
}
