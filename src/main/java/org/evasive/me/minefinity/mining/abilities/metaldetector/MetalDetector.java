package org.evasive.me.minefinity.mining.abilities.metaldetector;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.mining.abilities.MiningAbility;
import org.evasive.me.minefinity.mining.context.BreakContext;

public class MetalDetector implements MiningAbility {

    @Override
    public void onBreak(BreakContext context) {

        context.statsContext().addSpecialChance(5);

        if(!context.statsContext().isSpecialDrop())
            return;

        playMetalDetectAnimation(context.player());


    }

    private void playMetalDetectAnimation(Player player){
        player.sendMessage("METAL DETECTOR ANIMATION!!! WOW!!!");
    }

}
