package org.evasive.me.minevolutionCore.forge.recipes.pickaxe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.StonePickaxe;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;

import java.util.List;

public class StonePickaxeRecipe extends StonePickaxe implements ForgeItemBuilder {
    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of(new ItemStack(Material.COBBLESTONE, 100));
    }

    @Override
    public int getCraftTime() {
        return 30;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.PICKAXE;
    }
}
