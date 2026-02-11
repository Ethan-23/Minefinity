package org.evasive.me.minevolutionCore.automation.miner.gui.selection;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.automation.miner.gui.main.MinerMainGui;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;

public class MinerBlockSelectionHandler {


    public void handleBlockSelection(Player player, int clickedSlot){
        int clickedBlockTier = clickedSlot - 1;
        int playerBlockTier = MinevolutionCore.playerManager.getBlockTier(player);
        if(clickedBlockTier > playerBlockTier) return;
        MinevolutionCore.playerManager.getPlayerData(player).getAutoMiner().setBlockType(BlockType.values()[clickedSlot - 1]);
    }


    public void handleNoneSelection(Player player) {
        MinevolutionCore.playerManager.getPlayerData(player).getAutoMiner().setBlockType(null);
    }

    public void handleBackButton(Player player){
        new MinerMainGui(player).open();
    }

}
