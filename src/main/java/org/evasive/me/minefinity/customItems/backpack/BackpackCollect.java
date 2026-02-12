package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;

import java.util.Set;

import static org.evasive.me.minefinity.customItems.ItemFunctions.*;

public class BackpackCollect {

    public String findBackpackItem(Player player, String itemId){
        Inventory inventory = player.getInventory();

        int storageAmount = 0;

        for(ItemStack item : inventory.getContents()){

            if(!hasItemId(item) || !Backpacks.contains(getItemId(item))) continue;

            BaseBackpackItem baseBackpackItem = getBackpackItem(item);

            if(!baseBackpackItem.getStoredItemIdList().contains(itemId)) continue;

            storageAmount += baseBackpackItem.getStoredItemAmount();

            if(storageAmount <= Minefinity.playerManager.getBackpackStoredItemAmount(player, itemId)) continue;

            return baseBackpackItem.getID();
        }

        return null;
    }

    public int getTotalBackpackStorage(Player player, String backPackId){
        Inventory inventory = player.getInventory();

        int storageAmount = 0;

        for(ItemStack item : inventory.getContents()){

            if(!hasItemId(item) || !getItemId(item).equals(backPackId)) continue;

            BaseBackpackItem baseBackpackItem = getBackpackItem(item);

            storageAmount += baseBackpackItem.getStoredItemAmount();
        }

        return storageAmount;
    }

    public void insertInventoryToBackpack(Player player, String backPackId){

        int maxItemStorage = getTotalBackpackStorage(player, backPackId);

        Inventory inventory = player.getInventory();

        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) CustomItemRegistry.getByID(backPackId).getBuilder();
        Set<String> storedItemIds = baseBackpackItem.getStoredItemIdList();

        for(ItemStack item : inventory.getContents()){
            if(!hasItemId(item))continue;

            String itemId = getItemId(item);

            if(!storedItemIds.contains(itemId)) continue;

            int amount = item.getAmount();
            int currentStorage = Minefinity.playerManager.getBackpackStoredItemAmount(player, itemId);
            int remainingStorage = maxItemStorage - currentStorage;

            if(remainingStorage == 0)continue;

            if(amount <= remainingStorage){
                Minefinity.playerManager.addBackpackItem(player, itemId, amount);
                item.setAmount(0);
            }else {
                Minefinity.playerManager.addBackpackItem(player, itemId, remainingStorage);
                item.setAmount(amount - remainingStorage);
            }
        }
    }

    public boolean canHoldItem(Player player, String backpackId, String itemId){
        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) CustomItemRegistry.getByID(backpackId).getBuilder();

        int max = baseBackpackItem.getStoredItemAmount();
        int total = Minefinity.playerManager.getBackpackStoredItemAmount(player, itemId);
        return total <= max;
    }

}
