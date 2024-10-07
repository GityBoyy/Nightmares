package org.chubby.github.nightmares.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.chubby.github.nightmares.network.PayloadHandler;

public final class SanityTracker
{
    private int sanityLevel;
    private int darknessTime;
    private int lightTime;

    private static final int MAX_SANITY = 100;
    private static final int MIN_SANITY = 0;
    private static final int TICK_INTERVAL = 20;

    public SanityTracker(int initialSanity) {
        this.sanityLevel = initialSanity;
        this.darknessTime = 0;
        this.lightTime = 0;
    }

    public void updateSanity(Player player) {
        if (player.level().getGameTime() % TICK_INTERVAL == 0) {
            updateSanityBasedOnDarkness(player);
        }
    }

    private void updateSanityBasedOnDarkness(Player player) {
        Level world = player.level();
        BlockPos playerPos = player.blockPosition();

        int blockLight = world.getBrightness(LightLayer.BLOCK, playerPos);
        int skyLight = world.getBrightness(LightLayer.SKY, playerPos);

        boolean isInDarkness = blockLight <= 7 && skyLight <= 7;

        if (isInDarkness) {
            darknessTime++;
            lightTime = 0;
            if (darknessTime >= 5) {
                sanityLevel = Math.max(MIN_SANITY, sanityLevel - 1);
                darknessTime = 0;
            }
        } else {
            lightTime++;
            darknessTime = 0;
            if (lightTime >= 5) {
                sanityLevel = Math.min(MAX_SANITY, sanityLevel + 1);
                lightTime = 0;
            }
        }

        PayloadHandler.setSanityLevel(player, sanityLevel);

        System.out.println("Sanity Level: " + sanityLevel);
    }

    public int getSanityLevel() {
        return sanityLevel;
    }

    public int getMaxSanity() {
        return MAX_SANITY;
    }

    public int getMinSanity() {
        return MIN_SANITY;
    }
}
