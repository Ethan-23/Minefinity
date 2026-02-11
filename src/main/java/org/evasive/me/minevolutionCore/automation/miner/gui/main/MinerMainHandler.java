package org.evasive.me.minevolutionCore.automation.miner.gui.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.automation.miner.data.AutoMiner;
import org.evasive.me.minevolutionCore.automation.miner.gui.selection.MinerBlockSelectionGui;
import org.evasive.me.minevolutionCore.customItems.ItemGiver;

import java.util.Map;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.*;


public class MinerMainHandler {


    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        AutoMiner miner = MinevolutionCore.playerManager.getPlayerData(player).getAutoMiner();

        Map<String, Integer> itemMap = miner.getItemStorage();

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            int remaining = entry.getValue();

            int overflow = MinevolutionCore.itemGiver.givePlayerDrops(player, entry.getKey(), remaining);

            miner.removeItemStorage(entry.getKey(), remaining - overflow);

        }
    }

    public boolean handlePickaxeSlot(Player player, ItemStack cursorItem) {

        if(!isPickaxe(cursorItem) && !cursorItem.isEmpty()) {
            return false;
        }

        if(cursorItem.isEmpty()){
            MinevolutionCore.playerManager.getPlayerData(player).getAutoMiner().setPickaxe(null);
            return true;
        }

        MinevolutionCore.playerManager.getPlayerData(player).getAutoMiner().setPickaxe(cursorItem.clone());
        return true;

    }

    public void handleBlockSlot(Player player){
        new MinerBlockSelectionGui(player).open();
    }



}
