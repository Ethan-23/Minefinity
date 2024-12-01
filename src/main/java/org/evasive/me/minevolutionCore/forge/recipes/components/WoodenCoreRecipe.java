package org.evasive.me.minevolutionCore.forge.recipes.components;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.items.components.WoodenCore;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;

import java.util.List;

public class WoodenCoreRecipe extends WoodenCore implements ForgeItemBuilder {

    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of(new ItemStack(Material.OAK_PLANKS, 10));
    }

    @Override
    public int getCraftTime() {
        return 10;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.COMPONENT;
    }
}
