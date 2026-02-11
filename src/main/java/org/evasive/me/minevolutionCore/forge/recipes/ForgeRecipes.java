package org.evasive.me.minevolutionCore.forge.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minevolutionCore.customItems.items.ResourceItem;
import org.evasive.me.minevolutionCore.forge.data.ForgeCategories;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ForgeRecipes {
    COPPER_TEMPLATE(new LinkedHashMap<>(Map.of(
            ResourceItem.COPPER_INGOT.getID(), 10
    )), 1, 300, PickaxeItem.COPPER_TEMPLATE.getBuilder().buildItem(), ForgeCategories.PICKAXES),
    BACKPACK(new LinkedHashMap<>(Map.of(
            ResourceItem.COPPER_INGOT.getID(), 50,
            ResourceItem.OAK_PLANK.getID(), 50
    )), 1, 30, new ItemStack(Material.PLAYER_HEAD), ForgeCategories.STORAGE),
    COMPRESSED_STONE(new LinkedHashMap<>(Map.of(
            ResourceItem.COBBLESTONE.getID(), 25,
            ResourceItem.STONE.getID(), 25
    )), 1, 30, ResourceItem.COMPRESSED_STONE.getBuilder().buildItem(), ForgeCategories.MATERIALS),
    REINFORCED_STONE(new LinkedHashMap<>(Map.of(
            ResourceItem.COMPRESSED_STONE.getID(), 10,
            ResourceItem.ROCKS.getID(), 10
    )), 1, 30, ResourceItem.REINFORCED_STONE.getBuilder().buildItem(), ForgeCategories.MATERIALS)
    ;


    private final BaseCrafting crafting;
    ForgeRecipes(Map<String, Integer> recipeList, int amount, int craftTime, ItemStack result, ForgeCategories forgeCategory) {
        // Use the enum constant name as the ID automatically
        this.crafting = new BaseCrafting(recipeList, amount, craftTime, result, forgeCategory);
    }

    public BaseCrafting getCrafting() {
        return crafting;
    }
}
