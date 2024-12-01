package org.evasive.me.minevolutionCore.forge.recipes.pickaxe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemList;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeCategories;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.customItems.items.pickaxes.WoodenPickaxe;

import java.util.List;

public class WoodenPickaxeRecipe extends WoodenPickaxe implements ForgeItemBuilder {
    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of(new ItemStack(Material.OAK_PLANKS, 10), ItemList.WOODEN_CORE.getBuilder().getItem());
    }

    @Override
    public int getCraftTime() {
        return 10;
    }

    @Override
    public ForgeCategories getForgeCategory() {
        return ForgeCategories.PICKAXE;
    }
}
