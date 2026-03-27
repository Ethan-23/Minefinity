package org.evasive.me.minefinity.customItems.recipebuilder.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;

import java.util.HashMap;
import java.util.Map;

public class RecipeService {

    private final BackpackService backpackService;
    private final CustomItemRegistryService customItemRegistryService;

    public RecipeService(BackpackService backpackService, CustomItemRegistryService customItemRegistryService) {
        this.backpackService = backpackService;
        this.customItemRegistryService = customItemRegistryService;
    }

    public boolean tryPurchaseItem(Player player, BaseItemRecipe recipe){

        Map<String, Integer> remaining = new HashMap<>(recipe.getRecipe());
        Map<Integer, Integer> slotUsage = new HashMap<>();
        Map<String, Integer> backpackUsage = new HashMap<>();

        Inventory inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getSize(); slot++) {

            ItemStack stack = inventory.getItem(slot);

            if(!isValidResource(stack)) continue;

            String itemId = customItemRegistryService.getItemId(stack);

            Integer needed = remaining.get(itemId);

            if (needed == null || needed <= 0) continue;

            int available = stack.getAmount();
            int used = Math.min(available, needed);

            slotUsage.put(slot, used);
            remaining.put(itemId, needed - used);

            if(allRequirementsMet(remaining)){
                removeRequiredItems(player, slotUsage, backpackUsage);
                if(recipe.getResult() != null && !(recipe instanceof BaseForgeRecipe))
                    player.getInventory().addItem(customItemRegistryService.getRegisteredItemStack(recipe.getResult()));
                return true;
            }
        }

        //Check backpack
        for(Map.Entry<String, Integer> entry : remaining.entrySet()){

            if(entry.getValue() <= 0) continue;

            if(!backpackService.getBackpackStorage(player).containsKey(entry.getKey())) return false;

            if(entry.getValue() > backpackService.getBackpackStorage(player).get(entry.getKey())) return false;

            backpackUsage.put(entry.getKey(), entry.getValue());
            remaining.put(entry.getKey(), 0);

            if(allRequirementsMet(remaining)){
                removeRequiredItems(player, slotUsage, backpackUsage);
                if(recipe.getResult() != null)
                    player.getInventory().addItem(customItemRegistryService.getRegisteredItemStack(recipe.getResult()));
                return true;
            }
        }

        return false;
    }

    public void removeRequiredItems(Player player, Map<Integer, Integer> itemTracker, Map<String, Integer> backpackUseage) {
        for (Map.Entry<Integer, Integer> entry : itemTracker.entrySet()) {
            ItemStack itemStack = player.getInventory().getItem(entry.getKey());
            int total = itemStack.getAmount();
            itemStack.setAmount(total - entry.getValue());
        }
        if(backpackUseage.isEmpty()) return;
        for(Map.Entry<String, Integer> entry : backpackUseage.entrySet()){
            backpackService.removeBackpackItem(player, entry.getKey(), entry.getValue());
        }
    }

    private boolean allRequirementsMet(Map<String, Integer> map) {
        for (int value : map.values()) {
            if (value > 0) return false;
        }
        return true;
    }

    private boolean isValidResource(ItemStack stack) {
        return stack != null
                && customItemRegistryService.getItemId(stack) != null;
    }

}
