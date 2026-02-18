package org.evasive.me.minefinity.forge.recipes;

import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.FuelItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.forge.data.ForgeCategories;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ForgeRecipes {
    CHARCOAL(new LinkedHashMap<>(Map.of(
            ResourceItem.OAK_BARK, 1,
            ResourceItem.OAK_PLANK, 5
    )), 1, 5, FuelItem.CHARCOAL, ForgeCategories.MATERIALS),
    BRICK(new LinkedHashMap<>(Map.of(
            ResourceItem.CLAY_BALL, 4
    )), 1, 5, ResourceItem.BRICK, ForgeCategories.MATERIALS),
    STONE(new LinkedHashMap<>(Map.of(
            ResourceItem.COBBLESTONE, 4
    )), 1, 5, ResourceItem.STONE, ForgeCategories.MATERIALS),
    COPPER_INGOT(new LinkedHashMap<>(Map.of(
            ResourceItem.RAW_COPPER, 3
    )), 1, 5, ResourceItem.COPPER_INGOT, ForgeCategories.MATERIALS),
    TIN_INGOT(new LinkedHashMap<>(Map.of(
            ResourceItem.RAW_TIN, 3
    )), 1, 5, ResourceItem.TIN_INGOT, ForgeCategories.MATERIALS),
    BRONZE_INGOT(new LinkedHashMap<>(Map.of(
            ResourceItem.TIN_INGOT, 1,
            ResourceItem.COPPER_INGOT, 3
    )), 1, 5, ResourceItem.BRONZE_INGOT, ForgeCategories.MATERIALS),
    TUFF_BRICK(new LinkedHashMap<>(Map.of(
            ResourceItem.TUFF, 4
    )), 1, 5, ResourceItem.TUFF_BRICK, ForgeCategories.MATERIALS),
    COPPER_TEMPLATE(new LinkedHashMap<>(Map.of(
            ResourceItem.COPPER_INGOT, 10
    )), 1, 300, PickaxeItem.COPPER_TEMPLATE, ForgeCategories.PICKAXES),
    ;


    private final BaseForgeRecipe crafting;
    ForgeRecipes(Map<CustomItem, Integer> recipeList, int amount, int craftTime, CustomItem result, ForgeCategories forgeCategory) {
        // Use the enum constant name as the ID automatically
        this.crafting = new BaseForgeRecipe(recipeList, result, amount, craftTime, forgeCategory);
    }

    public BaseForgeRecipe getCrafting() {
        return crafting;
    }
}
