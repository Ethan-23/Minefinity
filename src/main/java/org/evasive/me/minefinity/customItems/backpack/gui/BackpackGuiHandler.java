package org.evasive.me.minefinity.customItems.backpack.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.backpack.Backpacks;
import org.evasive.me.minefinity.customItems.backpack.BackpackCollect;

import static org.evasive.me.minefinity.utils.PlayerInventoryUtil.getOpenSlots;

public class BackpackGuiHandler {

    public void handleTakeAll(Player player, String itemId) {
        int openSlots = getOpenSlots(player);
        if(openSlots == 0) return;

        while (openSlots > 0 && Minefinity.playerManager.getBackpackStoredItemAmount(player, itemId) > 0) {
            takeBlocks(player, itemId);
            openSlots = getOpenSlots(player);
        }
    }

    public void handleInsertAll(Player player, String backpackId){
        new BackpackCollect().insertInventoryToBackpack(player, backpackId);
    }

    public void handleTakeStack(Player player, String itemId) {
        int slot = player.getInventory().firstEmpty();
        if(slot == -1) return;
        takeBlocks(player, itemId);
    }

    private void takeBlocks(Player player, String itemId) {
        ItemStack item = CustomItemRegistry.getByID(itemId).getBuilder().buildItem();
        int withdrawAmount = Math.min(item.getMaxStackSize(), Minefinity.playerManager.getBackpackStoredItemAmount(player, itemId));
        item.setAmount(withdrawAmount);
        Minefinity.playerManager.removeBackpackItem(player, itemId, withdrawAmount);
        player.getInventory().addItem(item);

    }

    public int calculateBackpackSize(String backpackId){
        int inventorySize = Backpacks.valueOf(backpackId).getBuilder().getStoredItemIdList().size();
        int INVENTORY_ROW_SIZE = 9;
        if(inventorySize % 9 == 0){
            inventorySize = inventorySize / INVENTORY_ROW_SIZE * INVENTORY_ROW_SIZE + INVENTORY_ROW_SIZE;
        }else{
            inventorySize = inventorySize / INVENTORY_ROW_SIZE * INVENTORY_ROW_SIZE + (INVENTORY_ROW_SIZE * 2);
        }
        return inventorySize;
    }



}
