package org.evasive.me.minefinity.forge.recipes;

import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.forge.data.ForgeCategories;

import java.util.Map;

public enum ForgeRecipes {
    WOODEN_TEMPLATE(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_TEMPLATES),
    STONE_TEMPLATE(Map.of(
    ), 1, 90, null, ForgeCategories.PICKAXE_TEMPLATES),
    COPPER_TEMPLATE(Map.of(
    ), 1, 300, null, ForgeCategories.PICKAXE_TEMPLATES),
    FLINT_HEAD(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_HEADS),
    BRICK_CORE(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_CORES),
    CLAY_HANDLE(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_HANDLES),
    ANDESITE_CORE(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_CORES),
    ROCK_HANDLE(Map.of(
    ), 1, 30, null, ForgeCategories.PICKAXE_HANDLES)
    ;


    private final BaseForgeRecipe baseForgeRecipe;
    ForgeRecipes(Map<CustomItem, Integer> recipeList, int amount, int craftTime, CustomItem result, ForgeCategories forgeCategory) {
        this.baseForgeRecipe = new BaseForgeRecipe(recipeList, result, amount, craftTime, forgeCategory);
    }

    public BaseForgeRecipe getBaseForgeRecipe() {
        return baseForgeRecipe;
    }

    public static boolean contains(String value) {
        for (ForgeRecipes forgeRecipes : ForgeRecipes.values()) {
            if (forgeRecipes.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
