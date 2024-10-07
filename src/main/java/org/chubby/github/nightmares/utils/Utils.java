package org.chubby.github.nightmares.utils;

import net.minecraft.resources.ResourceLocation;
import org.chubby.github.nightmares.Constants;

public class Utils
{
    public static ResourceLocation resource(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID,path);
    }
}
