package org.evasive.me.minevolutionCore.forge.recipes.storage;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.customItems.items.storage.Backpack;

import java.util.List;

public class BackpackRecipe extends Backpack implements ForgeItemBuilder {
    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of();
    }

    @Override
    public int getCraftTime() {
        return 30;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.STORAGE;
    }
}
