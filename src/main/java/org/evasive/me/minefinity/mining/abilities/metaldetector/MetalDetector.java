package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.context.HitContext;

public class MetalDetector implements MiningAbility {

    private static final int METAL_DETECT_CHANCE = 5;

    @Override
    public void applyStats(HitContext context) {
        context.getStatsContext().addSpecialChance(METAL_DETECT_CHANCE);
    }

}
