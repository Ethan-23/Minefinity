package org.evasive.me.minefinity.customItems.recipes;

import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.BaseSmelterRecipe;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.SmelterRecipeManager;

import java.util.*;

public class RecipeUnlockManager {

    Map<UUID, Set<String>> unlockedRecipes = new HashMap<>();
    Set<String> defaultRecipeSet = new HashSet<>();
    Set<BaseItemRecipe> itemRecipes = new HashSet<>();

    public RecipeUnlockManager(SmelterRecipeManager smelterRecipeManager, ForgeRecipeManager forgeRecipeManager) {
        //Create set of all no requirement recipes for players to get on join
        for (Map.Entry<String, BaseSmelterRecipe> entry : smelterRecipeManager.getRecipes().entrySet()) {
            if(entry.getValue().getRequirements().isEmpty())
                defaultRecipeSet.add(entry.getKey());
            itemRecipes.add(entry.getValue());
        }

        for (Map.Entry<String, BaseForgeRecipe> entry : forgeRecipeManager.getRecipes().entrySet()) {
            if(entry.getValue().getRequirements().isEmpty())
                defaultRecipeSet.add(entry.getKey());
            itemRecipes.add(entry.getValue());
        }


    }

    public void registerPlayerUnlocks(PlayerData playerData) {
        Set<String> unlockedRecipeSet = defaultRecipeSet;
        //Go through all recipes that have req and add if reached
        for (BaseItemRecipe itemRecipe : itemRecipes) {
            if(!unlockedRecipeSet.contains(itemRecipe.getResult()) && itemRecipe.completedRequirements(playerData))
                unlockedRecipeSet.add(itemRecipe.getResult());
        }
        unlockedRecipes.put(playerData.getUuid(), unlockedRecipeSet);

    }

    public boolean hasRecipeUnlocked(UUID uuid, String recipeId) {
        return unlockedRecipes.get(uuid).contains(recipeId);
    }

}
