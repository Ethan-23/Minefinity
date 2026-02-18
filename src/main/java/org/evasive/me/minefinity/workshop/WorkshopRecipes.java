package org.evasive.me.minefinity.workshop;

import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.ResourceItem;

import java.util.Map;

public enum WorkshopRecipes {
    ROCKY_STONE(Map.of(
            ResourceItem.ROCK, 5,
            ResourceItem.STONE, 10
    ), ResourceItem.ROCKY_STONE, WorkshopMode.STONEWORKING, WorkshopToolsTiers.FLINT, 1, 1),
    TOUGH_STONE(Map.of(
            ResourceItem.ROCKY_STONE, 5,
            ResourceItem.TUFF_BRICK, 10
    ), ResourceItem.TOUGH_STONE, WorkshopMode.STONEWORKING, WorkshopToolsTiers.BRONZE_INGOT, 2, 2),
    TIMBER(Map.of(
            ResourceItem.OAK_PLANK, 5
    ), ResourceItem.TIMBER, WorkshopMode.CARPENTRY, WorkshopToolsTiers.FLINT, 1, 1),
    MOSSY_TIMBER(Map.of(
            ResourceItem.MOSS, 3,
            ResourceItem.TIMBER, 2
    ), ResourceItem.MOSSY_TIMBER, WorkshopMode.CARPENTRY, WorkshopToolsTiers.BRONZE_INGOT, 2, 2)
    ;

    public final BaseWorkshopRecipe baseWorkshopRecipe;

    WorkshopRecipes(Map<CustomItem, Integer> recipe, ResourceItem result, WorkshopMode requiredToolType, WorkshopToolsTiers requiredToolsTier, int unlockLevel, int durabilityUsage) {
        this.baseWorkshopRecipe = new BaseWorkshopRecipe(recipe, result, requiredToolType, requiredToolsTier, unlockLevel, durabilityUsage);
    }

    public BaseWorkshopRecipe getRecipe() {
        return this.baseWorkshopRecipe;
    }
}
