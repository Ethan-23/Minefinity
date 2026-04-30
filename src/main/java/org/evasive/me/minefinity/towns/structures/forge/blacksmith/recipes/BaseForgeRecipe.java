package org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes;

import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;

import java.util.List;
import java.util.Map;

public class BaseForgeRecipe extends BaseItemRecipe {

    private final int amount;
    private final int craftTime;
    private final ForgeCategories forgeCategories;

    public BaseForgeRecipe(Map<String, Integer> recipe, String resultId, int amount, int craftTime, float cost, ForgeCategories forgeCategories, List<RecipeRequirement> requirements) {
        super(recipe, resultId, cost, requirements);
        this.amount = amount;
        this.craftTime = craftTime;
        this.forgeCategories = forgeCategories;
    }

    public int getAmount() {
        return amount;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public ForgeCategories getForgeCategory() {
        return forgeCategories;
    }
}
