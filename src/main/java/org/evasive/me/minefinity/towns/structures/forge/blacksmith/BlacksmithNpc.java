package org.evasive.me.minefinity.towns.structures.forge.blacksmith;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.gui.ForgeGui;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.service.ForgeService;

public class BlacksmithNpc implements NpcBehavior {

    private final CustomItemRegistryService customItemRegistryService;
    private final ForgeRecipeManager forgeRecipeManager;
    private final ForgeService forgeService;
    private final RecipeService recipeService;

    public BlacksmithNpc(CustomItemRegistryService customItemRegistryService, ForgeRecipeManager forgeRecipeManager, ForgeService forgeService, RecipeService recipeService) {
        this.customItemRegistryService = customItemRegistryService;
        this.forgeRecipeManager = forgeRecipeManager;
        this.forgeService = forgeService;
        this.recipeService = recipeService;
    }

    @Override
    public void onInteract(Player player) {
        new ForgeGui(player, customItemRegistryService, forgeRecipeManager, forgeService, recipeService).open();
    }

    @Override
    public void onTick() {

    }
}
