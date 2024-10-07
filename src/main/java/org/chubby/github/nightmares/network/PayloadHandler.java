package org.chubby.github.nightmares.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.chubby.github.nightmares.Constants;
import org.chubby.github.nightmares.network.handler.ClientPayloadHandler;
import org.chubby.github.nightmares.network.paylod.SanityData;

@EventBusSubscriber(modid = Constants.MODID,bus = EventBusSubscriber.Bus.MOD)
public class PayloadHandler
{
    public static void setSanityLevel(Player player, int sanityLevel)
    {
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SanityData(sanityLevel));
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SanityData.TYPE,
                SanityData.STREAM_CODEC,
                ClientPayloadHandler::handleSanityData
        );
    }
}
