package org.evasive.me.minevolutionCore.forge.recipes.pickaxe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.CopperPickaxe;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;

import java.util.List;

public class CopperPickaxeRecipe extends CopperPickaxe implements ForgeItemBuilder {
    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of(new ItemStack(Material.COPPER_INGOT, 10));
    }

    @Override
    public int getCraftTime() {
        return 100;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.PICKAXE;
    }
}
