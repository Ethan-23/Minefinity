package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.context.BreakContext;

public class MetalDetector implements MiningAbility {

    @Override
    public void onBreak(BreakContext context) {

        int METAL_DETECT_CHANCE = 5;
        context.statsContext().addSpecialChance(METAL_DETECT_CHANCE);

        if(!context.statsContext().isSpecialDrop())
            return;

        playMetalDetectAnimation(context.player());


    }

    private void playMetalDetectAnimation(Player player){
        player.sendMessage("METAL DETECTOR ANIMATION!!! WOW!!!");
    }

}
