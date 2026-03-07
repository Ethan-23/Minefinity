package org.evasive.me.minefinity.core.recipe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipes;

import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.hasItemId;

public class RecipeService {

    BackpackService backpackService = Minefinity.getCore().getBackpackService();

    public boolean tryPurchaseItem(Player player, BaseItemRecipe recipe){

        Map<CustomItem, Integer> remaining = new HashMap<>(recipe.getRecipe());
        Map<Integer, Integer> slotUsage = new HashMap<>();
        Map<CustomItem, Integer> backpackUsage = new HashMap<>();

        Inventory inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getSize(); slot++) {

            ItemStack stack = inventory.getItem(slot);

            if(!isValidResource(stack)) continue;

            CustomItem item = CustomItemRegistry.getByID(getItemId(stack));

            Integer needed = remaining.get(item);

            if (needed == null || needed <= 0) continue;

            int available = stack.getAmount();
            int used = Math.min(available, needed);

            slotUsage.put(slot, used);
            remaining.put(item, needed - used);

            if(allRequirementsMet(remaining)){
                removeRequiredItems(player, slotUsage, backpackUsage);
                if(recipe.getResult() != null && !(ForgeRecipes.contains(recipe.getResult().getID())))
                    player.getInventory().addItem(recipe.getResult().getBaseItem().buildItem());
                return true;
            }
        }

        //Check backpack
        for(Map.Entry<CustomItem, Integer> entry : remaining.entrySet()){

            if(entry.getValue() <= 0) continue;

            if(!backpackService.getBackpackItems(player).containsKey(entry.getKey().getID())) return false;

            if(entry.getValue() > backpackService.getBackpackItems(player).get(entry.getKey().getID())) return false;

            backpackUsage.put(entry.getKey(), entry.getValue());
            remaining.put(entry.getKey(), 0);

            if(allRequirementsMet(remaining)){
                removeRequiredItems(player, slotUsage, backpackUsage);
                if(recipe.getResult() != null && !(ForgeRecipes.contains(recipe.getResult().getID())))
                    player.getInventory().addItem(recipe.getResult().getBaseItem().buildItem());
                return true;
            }
        }

        return false;
    }

    public void removeRequiredItems(Player player, Map<Integer, Integer> itemTracker, Map<CustomItem, Integer> backpackUseage) {
        for (Map.Entry<Integer, Integer> entry : itemTracker.entrySet()) {
            ItemStack itemStack = player.getInventory().getItem(entry.getKey());
            int total = itemStack.getAmount();
            itemStack.setAmount(total - entry.getValue());
        }
        if(backpackUseage.isEmpty()) return;
        for(Map.Entry<CustomItem, Integer> entry : backpackUseage.entrySet()){
            backpackService.removeBackpackItem(player, entry.getKey().getID(), entry.getValue());
        }
    }

    private boolean allRequirementsMet(Map<CustomItem, Integer> map) {
        for (int value : map.values()) {
            if (value > 0) return false;
        }
        return true;
    }

    private boolean isValidResource(ItemStack stack) {
        return stack != null
                && stack.hasItemMeta()
                && hasItemId(stack)
                && CustomItemRegistry.isRegistered(getItemId(stack));
    }

}
