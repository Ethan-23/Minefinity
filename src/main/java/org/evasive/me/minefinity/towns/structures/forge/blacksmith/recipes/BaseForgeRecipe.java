package org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes;

import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;

import java.util.Map;

public class BaseForgeRecipe extends BaseItemRecipe {

    int amount;
    int craftTime;
    ForgeCategories forgeCategories;

    public BaseForgeRecipe(Map<String, Integer> recipe, String resultId, int amount, int craftTime, ForgeCategories forgeCategories) {
        super(recipe, resultId);
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
