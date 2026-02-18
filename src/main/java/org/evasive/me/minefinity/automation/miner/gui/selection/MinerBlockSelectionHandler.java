package org.evasive.me.minefinity.automation.miner.gui.selection;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.player.sevices.AutoMinerService;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.resourceblock.BlockType;

public class MinerBlockSelectionHandler {

    BlockTierService blockTierService = Minefinity.core.getBlockTierService();
    AutoMinerService autoMinerService = Minefinity.core.getAutoMinerService();

    public void handleBlockSelection(Player player, int clickedSlot){
        int clickedBlockTier = clickedSlot - 1;
        int playerBlockTier = blockTierService.getBlockTier(player).ordinal();
        if(clickedBlockTier > playerBlockTier) return;
        autoMinerService.setAutoMinerBlockType(player, BlockType.values()[clickedSlot - 1]);
    }

    public void handleNoneSelection(Player player) {
        autoMinerService.setAutoMinerBlockType(player,null);
    }

    public void handleBackButton(Player player){
        new MinerMainGui(player).open();
    }

}
