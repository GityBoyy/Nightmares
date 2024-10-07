package org.chubby.github.nightmares.utils;

import net.neoforged.bus.api.IEventBus;
import org.chubby.github.nightmares.common.init.ModAttachments;
import org.chubby.github.nightmares.common.init.ModBlocks;
import org.chubby.github.nightmares.common.init.ModItems;
import org.chubby.github.nightmares.common.init.ModTabs;

public class ModSetup
{
    public static void register(IEventBus eventBus)
    {
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModTabs.register(eventBus);
        ModAttachments.register(eventBus);
    }
}
