package org.evasive.me.minevolutionCore.forge.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeRecipes;
import org.evasive.me.minevolutionCore.forge.ForgeItem;
import org.evasive.me.minevolutionCore.forge.ForgeManager;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForgeGUIEvents implements Listener {

    ForgeGUICategories forgeGUICategories = new ForgeGUICategories();
    ForgeGUIMain forgeGUIMain = new ForgeGUIMain();
    ForgeGUIConfirmation forgeGUIConfirmation = new ForgeGUIConfirmation();
    ForgeManager forgeManager = new ForgeManager();

    @EventHandler
    public void forgeGUIMain(InventoryClickEvent e){
        if(!e.getView().title().equals(Component.text("Forge")))
            return;

        if(e.getClickedInventory() == null)
            return;

        if(e.getClickedInventory().equals(e.getView().getBottomInventory())){
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getWhoClicked();

        //20 - 24 forge slots
        if(e.getSlot() > 19 && e.getSlot() < 25){
            int selectedSlot = e.getSlot() - 19;
            if(!forgeManager.hasForgeItem(player, selectedSlot)) {
                forgeManager.setSelectedForge(player, selectedSlot);
                forgeGUICategories.openInventory(player, 1);
            }
            if(forgeManager.hasForgeItem(player, selectedSlot) && forgeManager.getForgeFinishTime(player, selectedSlot) < Instant.now().toEpochMilli()){
                player.getInventory().addItem(forgeManager.getForgeItemStack(player, selectedSlot));
                forgeManager.removeForgeItem(player, selectedSlot);
                forgeGUIMain.openInventory(player);
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void forgeGUICategories(InventoryClickEvent e){
        if(!e.getView().title().equals(Component.text("Forge Categories")))
            return;

        if(e.getClickedInventory() == null)
            return;

        if(e.getClickedInventory().equals(e.getView().getBottomInventory())){
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        //37 to 43
        //Change to 44 when I add the last 2
        int selectedSlot = e.getSlot();

        //Checks if you are clicking on a recipe
        if(selectedSlot > 9 && selectedSlot < 35 && selectedSlot % 9 != 0 && selectedSlot % 9 != 8){
            if(!Objects.requireNonNull(e.getCurrentItem()).hasItemMeta())
                return;
            if(!e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(PickaxeKeys.itemID))
                return;
            forgeGUIConfirmation.openInventory(player, ForgeRecipes.valueOf(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(PickaxeKeys.itemID, PersistentDataType.STRING)).getForgeItemBuilder());
        }

        if(e.getSlot() > 36 && e.getSlot() < 41){
            forgeGUICategories.openInventory((Player) e.getWhoClicked(), e.getSlot() - 36);
        }

        if(e.getSlot() == 45){
            forgeGUIMain.openInventory(player);
        }

        if(e.getSlot() == 49){
            player.closeInventory();
        }


    }

    @EventHandler
    public void forgeGUIConfirmation(InventoryClickEvent e){
        if(!e.getView().title().equals(Component.text("Forge Confirmation")))
            return;

        if(e.getClickedInventory() == null)
            return;

        if(e.getClickedInventory().equals(e.getView().getBottomInventory())){
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        if(e.getSlot() == 22){
            ItemStack item = e.getClickedInventory().getItem(16);

            ForgeItemBuilder forgeItemBuilder = ForgeRecipes.valueOf(item.getItemMeta().getPersistentDataContainer().get(PickaxeKeys.itemID, PersistentDataType.STRING)).getForgeItemBuilder();

            List<ItemStack> recipe = forgeItemBuilder.getRecipe();

            Map<ItemStack, Integer> requiredCounts = new HashMap<>();

            // Populate the map with recipe items
            for (ItemStack recipeItem : recipe) {
                requiredCounts.merge(recipeItem, recipeItem.getAmount(), Integer::sum);
            }

            for(ItemStack itemStack : player.getInventory()){
                if(itemStack == null)
                    continue;
                for (ItemStack recipeItem : requiredCounts.keySet()) {
                    // Check if the inventory item matches the recipe item
                    if (itemStack.isSimilar(recipeItem)) {
                        int requiredAmount = requiredCounts.get(recipeItem);
                        int currentAmount = itemStack.getAmount();

                        // Reduce the required count based on available items
                        if (currentAmount >= requiredAmount) {
                            requiredCounts.put(recipeItem, 0); // Requirement fulfilled
                        } else {
                            requiredCounts.put(recipeItem, requiredAmount - currentAmount);
                        }
                    }
                }
            }
            if(requiredCounts.values().stream().allMatch(count -> count == 0)){
                removeRequiredItems(player, recipe);
                if(forgeManager.hasForgeItem(player, forgeManager.getSelectedForge(player)))
                    return;
                forgeManager.addForgeItem(player, forgeManager.getSelectedForge(player), new ForgeItem(Instant.now().toEpochMilli() + (forgeItemBuilder.getCraftTime() * 1000L), forgeItemBuilder.getItem()));
                forgeGUIMain.openInventory(player);
            }
        }

        if(e.getSlot() == 45){
            forgeGUICategories.openInventory(player, 1);
        }

        if(e.getSlot() == 49){
            player.closeInventory();
        }


    }

    public void removeRequiredItems(Player player, List<ItemStack> recipe) {
        Inventory inventory = player.getInventory();

        for (ItemStack recipeItem : recipe) {
            int remainingAmount = recipeItem.getAmount(); // Total required amount for this item

            for (ItemStack inventoryItem : inventory.getContents()) {
                if (inventoryItem == null || !inventoryItem.isSimilar(recipeItem)) {
                    continue; // Skip if the item doesn't match
                }

                int inventoryAmount = inventoryItem.getAmount();

                if (inventoryAmount >= remainingAmount) {
                    // Fulfill the requirement and update inventory
                    inventoryItem.setAmount(inventoryAmount - remainingAmount);
                    if (inventoryItem.getAmount() == 0) {
                        inventory.remove(inventoryItem); // Remove completely if amount is zero
                    }
                    remainingAmount = 0; // Requirement fulfilled
                    break;
                } else {
                    // Partially fulfill the requirement
                    remainingAmount -= inventoryAmount;
                    inventory.remove(inventoryItem); // Remove the depleted stack
                }
            }
        }
    }

}
