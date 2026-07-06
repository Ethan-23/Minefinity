package org.evasive.me.minefinity.mining.abilities.earlybird;

import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.core.data.BaseBlock;

public class EarlyBird implements MiningAbility {

    private final static float MINING_SPEED_MULTIPLIER = 1.1f;

    @Override
    public void onHit(HitContext context) {
        BaseBlock baseBlock = context.getBaseBlock();
        int breakingPower = baseBlock.breakingPower();

        if (breakingPower != 1)
            return;

        context.getStatsContext().multiplySpeed(MINING_SPEED_MULTIPLIER);
    }
}
