package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;

import java.util.List;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.*;

public class BackpackHandler {

    BackpackService backpackService = Minefinity.getCore().getBackpackService();

    public String findBackpackItem(Player player, String itemId){
        Inventory inventory = player.getInventory();

        int storageAmount = 0;

        for(ItemStack item : inventory.getContents()){

            if(!(getRegisteredBaseItem(item) instanceof BaseBackpackItem baseBackpackItem))
                continue;

            if(!baseBackpackItem.getStoredItemIdList().contains(itemId)) continue;

            storageAmount += baseBackpackItem.getStoredItemAmount();

            if(storageAmount <= backpackService.getBackpackStoredItemAmount(player, itemId)) continue;

            return baseBackpackItem.getID();
        }

        return null;
    }

    public int getTotalBackpackStorage(Player player, String backPackId){
        Inventory inventory = player.getInventory();

        int storageAmount = 0;

        for(ItemStack item : inventory.getContents()){

            if(!(getRegisteredBaseItem(item) instanceof BaseBackpackItem baseBackpackItem))
                continue;

            storageAmount += baseBackpackItem.getStoredItemAmount();
        }

        return storageAmount;
    }

    public void insertInventoryToBackpack(Player player, String backPackId){

        int maxItemStorage = getTotalBackpackStorage(player, backPackId);

        Inventory inventory = player.getInventory();

        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) CustomItemRegistry.getByID(backPackId).getBaseItem();
        List<String> storedItemIds = baseBackpackItem.getStoredItemIdList();

        for(ItemStack item : inventory.getContents()){
            if(!hasItemId(item))continue;

            String itemId = getItemId(item);

            if(!storedItemIds.contains(itemId)) continue;

            int amount = item.getAmount();
            int currentStorage = backpackService.getBackpackStoredItemAmount(player, itemId);
            int remainingStorage = maxItemStorage - currentStorage;

            if(remainingStorage == 0)continue;

            if(amount <= remainingStorage){
                backpackService.addBackpackItem(player, itemId, amount);
                item.setAmount(0);
            }else {
                backpackService.addBackpackItem(player, itemId, remainingStorage);
                item.setAmount(amount - remainingStorage);
            }
        }
    }

    public boolean canHoldItem(Player player, String backpackId, String itemId){
        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) CustomItemRegistry.getByID(backpackId).getBaseItem();

        int max = baseBackpackItem.getStoredItemAmount();
        int total = backpackService.getBackpackStoredItemAmount(player, itemId);
        return total <= max;
    }

}
