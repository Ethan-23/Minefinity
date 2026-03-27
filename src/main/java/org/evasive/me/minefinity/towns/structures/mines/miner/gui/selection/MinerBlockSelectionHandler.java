package org.evasive.me.minefinity.towns.structures.mines.miner.gui.selection;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.mines.miner.gui.main.MinerMainGui;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

public class MinerBlockSelectionHandler {

    private final BlockTierService blockTierService;
    private final AutoMinerService autoMinerService;
    private final ItemPickupService itemPickupService;
    private final CustomItemRegistryService customItemRegistryService;

    public MinerBlockSelectionHandler(BlockTierService blockTierService, ItemPickupService itemPickupService, CustomItemRegistryService customItemRegistryService, AutoMinerService autoMinerService) {
        this.blockTierService = blockTierService;
        this.autoMinerService = autoMinerService;
        this.itemPickupService = itemPickupService;
        this.customItemRegistryService = customItemRegistryService;
    }

    public void handleBlockSelection(Player player, int clickedSlot){
        int clickedBlockTier = clickedSlot - 1;
        int minerUnlockedBlockTier = autoMinerService.getAutoMinerUnlockedBlockTier(player);
        if(clickedBlockTier > minerUnlockedBlockTier) return;

        autoMinerService.setAutoMinerBlockType(player, blockTierService.getBlockTypeRegistryService().getBlockList(player.getWorld().getName()).get(clickedSlot - 1));
    }

    public void handleNoneSelection(Player player) {
        autoMinerService.setAutoMinerBlockType(player, null);
    }

    public void handleBackButton(Player player){
        new MinerMainGui(player, customItemRegistryService, itemPickupService,autoMinerService, blockTierService).open();
    }

}
