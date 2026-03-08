package org.evasive.me.minefinity.miner.gui.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.BasePickaxeItem;
import org.evasive.me.minefinity.miner.gui.selection.MinerBlockSelectionGui;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.player.sevices.BlockTierService;

import java.util.Map;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;


public class MinerMainHandler {

    private final AutoMinerService autoMinerService;
    private final BlockTierService blockTierService;

    public MinerMainHandler(AutoMinerService autoMinerService,BlockTierService blockTierService) {
        this.autoMinerService = autoMinerService;
        this.blockTierService = blockTierService;
    }

    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Map<String, Integer> itemMap = autoMinerService.getAutoMinerStorage(player);

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            int remaining = entry.getValue();

            int overflow = Minefinity.itemGiver.givePlayerDrops(player, entry.getKey(), remaining);

            autoMinerService.removeAutoMinerStorage(player, entry.getKey(), remaining - overflow);

        }
    }

    public boolean handlePickaxeSlot(Player player, ItemStack cursorItem) {

        if(!cursorItem.isEmpty()) {
            return false;
        }

        BaseCustomItem baseCustomItem = getRegisteredBaseItem(cursorItem);

        if(!(baseCustomItem instanceof BasePickaxeItem basePickaxeItem))
            return false;

        if(cursorItem.isEmpty()){
            autoMinerService.setAutoMinerPickaxe(player, null);
            return true;
        }

        autoMinerService.setAutoMinerPickaxe(player, basePickaxeItem);
        return true;

    }

    public void handleBlockSlot(Player player){
        new MinerBlockSelectionGui(player, autoMinerService, blockTierService).open();
    }



}
