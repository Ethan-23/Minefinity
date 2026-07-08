package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.AbilityNotifier;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.mining.context.BreakContext;
import org.evasive.me.minefinity.mining.context.HitContext;

public class MetalDetector implements MiningAbility {

    private static final int METAL_DETECT_CHANCE = 5;
    private final AbilityNotifier abilityNotifier;

    public MetalDetector(AbilityNotifier abilityNotifier) {
        this.abilityNotifier = abilityNotifier;
    }

    @Override
    public void applyStats(HitContext context) {
        context.getStatsContext().addSpecialChance(METAL_DETECT_CHANCE);
    }

    @Override
    public void onBreak(BreakContext context) {
        if(!context.statsContext().isSpecialDrop())
            return;

        sendMetalDetectMessage(context.player());
    }

    private void sendMetalDetectMessage(Player player){
        abilityNotifier.abilityNotification(player, PickaxeAbilities.METAL_DETECT, "Proc");
    }

}
