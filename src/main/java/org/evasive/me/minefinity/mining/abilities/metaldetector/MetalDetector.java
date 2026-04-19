package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.context.BreakContext;

public class MetalDetector implements MiningAbility {

    @Override
    public void onBreak(BreakContext context) {

        context.getStatsContext().addSpecialChance(5);

        if(!context.getStatsContext().isSpecialDrop())
            return;

        playMetalDetectAnimation(context.getPlayer());


    }

    private void playMetalDetectAnimation(Player player){
        player.sendMessage("METAL DETECTOR ANIMATION!!! WOW!!!");
    }

}
