package org.evasive.me.minefinity.towns.structures.workshop.engineer;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.gui.EngineerGui;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.service.EngineerService;

public class EngineerNpc implements NpcBehavior {

    private final EngineerService engineerService;
    private final WorkshopRecipeManager workshopRecipeManager;
    private final BackpackService backpackService;
    private final CustomItemRegistryService customItemRegistryService;
    private final RecipeService recipeService;

    public EngineerNpc(EngineerService engineerService, WorkshopRecipeManager workshopRecipeManager, BackpackService backpackService, CustomItemRegistryService customItemRegistryService, RecipeService recipeService) {
        this.engineerService = engineerService;
        this.workshopRecipeManager = workshopRecipeManager;
        this.backpackService = backpackService;
        this.customItemRegistryService = customItemRegistryService;
        this.recipeService = recipeService;
    }

    @Override
    public void onInteract(Player player) {
        new EngineerGui(player, WorkshopMode.CARPENTRY, engineerService, workshopRecipeManager, backpackService, customItemRegistryService, recipeService).open();
    }

    @Override
    public void onTick() {

    }
}
