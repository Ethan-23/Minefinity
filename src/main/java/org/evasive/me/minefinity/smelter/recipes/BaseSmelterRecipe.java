package org.evasive.me.minefinity.smelter.recipes;

import org.evasive.me.minefinity.core.recipe.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;

import java.util.Map;

public class BaseSmelterRecipe extends BaseItemRecipe {
    public BaseSmelterRecipe(Map<CustomItem, Integer> recipe) {
        super(recipe);
    }

    //private final FuelItem requiredFuelTier;
    //private final int fuelCost;

    /*public BaseSmelterRecipe(Map<CustomItem, Integer> smelterRecipe, CustomItem result, FuelItem requiredFuelTier, int fuelCost) {
        super(smelterRecipe, result);
        this.requiredFuelTier = requiredFuelTier;
        this.fuelCost = fuelCost;
    }

    public FuelItem getRequiredFuelTier() {
        return requiredFuelTier;
    }

    public int getFuelCost() {
        return fuelCost;
    }*/
}
