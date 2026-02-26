package org.evasive.me.minefinity.forge.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.recipe.RecipeService;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.data.BaseForgeItem;
import org.evasive.me.minefinity.forge.gui.ForgeCategoriesGui;
import org.evasive.me.minefinity.forge.gui.ForgeGui;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipes;
import org.evasive.me.minefinity.utils.TextConversions;

import java.time.Instant;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;

public class ForgeHandler {

    ForgeService forgeService = Minefinity.getCore().getForgeService();
    RecipeService recipeService = new RecipeService();

    public void startForgeAttempt(Player player, ItemStack result){
        ForgeRecipes forgeRecipe = ForgeRecipes.valueOf(getItemId(result));
        boolean canCraft = recipeService.tryPurchaseItem(player, forgeRecipe.getBaseForgeRecipe());
        if(!canCraft) {
            player.sendMessage(TextConversions.parse("<red>You do not have the correct amount of resources."));
            return;
        }
        forgeService.addForgeItem(player, forgeService.getSelectedForge(player), new BaseForgeItem((Instant.now().toEpochMilli() + (forgeRecipe.getBaseForgeRecipe().getCraftTime()) * 1000L), forgeRecipe.getBaseForgeRecipe().getResult().getID()));
        new ForgeGui(player).open();
    }


    public void handleMainForge(Player player, int slot){

        if(!ForgeGui.FORGE_SLOTS.contains(slot)) return;

        int selectedSlot = ForgeGui.FORGE_SLOTS.indexOf(slot) + 1;

        if(!forgeService.hasForgeItem(player, selectedSlot)) {
            forgeService.setSelectedForge(player, selectedSlot);
            new ForgeCategoriesGui(player, ForgeCategories.PICKAXE_TEMPLATES).open();
        }
        if(forgeService.hasForgeItem(player, selectedSlot) && forgeService.getForgeFinishTime(player, selectedSlot) < Instant.now().toEpochMilli()){
            player.getInventory().addItem(forgeService.getForgeItemStack(player, selectedSlot));
            forgeService.removeForgeItem(player, selectedSlot);
            new ForgeGui(player).open();
        }
    }



}
