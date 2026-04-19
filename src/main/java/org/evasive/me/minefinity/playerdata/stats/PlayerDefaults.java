package org.evasive.me.minefinity.playerdata.stats;

import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.EnumMap;

public class PlayerDefaults {

    public static final EnumMap<Stats, Integer> BASE = new EnumMap<>(Stats.class);

    static {
        //Combat
        BASE.put(Stats.HEALTH, 20);
        BASE.put(Stats.REGENERATION, 1);
        BASE.put(Stats.DEFENSE, 0);
        BASE.put(Stats.STRENGTH, 5);
        BASE.put(Stats.CRITICAL_CHANCE, 0);
        BASE.put(Stats.CRITICAL_DAMAGE, 10);
        BASE.put(Stats.SPEED, 5);

        //Skills
        BASE.put(Stats.BREAKING_POWER, 0);
        BASE.put(Stats.MINING_SPEED, 5);
        BASE.put(Stats.MINING_FORTUNE, 0);
        BASE.put(Stats.WOODCUTTING_POWER, 0);
        BASE.put(Stats.WOODCUTTING_FORTUNE, 0);
        BASE.put(Stats.FISHING_SPEED, 0);
        BASE.put(Stats.FISHING_LUCK, 0);
        BASE.put(Stats.ANIMAL_SPEED, 0);
        BASE.put(Stats.ANIMAL_FORTUNE, 0);
    }

}
