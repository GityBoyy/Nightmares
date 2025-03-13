package org.chubby.github.nightmares.common.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import org.chubby.github.nightmares.common.data.SanityData;

@EventBusSubscriber
public class SanityEvents {

    @SubscribeEvent
    public static void onKillMonster(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            SanityData sanity = SanityTracker.getSanityData(player);
            sanity.increaseSanity(5);
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        SanityData sanity = SanityTracker.getSanityData(player);
        float damage = event.getNewDamage();

        int sanityLoss = Math.max(1, Math.round(damage));
        sanity.decreaseSanity(sanityLoss);
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer && !player.level().isClientSide() && !event.updateLevel()) {
            SanityData sanity = SanityTracker.getSanityData(player);

            if (player.level().isDay()) {
                sanity.increaseSanity(20);
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        //TODO::SOME IMPLEMENTATION
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityData sanity = SanityTracker.getSanityData(player);


            switch (event.getTo().location().toString()) {
                case "minecraft:the_nether":

                    sanity.decreaseSanity(15);
                    break;
                case "minecraft:the_end":
                    sanity.decreaseSanity(25);
                    break;
                case "minecraft:overworld":
                    sanity.increaseSanity(10);
                    break;
                default:
                    break;
            }
        }
    }
}