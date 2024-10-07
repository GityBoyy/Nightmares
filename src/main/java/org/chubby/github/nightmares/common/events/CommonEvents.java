package org.chubby.github.nightmares.common.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.chubby.github.nightmares.Constants;
import org.chubby.github.nightmares.network.PayloadHandler;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents
{
    private static final String SANITY_TRACKER_KEY = "SanityTracker";

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player  = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer)
        {
            SanityTracker tracker = getOrCreateSanityTracker(player);
            PayloadHandler.setSanityLevel(player, tracker.getSanityLevel());
        }
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event)
    {
        Player player = event.getEntity();

        if (!player.level().isClientSide())
        {
            SanityTracker tracker = getOrCreateSanityTracker(player);

            tracker.updateSanity(player);

            saveSanityTracker(player, tracker);

            PayloadHandler.setSanityLevel(player, tracker.getSanityLevel());
        }
    }

    private static SanityTracker getOrCreateSanityTracker(Player player) {
        CompoundTag persistentData = player.getPersistentData();

        if (!persistentData.contains(SANITY_TRACKER_KEY)) {
            return new SanityTracker(100);
        } else {
            int sanityLevel = persistentData.getInt(SANITY_TRACKER_KEY);
            return new SanityTracker(sanityLevel);
        }
    }

    private static void saveSanityTracker(Player player, SanityTracker tracker) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(SANITY_TRACKER_KEY, tracker.getSanityLevel());
    }
}
