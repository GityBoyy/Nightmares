package org.chubby.github.nightmares.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.chubby.github.nightmares.common.data.SanityData;
import org.chubby.github.nightmares.common.init.ModAttachments;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber
public final class SanityTracker {
    // Track last decay tick to avoid expensive calculations every tick
    private static final Map<UUID, Integer> lastDecayTick = new HashMap<>();

    // Constants for sanity decay
    private static final int SANITY_DECAY_INTERVAL = 1200; // Every 60 seconds (20 ticks * 60)
    private static final int BASE_SANITY_LOSS = 1;
    private static final int DARKNESS_SANITY_LOSS = 3;
    private static final int UNDERGROUND_SANITY_LOSS = 4;
    private static final int MONSTER_NEARBY_LOSS = 5;
    private static final int DEATH_SANITY_LOSS = 15;

    // Constants for sanity regeneration
    private static final int DAYLIGHT_SANITY_GAIN = 2;
    private static final int SLEEP_SANITY_GAIN = 20;
    private static final int FOOD_SANITY_GAIN = 5;

    private static final Random random = new Random();

    /** Gets a player's sanity data */
    public static SanityData getSanityData(Player player) {
        if (player.hasData(ModAttachments.SANITY)) {
            return player.getData(ModAttachments.SANITY);
        }

        // Create default sanity data if none exists
        SanityData data = new SanityData(100);
        player.setData(ModAttachments.SANITY, data);
        return data;
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().hasData(ModAttachments.SANITY)) {
            event.getEntity().setData(ModAttachments.SANITY, new SanityData(100));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getOriginalDamage() >= player.getHealth()) {
            SanityData sanity = getSanityData(player);
            sanity.decreaseSanity(DEATH_SANITY_LOSS);
        }
    }

    /** Handles passive sanity changes over time */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // Only process sanity changes at certain intervals to improve performance
        int currentTick = player.tickCount;
        int lastTick = lastDecayTick.getOrDefault(player.getUUID(), 0);

        if (currentTick - lastTick >= SANITY_DECAY_INTERVAL) {
            lastDecayTick.put(player.getUUID(), currentTick);

            SanityData sanity = getSanityData(player);
            Level level = player.level();

            // Apply appropriate sanity changes
            processSanityChanges(player, sanity, level);

            // Apply effects based on current sanity
            applySanityEffects(player, sanity);
        }
    }

    private static void processSanityChanges(ServerPlayer player, SanityData sanity, Level level) {
        BlockPos pos = player.blockPosition();
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        long timeOfDay = level.getDayTime() % 24000;
        boolean isNight = timeOfDay >= 13000 && timeOfDay <= 23000;
        boolean isUnderground = pos.getY() < 40 && skyLight == 0;

        // Calculate sanity changes
        int sanityChange = 0;

        // Factors that decrease sanity
        if (isNight) {
            sanityChange -= BASE_SANITY_LOSS;

            if (blockLight < 7) {
                sanityChange -= DARKNESS_SANITY_LOSS;
            }
        }

        if (isUnderground) {
            sanityChange -= UNDERGROUND_SANITY_LOSS;
        }

        // Check for nearby monsters (simplified version)
        if (!player.level().getNearbyEntities(Monster.class, TargetingConditions.DEFAULT,
                player,player.getBoundingBox().inflate(8.0D)).isEmpty()) {
            sanityChange -= MONSTER_NEARBY_LOSS;
        }

        // Factors that increase sanity
        if (!isNight && player.level().isDay() && !player.isInWaterOrRain()) {
            sanityChange += DAYLIGHT_SANITY_GAIN;
        }

        // Apply the net sanity change
        if (sanityChange < 0) {
            sanity.decreaseSanity(-sanityChange);
        } else if (sanityChange > 0) {
            sanity.increaseSanity(sanityChange);
        }
    }

    /** Applies effects based on the player's sanity stage */
    private static void applySanityEffects(ServerPlayer player, SanityData sanity) {
        switch (sanity.getCurrentStage()) {
            case UNEASY:
                if (random.nextFloat() < 0.05f) {
                    // Play ambient whisper sound (implementation depends on your sound system)
                    // Example: player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    //          ModSounds.WHISPER, SoundSource.AMBIENT, 0.5f, 0.8f + random.nextFloat() * 0.4f);
                }
                break;

            case DISTORTED:
                // Send visual distortion packet to client (implementation needed)
                if (random.nextFloat() < 0.1f) {
                    // Example: NetworkHandler.sendToPlayer(new DistortionPacket(0.2f), player);
                }
                break;

            case PARANOID:
                // Spawn fake entities (client-side implementation needed)
                if (random.nextFloat() < 0.08f) {
                    // Example: NetworkHandler.sendToPlayer(new SpawnFakeEntityPacket(), player);
                }
                break;

            case FRACTURED:
                // World warping effects
                if (random.nextFloat() < 0.1f) {
                    // Example: player.teleport(player.getX() + random.nextFloat() * 2 - 1,
                    //                          player.getY(),
                    //                          player.getZ() + random.nextFloat() * 2 - 1);
                }
                break;

            case NIGHTMARE:
                // Intense hallucinations and possible damage
                if (random.nextFloat() < sanity.getDeathChances()) {
                    // Example: player.hurt(player.damageSources().magic(), 2.0f);
                    // Example: NetworkHandler.sendToPlayer(new NightmareEffectPacket(), player);
                }
                break;

            default:
                break;
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(CanContinueSleepingEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SanityData sanity = getSanityData(player);
            sanity.increaseSanity(SLEEP_SANITY_GAIN);
        }
    }

    /** Helper method to determine if time is between sunset and early night */
    private static boolean isEvening(Level level) {
        long time = level.getDayTime() % 24000;
        return time >= 12000 && time < 14000;
    }
}