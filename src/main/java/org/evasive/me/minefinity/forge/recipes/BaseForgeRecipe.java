package org.evasive.me.minefinity.forge.recipes;

import org.evasive.me.minefinity.core.items.BaseItemRecipe;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.forge.data.ForgeCategories;

import java.util.Map;

public class BaseForgeRecipe extends BaseItemRecipe {

    int amount;
    int craftTime;
    ForgeCategories forgeCategories;

    public BaseForgeRecipe(Map<CustomItem, Integer> recipe, CustomItem result, int amount, int craftTime, ForgeCategories forgeCategories) {
        super(recipe, result);
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
