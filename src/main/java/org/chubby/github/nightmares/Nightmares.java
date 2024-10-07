package org.chubby.github.nightmares;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.chubby.github.nightmares.common.commands.GetSanityCommand;
import org.chubby.github.nightmares.network.handler.ClientPayloadHandler;
import org.chubby.github.nightmares.network.handler.ServerPayloadHandler;
import org.chubby.github.nightmares.network.paylod.SanityData;
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




    @EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        GetSanityCommand.register(event.getDispatcher());
    }
}
