package org.evasive.me.minefinity.automation.miner.gui.selection;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.resourceblock.BlockType;

public class MinerBlockSelectionHandler {


    public void handleBlockSelection(Player player, int clickedSlot){
        int clickedBlockTier = clickedSlot - 1;
        int playerBlockTier = Minefinity.playerManager.getBlockTier(player);
        if(clickedBlockTier > playerBlockTier) return;
        Minefinity.playerManager.getPlayerData(player).getAutoMiner().setBlockType(BlockType.values()[clickedSlot - 1]);
    }


    public void handleNoneSelection(Player player) {
        Minefinity.playerManager.getPlayerData(player).getAutoMiner().setBlockType(null);
    }

    public void handleBackButton(Player player){
        new MinerMainGui(player).open();
    }

}
