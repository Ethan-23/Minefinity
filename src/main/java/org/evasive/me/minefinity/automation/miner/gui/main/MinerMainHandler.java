package org.evasive.me.minefinity.automation.miner.gui.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.data.AutoMiner;
import org.evasive.me.minefinity.automation.miner.gui.selection.MinerBlockSelectionGui;

import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.*;


public class MinerMainHandler {


    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        AutoMiner miner = Minefinity.playerManager.getPlayerData(player).getAutoMiner();

        Map<String, Integer> itemMap = miner.getItemStorage();

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            int remaining = entry.getValue();

            int overflow = Minefinity.itemGiver.givePlayerDrops(player, entry.getKey(), remaining);

            miner.removeItemStorage(entry.getKey(), remaining - overflow);

        }
    }

    public boolean handlePickaxeSlot(Player player, ItemStack cursorItem) {

        if(!isPickaxe(cursorItem) && !cursorItem.isEmpty()) {
            return false;
        }

        if(cursorItem.isEmpty()){
            Minefinity.playerManager.getPlayerData(player).getAutoMiner().setPickaxe(null);
            return true;
        }

        Minefinity.playerManager.getPlayerData(player).getAutoMiner().setPickaxe(cursorItem.clone());
        return true;

    }

    public void handleBlockSlot(Player player){
        new MinerBlockSelectionGui(player).open();
    }



}
