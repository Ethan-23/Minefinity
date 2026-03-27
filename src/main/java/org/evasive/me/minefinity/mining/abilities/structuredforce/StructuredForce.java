package org.evasive.me.minefinity.mining.abilities.structuredforce;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.abilities.structuredforce.data.StreakMap;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.UUID;

public class StructuredForce implements MiningAbility {

    private final StreakMap streakMap;

    public StructuredForce(StreakMap streakMap) {
        this.streakMap = streakMap;
    }

    @Override
    public void onHit(HitContext context) {
        UUID uuid = context.getUUID();
        BaseBlock baseBlock = streakMap.getBaseBlock(uuid);
        if(baseBlock == null)
            return;
        int streak = streakMap.getStreak(uuid);
        context.getStatsContext().addSpeed(Math.min(5, streak * 0.02f));
    }

    @Override
    public void onBreak(BreakContext context) {
        Player player = context.getPlayer();
        UUID uuid = context.getUUID();
        BaseBlock baseBlock = context.getBaseBlock();

        streakMap.addStreak(uuid, baseBlock);
        player.sendActionBar(TextConversions.parse("<gold>" + TextConversions.formatItemName(baseBlock.name()) + " Streak: <gray>" + streakMap.getStreak(uuid)));
    }
}
