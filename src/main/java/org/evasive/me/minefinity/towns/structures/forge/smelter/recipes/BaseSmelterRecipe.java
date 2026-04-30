package org.evasive.me.minefinity.towns.structures.forge.smelter.recipes;

import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;

import java.util.List;
import java.util.Map;

public class BaseSmelterRecipe extends BaseItemRecipe {

    private final int requiredFuelTier;
    private final int fuelCost;

    public BaseSmelterRecipe(Map<String, Integer> smelterRecipe, String resultId, int requiredFuelTier, int fuelCost, float cost, List<RecipeRequirement> requirements) {
        super(smelterRecipe, resultId, cost, requirements);
        this.requiredFuelTier = requiredFuelTier;
        this.fuelCost = fuelCost;
    }

    public int getRequiredFuelTier() {
        return requiredFuelTier;
    }

    public int getFuelCost() {
        return fuelCost;
    }

}
