package org.evasive.me.minefinity.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.items.BaseItemRecipe;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;

import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.ItemFunctions.hasItemId;

public class InventoryItemPurchase {

    public boolean tryPurchaseItem(Player player, BaseItemRecipe recipe){
        Map<CustomItem, Integer> recipeMapCopy = new HashMap<>(recipe.getRecipe());
        Inventory inventory = player.getInventory();
        Map<Integer, Integer> itemTracker = new HashMap<>();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);

            if (itemStack == null || !itemStack.hasItemMeta() || !(hasItemId(itemStack)) || !ResourceItem.contains(getItemId(itemStack))) continue;

            ResourceItem resourceItem = ResourceItem.valueOf(getItemId(itemStack));

            if (!recipeMapCopy.containsKey(resourceItem) || recipeMapCopy.get(resourceItem) == 0) continue;

            int requiredAmount = recipeMapCopy.get(resourceItem);
            int currentAmount = itemStack.getAmount();

            if (currentAmount >= requiredAmount) {
                itemTracker.put(slot, requiredAmount);
                recipeMapCopy.put(resourceItem, 0);
            } else {
                itemTracker.put(slot, currentAmount);
                recipeMapCopy.put(resourceItem, requiredAmount - currentAmount);
            }
            if (recipeMapCopy.values().stream().allMatch(count -> count == 0)) {
                removeRequiredItems(player, itemTracker);
                if(recipe.getResult() != null)
                    player.getInventory().addItem(recipe.getResult().getBuilder().buildItem());
                return true;
            }
        }
        return false;
    }

    public void removeRequiredItems(Player player, Map<Integer, Integer> itemTracker) {
        for (Map.Entry<Integer, Integer> entry : itemTracker.entrySet()) {
            ItemStack itemStack = player.getInventory().getItem(entry.getKey());
            int total = itemStack.getAmount();
            itemStack.setAmount(total - entry.getValue());
        }
    }

}
