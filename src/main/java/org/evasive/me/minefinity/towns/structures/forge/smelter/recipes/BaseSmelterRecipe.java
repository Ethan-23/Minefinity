package org.evasive.me.minefinity.towns.structures.forge.smelter.recipes;

import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;

import java.util.Map;

public class BaseSmelterRecipe extends BaseItemRecipe {

    private final int requiredFuelTier;
    private final int fuelCost;
    private final int unlockLevel;

    public BaseSmelterRecipe(Map<String, Integer> smelterRecipe, String resultId, int requiredFuelTier, int fuelCost, int unlockLevel) {
        super(smelterRecipe, resultId);
        this.requiredFuelTier = requiredFuelTier;
        this.fuelCost = fuelCost;
        this.unlockLevel = unlockLevel;
    }

    public int getRequiredFuelTier() {
        return requiredFuelTier;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getUnlockLevel() {
        return unlockLevel;
    }
}
