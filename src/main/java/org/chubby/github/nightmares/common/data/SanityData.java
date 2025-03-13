package org.chubby.github.nightmares.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class SanityData {

    public static final Codec<SanityData> CODEC = RecordCodecBuilder.create(inst->inst.group(
            SanityStages.CODEC.fieldOf("currentStage").forGetter(SanityData::getCurrentStage),
            Codec.INT.fieldOf("maxSanityLevel").forGetter(SanityData::getMaxSanityLevel),
            Codec.INT.fieldOf("currentSanityLevel").forGetter(SanityData::getCurrentSanityLevel),
            Codec.FLOAT.fieldOf("deathChances").forGetter(SanityData::getDeathChances),
            Codec.INT.fieldOf("ticksInCurrentStage").forGetter(SanityData::getTicksInCurrentStage),
            Codec.BOOL.fieldOf("isImmune").forGetter(SanityData::isImmune)
    ).apply(inst, SanityData::new));

    private SanityStages currentStage;
    private final int maxSanityLevel;
    private int currentSanityLevel;
    private float deathChances;
    private int ticksInCurrentStage;
    private boolean isImmune;
    private long lastDecayTime;

    public SanityData(SanityStages currentStage, int maxSanityLevel, int currentSanityLevel,
                      float deathChances, int ticksInCurrentStage, boolean isImmune) {
        this.currentStage = currentStage;
        this.maxSanityLevel = maxSanityLevel;
        this.currentSanityLevel = currentSanityLevel;
        this.deathChances = deathChances;
        this.ticksInCurrentStage = ticksInCurrentStage;
        this.isImmune = isImmune;
        this.lastDecayTime = 0;
    }

    public enum SanityStages {
        STABLE,      // 100% - 80% - No effects
        UNEASY,      // 80% - 60% - Creepy sounds
        DISTORTED,   // 60% - 40% - Visual distortions
        PARANOID,    // 40% - 20% - Hallucinations
        FRACTURED,   // 20% - 1% - Reality bending
        NIGHTMARE;   // 0% - Full nightmare events

        public static final Codec<SanityStages> CODEC = Codec.STRING.xmap(SanityStages::valueOf, SanityStages::name);

        public String getTranslationKey() {
            return "sanity.stage." + name().toLowerCase();
        }

        public SanityStages getNextWorse() {
            int ordinal = this.ordinal();
            if (ordinal < SanityStages.values().length - 1) {
                return SanityStages.values()[ordinal + 1];
            }
            return this;
        }

        public SanityStages getNextBetter() {
            int ordinal = this.ordinal();
            if (ordinal > 0) {
                return SanityStages.values()[ordinal - 1];
            }
            return this;
        }
    }

    public SanityData(int maxSanity) {
        this.maxSanityLevel = maxSanity;
        this.currentSanityLevel = maxSanity;
        this.currentStage = SanityStages.STABLE;
        this.deathChances = 0.0f;
        this.ticksInCurrentStage = 0;
        this.isImmune = false;
        this.lastDecayTime = 0;
    }

    /** Decrease sanity over time or due to events */
    public void decreaseSanity(int amount) {
        if (isImmune) return;

        int oldSanityLevel = this.currentSanityLevel;
        SanityStages oldStage = this.currentStage;

        this.currentSanityLevel = Math.max(0, this.currentSanityLevel - amount);
        updateSanityStage();

        // Reset timer if stage changed
        if (oldStage != this.currentStage) {
            this.ticksInCurrentStage = 0;
        }
    }

    /** Increase sanity (e.g., through items or actions) */
    public void increaseSanity(int amount) {
        int oldSanityLevel = this.currentSanityLevel;
        SanityStages oldStage = this.currentStage;

        this.currentSanityLevel = Math.min(maxSanityLevel, this.currentSanityLevel + amount);
        updateSanityStage();

        // Reset timer if stage changed
        if (oldStage != this.currentStage) {
            this.ticksInCurrentStage = 0;
        }
    }

    /** Sets sanity to a specific level */
    public void setSanity(int level) {
        SanityStages oldStage = this.currentStage;

        this.currentSanityLevel = Math.max(0, Math.min(maxSanityLevel, level));
        updateSanityStage();

        if (oldStage != this.currentStage) {
            this.ticksInCurrentStage = 0;
        }
    }

    /** Set sanity immunity (for creative mode or special items) */
    public void setImmunity(boolean immune) {
        this.isImmune = immune;
    }

    public boolean isImmune() {
        return isImmune;
    }

    /** Increment the time spent in current stage */
    public void incrementTicksInStage() {
        this.ticksInCurrentStage++;
    }

    public int getTicksInCurrentStage() {
        return ticksInCurrentStage;
    }

    private void updateSanityStage() {
        int sanityPercent = (currentSanityLevel * 100) / maxSanityLevel;
        SanityStages newStage;

        if (sanityPercent >= 80) {
            newStage = SanityStages.STABLE;
            deathChances = 0.0f;
        } else if (sanityPercent >= 60) {
            newStage = SanityStages.UNEASY;
            deathChances = 0.05f;
        } else if (sanityPercent >= 40) {
            newStage = SanityStages.DISTORTED;
            deathChances = 0.1f;
        } else if (sanityPercent >= 20) {
            newStage = SanityStages.PARANOID;
            deathChances = 0.2f;
        } else if (sanityPercent > 0) {
            newStage = SanityStages.FRACTURED;
            deathChances = 0.4f;
        } else {
            newStage = SanityStages.NIGHTMARE;
            deathChances = 0.8f;
        }

        if (newStage != currentStage) {
            ticksInCurrentStage = 0;
            currentStage = newStage;
        }
    }

    public SanityStages getCurrentStage() {
        return currentStage;
    }

    public int getMaxSanityLevel() {
        return maxSanityLevel;
    }

    public int getCurrentSanityLevel() {
        return currentSanityLevel;
    }

    public int getSanityPercentage() {
        return (currentSanityLevel * 100) / maxSanityLevel;
    }

    public float getDeathChances() {
        return deathChances;
    }

    public void setLastDecayTime(long time) {
        this.lastDecayTime = time;
    }

    public long getLastDecayTime() {
        return lastDecayTime;
    }

    public int getStageColor() {
        return switch (currentStage) {
            case STABLE -> 0x00FF00;    // Green
            case UNEASY -> 0xAAFF00;    // Yellow-green
            case DISTORTED -> 0xFFFF00; // Yellow
            case PARANOID -> 0xFFA500;  // Orange
            case FRACTURED -> 0xFF0000; // Red
            case NIGHTMARE -> 0x800080; // Purple
            default -> 0xFFFFFF;        // White (fallback)
        };
    }

    public float getStageProgress() {
        int lowerBound, upperBound;

        switch (currentStage) {
            case STABLE: lowerBound = 80; upperBound = 100; break;
            case UNEASY: lowerBound = 60; upperBound = 80; break;
            case DISTORTED: lowerBound = 40; upperBound = 60; break;
            case PARANOID: lowerBound = 20; upperBound = 40; break;
            case FRACTURED: lowerBound = 1; upperBound = 20; break;
            case NIGHTMARE: lowerBound = 0; upperBound = 1; break;
            default: return 0;
        }

        int percent = getSanityPercentage();
        if (percent >= upperBound) return 0;
        if (percent <= lowerBound) return 1;

        return 1.0f - ((percent - lowerBound) / (float)(upperBound - lowerBound));
    }
}