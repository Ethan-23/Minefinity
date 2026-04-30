package org.evasive.me.minefinity.towns.structures.forge.blacksmith.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.BaseForgeItem;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui.ForgeCategoriesGui;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui.ForgeGui;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;

import java.time.Instant;

public class ForgeHandler {

    private final ForgeService forgeService;
    private final ForgeRecipeManager forgeRecipeManager;
    private final RecipeService recipeService;
    private final CustomItemRegistryService customItemRegistryService;

    public ForgeHandler(CustomItemRegistryService customItemRegistryService, ForgeRecipeManager forgeRecipeManager, RecipeService recipeService, ForgeService forgeService) {
        this.customItemRegistryService = customItemRegistryService;
        this.forgeRecipeManager = forgeRecipeManager;
        this.forgeService = forgeService;
        this.recipeService = recipeService;
    }

    public void startForgeAttempt(Player player, ItemStack result){
        BaseForgeRecipe baseForgeRecipe = forgeRecipeManager.getRecipe(customItemRegistryService.getItemId(result));
        boolean canCraft = recipeService.tryPurchaseItem(player, baseForgeRecipe);
        if(!canCraft) {
            player.sendMessage(TextConversions.parse("<red>You do not have the correct amount of resources."));
            return;
        }
        forgeService.addForgeItem(player, forgeService.getSelectedForge(player), new BaseForgeItem((Instant.now().toEpochMilli() + (baseForgeRecipe.getCraftTime()) * 1000L), baseForgeRecipe.getResult()));
        new ForgeGui(player, customItemRegistryService, forgeRecipeManager, forgeService, recipeService).open();
    }


    public void handleMainForge(Player player, int slot){

        if(!ForgeGui.FORGE_SLOTS.contains(slot)) return;

        int selectedSlot = ForgeGui.FORGE_SLOTS.indexOf(slot) + 1;

        if(!forgeService.hasForgeItem(player, selectedSlot)) {
            forgeService.setSelectedForge(player, selectedSlot);
            new ForgeCategoriesGui(player, customItemRegistryService, ForgeCategories.PICKAXE_TEMPLATES, forgeRecipeManager, forgeService, recipeService).open();
        }
        if(forgeService.hasForgeItem(player, selectedSlot) && forgeService.getForgeFinishTime(player, selectedSlot) < Instant.now().toEpochMilli()){
            player.getInventory().addItem(forgeService.getForgeItemStack(player, selectedSlot));
            forgeService.removeForgeItem(player, selectedSlot);
            new ForgeGui(player, customItemRegistryService, forgeRecipeManager, forgeService, recipeService).open();
        }
    }



}
