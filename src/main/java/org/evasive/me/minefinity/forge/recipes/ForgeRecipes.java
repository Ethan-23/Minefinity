package org.evasive.me.minefinity.forge.recipes;

import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.types.FuelItem;
import org.evasive.me.minefinity.customItems.types.ResourceItem;
import org.evasive.me.minefinity.forge.data.ForgeCategories;

import java.util.Map;

public enum ForgeRecipes {
    WOODEN_TEMPLATE(Map.of(
            ResourceItem.TIMBER, 15
    ), 1, 30, PickaxeItem.WOODEN_TEMPLATE, ForgeCategories.PICKAXE_TEMPLATES),
    STONE_TEMPLATE(Map.of(
            ResourceItem.STONE, 50,
            ResourceItem.TIMBER, 30
    ), 1, 90, PickaxeItem.STONE_TEMPLATE, ForgeCategories.PICKAXE_TEMPLATES),
    COPPER_TEMPLATE(Map.of(
            ResourceItem.COPPER_INGOT, 100,
            ResourceItem.MOSSY_TIMBER, 30
    ), 1, 300, PickaxeItem.COPPER_TEMPLATE, ForgeCategories.PICKAXE_TEMPLATES),
    FLINT_HEAD(Map.of(
            ResourceItem.FLINT, 100
    ), 1, 30, PickaxeComponent.FLINT_HEAD, ForgeCategories.PICKAXE_HEADS),
    BRICK_CORE(Map.of(
            ResourceItem.BRICK, 100
    ), 1, 30, PickaxeComponent.BRICK_CORE, ForgeCategories.PICKAXE_CORES),
    CLAY_HANDLE(Map.of(
            ResourceItem.CLAY_BALL, 100
    ), 1, 30, PickaxeComponent.CLAY_HANDLE, ForgeCategories.PICKAXE_HANDLES),
    ANDESITE_CORE(Map.of(
            ResourceItem.ANDESITE, 100
    ), 1, 30, PickaxeComponent.ANDESITE_CORE, ForgeCategories.PICKAXE_CORES),
    ROCK_HANDLE(Map.of(
            ResourceItem.ROCK, 100
    ), 1, 30, PickaxeComponent.ROCK_HANDLE, ForgeCategories.PICKAXE_HANDLES)
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
