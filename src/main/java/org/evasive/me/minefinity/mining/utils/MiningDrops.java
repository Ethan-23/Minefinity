package org.evasive.me.minefinity.mining.utils;

import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.data.CustomItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiningDrops {

    private static final int SPECIAL_PERCENT_ROLL_MIN = 1;
    private static final int SPECIAL_PERCENT_ROLL_MAX = 101; // Exclusive Bound

    private static final int FORTUNE_PERCENT_ROLL_MIN = 1;
    private static final int FORTUNE_PERCENT_ROLL_MAX = 101; // Exclusive Bound

    /**
     * Adds block drops from broken block
     * @param drops List of custom items being edited
     * @param block Block being broken
     * @param fortune Players fortune stat
     * @param specialChance players special chance
     * @return returns true/false if the drop is special
     */
    public static boolean addBlockDrops(List<CustomItemStack> drops, BaseBlock block, float fortune, int specialChance){
        boolean special = isSpecialDrop(block, specialChance);
        String dropId = special ? block.specialBlockDropId() : block.blockDropId();

        if(dropId == null){
            Minefinity.SendLogMessage("Invalid ItemID from block: " + block.name());
            return false;
        }

        int amount = 1 + calculateFortuneDrops(fortune);
        drops.add(new CustomItemStack(dropId, amount));
        return special;
    }

    /**
     * Calculates the players fortune drops
     * @param fortuneStat players fortune stat
     * @return amount of drops added from fortune
     */
    public static int calculateFortuneDrops(float fortuneStat){
        Random random = ThreadLocalRandom.current();

        int guaranteed = (int) fortuneStat / 100;
        int remainder = (int) (fortuneStat % 100);

        int randomNum = random.nextInt(FORTUNE_PERCENT_ROLL_MIN, FORTUNE_PERCENT_ROLL_MAX);

        return guaranteed + (randomNum <= remainder ? 1 : 0);
    }

    /**
     * Checks if you get a special drop from the block
     * @param block block type being broken
     * @param specialChance special drop chance
     * @return true or false for special drop
     */
    public static boolean isSpecialDrop(BaseBlock block, int specialChance){
        return block.specialBlockDropId() != null && ThreadLocalRandom.current().nextInt(SPECIAL_PERCENT_ROLL_MIN, SPECIAL_PERCENT_ROLL_MAX) <= specialChance;
    }

}
