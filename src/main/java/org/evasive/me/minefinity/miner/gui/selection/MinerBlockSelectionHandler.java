package org.evasive.me.minefinity.miner.gui.selection;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;

public class MinerBlockSelectionHandler {

    private final BlockTierService blockTierService;
    private final AutoMinerService autoMinerService;

    public MinerBlockSelectionHandler(BlockTierService blockTierService, AutoMinerService autoMinerService) {
        this.blockTierService = blockTierService;
        this.autoMinerService = autoMinerService;
    }

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
        new MinerMainGui(player, autoMinerService, blockTierService).open();
    }

}
