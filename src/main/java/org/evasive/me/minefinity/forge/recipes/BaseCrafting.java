package org.evasive.me.minefinity.forge.recipes;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.forge.data.ForgeCategories;

import java.util.Collections;
import java.util.Map;

public class BaseCrafting {

    private Map<String, Integer> recipe;
    private int amount;
    private int craftTime;
    private ItemStack result;
    private ForgeCategories forgeCategories;

    public BaseCrafting(Map<String, Integer> recipe, int amount, int craftTime, ItemStack result, ForgeCategories forgeCategories) {
        this.recipe = recipe;
        this.amount = amount;
        this.craftTime = craftTime;
        this.result = result;
        this.forgeCategories = forgeCategories;
    }

    public Map<String, Integer> getRecipe() {
        return Collections.unmodifiableMap(recipe);
    }

    public int getAmount() {
        return amount;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public ForgeCategories getForgeCategories() {
        return forgeCategories;
    }

    public ItemStack getResult() {
        return result;
    }
}
