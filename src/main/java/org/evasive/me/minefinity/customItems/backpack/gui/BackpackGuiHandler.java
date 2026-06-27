package org.evasive.me.minefinity.customItems.backpack.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.backpack.BackpackHandler;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseBackpackItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

public class BackpackGuiHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final BackpackService backpackService;

    public BackpackGuiHandler(CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
    }

    public void handleTakeAll(Player player, String itemId) {
        int openSlots = getOpenSlots(player);
        if(openSlots == 0) return;

        while (openSlots > 0 && backpackService.getBackpackStoredItemAmount(player, itemId) > 0) {
            takeBlocks(player, itemId);
            openSlots = getOpenSlots(player);
        }
    }

    public void handleInsertAll(Player player, String backpackId){
        new BackpackHandler(customItemRegistryService, backpackService).insertInventoryToBackpack(player, backpackId);
    }

    public void handleTakeStack(Player player, String itemId) {
        int slot = player.getInventory().firstEmpty();
        if(slot == -1) return;
        takeBlocks(player, itemId);
    }

    private void takeBlocks(Player player, String itemId) {
        ItemStack item = customItemRegistryService.getRegisteredItemStack(itemId);
        int withdrawAmount = Math.min(item.getMaxStackSize(), backpackService.getBackpackStoredItemAmount(player, itemId));
        item.setAmount(withdrawAmount);
        backpackService.removeBackpackItem(player, itemId, withdrawAmount);
        player.getInventory().addItem(item);

    }

    public int calculateBackpackSize(String backpackId){
        int inventorySize = ((BaseBackpackItem) customItemRegistryService.getBaseItemById(backpackId)).storageListComponent().getValue().size();
        int INVENTORY_ROW_SIZE = 9;
        if(inventorySize % 9 == 0){
            inventorySize = inventorySize / INVENTORY_ROW_SIZE * INVENTORY_ROW_SIZE + INVENTORY_ROW_SIZE;
        }else{
            inventorySize = inventorySize / INVENTORY_ROW_SIZE * INVENTORY_ROW_SIZE + (INVENTORY_ROW_SIZE * 2);
        }
        return inventorySize;
    }

    public static int getOpenSlots(Player player){
        Inventory inventory = player.getInventory();
        int empty = 0;
        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null || item.getType() == Material.AIR) {
                empty++;
            }
        }
        return empty;
    }

}
