package org.evasive.me.minefinity.mining.abilities.structuredforce;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.mining.abilities.structuredforce.data.StreakMap;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;
import org.evasive.me.minefinity.core.data.BaseBlock;

import java.util.UUID;

public class StructuredForce implements MiningAbility {

    private final StreakMap streakMap;
    private final AbilityNotifier abilityNotifier;

    private static final float MAX_SPEED_INCREASE = 5f;
    private static final float SPEED_INCREASE = 0.02f;


    public StructuredForce(StreakMap streakMap, AbilityNotifier abilityNotifier) {
        this.streakMap = streakMap;
        this.abilityNotifier = abilityNotifier;
    }

    @Override
    public void onHit(HitContext context) {
        UUID uuid = context.getUUID();
        BaseBlock baseBlock = streakMap.getBaseBlock(uuid);
        if(baseBlock == null)
            return;
        int streak = streakMap.getStreak(uuid);
        context.getStatsContext().addSpeed(Math.min(MAX_SPEED_INCREASE, streak * SPEED_INCREASE));
    }

    @Override
    public void onBreak(BreakContext context) {
        Player player = context.player();
        UUID uuid = context.getUUID();
        BaseBlock baseBlock = context.baseBlock();

        streakMap.addStreak(uuid, baseBlock);
        sendAbilityNotification(player, uuid);
    }

    private void sendAbilityNotification(Player player, UUID uuid){
        abilityNotifier.abilityNotification(player, PickaxeAbilities.STRUCTURED_FORCE, "Streak: <gray>" + streakMap.getStreak(uuid));

    }
}
