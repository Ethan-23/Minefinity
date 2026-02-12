package org.evasive.me.minefinity.forge.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.recipes.BaseCrafting;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipes;
import org.evasive.me.minefinity.forge.data.ForgeItem;
import org.evasive.me.minefinity.forge.data.ForgeManager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.ItemFunctions.hasItemId;

public class ForgeHandler {

    ForgeManager forgeManager = new ForgeManager();

    public void startForgeAttempt(Player player, ItemStack result){
        BaseCrafting baseCrafting = ForgeRecipes.valueOf(getItemId(result)).getCrafting();

        Map<String, Integer> requiredCounts = new HashMap<>(Map.copyOf(baseCrafting.getRecipe()));
        Map<Integer, Integer> itemTracker = new HashMap<>();

        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            ItemStack itemStack = player.getInventory().getItem(slot);

            if (itemStack == null || !itemStack.hasItemMeta() || !(hasItemId(itemStack)))
                continue;

            String itemId = getItemId(itemStack);

            if (!requiredCounts.containsKey(itemId) || requiredCounts.get(itemId) == 0) continue;

            int requiredAmount = requiredCounts.get(itemId);
            int currentAmount = itemStack.getAmount();

            if (currentAmount >= requiredAmount) {
                itemTracker.put(slot, requiredAmount);
                requiredCounts.put(itemId, 0);
            } else {
                itemTracker.put(slot, currentAmount);
                requiredCounts.put(itemId, requiredAmount - currentAmount);
            }
            if (requiredCounts.values().stream().allMatch(count -> count == 0)) {
                removeRequiredItems(player, itemTracker);

                forgeManager.addForgeItem(player, forgeManager.getSelectedForge(player), new ForgeItem(Instant.now().toEpochMilli() + (baseCrafting.getCraftTime() * 1000L), baseCrafting.getResult()));
                new ForgeGui(player).open();
                return;
            }
        }
    }

    public void removeRequiredItems(Player player, Map<Integer, Integer> itemTracker) {
        for (Map.Entry<Integer, Integer> entry : itemTracker.entrySet()) {
            ItemStack itemStack = player.getInventory().getItem(entry.getKey());
            int total = itemStack.getAmount();
            itemStack.setAmount(total - entry.getValue());
        }
    }

    public void handleMainForge(Player player, int slot){

        if(!ForgeGui.FORGE_SLOTS.contains(slot)) return;

        int selectedSlot = ForgeGui.FORGE_SLOTS.indexOf(slot) + 1;

        if(!forgeManager.hasForgeItem(player, selectedSlot)) {
            forgeManager.setSelectedForge(player, selectedSlot);
            new ForgeCategoriesGui(player, ForgeCategories.MATERIALS).open();
        }
        if(forgeManager.hasForgeItem(player, selectedSlot) && forgeManager.getForgeFinishTime(player, selectedSlot) < Instant.now().toEpochMilli()){
            player.getInventory().addItem(forgeManager.getForgeItemStack(player, selectedSlot));
            forgeManager.removeForgeItem(player, selectedSlot);
            new ForgeGui(player).open();
        }
    }



}
