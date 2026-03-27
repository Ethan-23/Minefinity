package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.List;

public class BackpackHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final BackpackService backpackService;

    public BackpackHandler(CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
    }

    public String findBackpackItem(Player player, String itemId){
        Inventory inventory = player.getInventory();

        int storageAmount = 0;

        for(ItemStack item : inventory.getContents()){

            if(item == null || customItemRegistryService.getItemId(item) == null)
                continue;
            BaseCustomItem baseCustomItem;
            try {
                 baseCustomItem = customItemRegistryService.getRegisteredBaseItem(item);
            }catch (Exception e){
                throw new IllegalArgumentException("<red>Unregistered Item ID");
            }


            if(!(baseCustomItem instanceof BaseBackpackItem baseBackpackItem))
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

            BaseCustomItem baseCustomItem;

            try {
                baseCustomItem = customItemRegistryService.getRegisteredBaseItem(item);
            }catch (Exception e){
                continue;
            }

            if(!(baseCustomItem instanceof BaseBackpackItem baseBackpackItem))
                continue;

            if(!baseCustomItem.getID().equals(backPackId))
                continue;

            storageAmount += baseBackpackItem.getStoredItemAmount();
        }

        return storageAmount;
    }

    public void insertInventoryToBackpack(Player player, String backPackId){

        int maxItemStorage = getTotalBackpackStorage(player, backPackId);

        Inventory inventory = player.getInventory();

        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) customItemRegistryService.getBaseItemById(backPackId).getBaseItem();
        List<String> storedItemIds = baseBackpackItem.getStoredItemIdList();

        for(ItemStack item : inventory.getContents()){
            if(!customItemRegistryService.isRegistered(item))continue;

            String itemId = customItemRegistryService.getItemId(item);

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
        BaseBackpackItem baseBackpackItem = (BaseBackpackItem) customItemRegistryService.getBaseItemById(backpackId);

        int max = baseBackpackItem.getStoredItemAmount();
        int total = backpackService.getBackpackStoredItemAmount(player, itemId);
        return total <= max;
    }

}
