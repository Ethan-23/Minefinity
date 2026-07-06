package org.evasive.me.minefinity.mining.utils;

/**
 * Helper class for mining calculations
 */
public class MiningProgress {

    public static final int ANIMATION_STAGES = 10;
    public static final int MAX_SPEED_DENOMINATION = 4;
    private static final float SPEED_DENOMINATION = 10;

    /**
     * Calculates progress made on a block. Has a hard cap of 25% of the blocks health to prevent instant break
     * @param miningSpeed Players mining speed
     * @param health Blocks health
     * @return progress being dealt to block
     */
    public static float hitProgress(float miningSpeed, int health) {
        float progress = miningSpeed / SPEED_DENOMINATION;

        float sum = (float) health / MAX_SPEED_DENOMINATION;

        return Math.min(progress, sum);
    }

    /**
     * Calculates the progress between each animation stage
     * @param totalBlockHealth blocks total health
     * @return amount of health per stage
     */
    public static int stageStep(int totalBlockHealth) {
        return Math.max(1, totalBlockHealth / ANIMATION_STAGES);
    }

    /**
     * Checks if the current progress brings the block to the next animation stage
     * @param currentProgress progress before hit
     * @param totalProgress progress after hit
     * @param animationStep amount per stage
     * @return True/False if the stage has been increased
     */
    public static boolean crossedStage(float currentProgress, float totalProgress, int animationStep) {
        return (int) totalProgress / animationStep > (int) currentProgress / animationStep || currentProgress == 0;
    }

    /**
     * Compares breaking powers
     * @param playerBreakingPower players breaking power stat
     * @param blockBreakingPower blocks breaking power stat
     * @return True/False if the player can break the block
     */
    public static boolean canBreak(int playerBreakingPower, int blockBreakingPower){
        return playerBreakingPower >= blockBreakingPower;
    }
}
