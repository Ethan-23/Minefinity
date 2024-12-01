package org.evasive.me.minevolutionCore.forge.recipes.components;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.items.components.StoneCore;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;

import java.util.List;

public class StoneCoreRecipe extends StoneCore implements ForgeItemBuilder {
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
        return 300;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.COMPONENT;
    }
}
