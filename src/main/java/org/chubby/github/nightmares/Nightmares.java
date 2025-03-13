package org.chubby.github.nightmares;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.chubby.github.nightmares.common.commands.GetSanityCommand;
import org.chubby.github.nightmares.utils.ModSetup;

@Mod(Constants.MODID)
public class Nightmares {


    public Nightmares(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        ModSetup.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        GetSanityCommand.register(event.getDispatcher());
    }
}
