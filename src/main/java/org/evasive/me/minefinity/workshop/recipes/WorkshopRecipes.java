package org.evasive.me.minefinity.workshop.recipes;

import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

import java.util.Map;

public enum WorkshopRecipes {
    ROCKY_STONE(Map.of(
    ), null, WorkshopMode.STONEWORKING, WorkshopToolsTiers.FLINT, 1, 1),
    TOUGH_STONE(Map.of(
    ), null, WorkshopMode.STONEWORKING, WorkshopToolsTiers.BRONZE_INGOT, 2, 2),
    TIMBER(Map.of(
    ), null, WorkshopMode.CARPENTRY, WorkshopToolsTiers.FLINT, 1, 1),
    MOSSY_TIMBER(Map.of(
    ), null, WorkshopMode.CARPENTRY, WorkshopToolsTiers.BRONZE_INGOT, 2, 2)
    ;

    public final BaseWorkshopRecipe baseWorkshopRecipe;

    WorkshopRecipes(Map<CustomItem, Integer> recipe, String result, WorkshopMode requiredToolType, WorkshopToolsTiers requiredToolsTier, int unlockLevel, int durabilityUsage) {
        this.baseWorkshopRecipe = new BaseWorkshopRecipe(recipe, CustomItemRegistry.getByID(result), requiredToolType, requiredToolsTier, unlockLevel, durabilityUsage);
    }

    public BaseWorkshopRecipe getRecipe() {
        return this.baseWorkshopRecipe;
    }
}
