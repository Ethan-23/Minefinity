package org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes;

import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.WorkshopToolsTiers;

import java.util.List;
import java.util.Map;

public class BaseWorkshopRecipe extends BaseItemRecipe {
    WorkshopMode requiredToolType;
    WorkshopToolsTiers requiredToolsTier;
    int durabilityUsage;

    public BaseWorkshopRecipe(Map<String, Integer> recipe, String result, WorkshopMode requiredToolType, WorkshopToolsTiers requiredToolsTier, float cost, int durabilityUsage, List<RecipeRequirement> requirements) {
        super(recipe, result, cost, requirements);
        this.requiredToolType = requiredToolType;
        this.requiredToolsTier = requiredToolsTier;
        this.durabilityUsage = durabilityUsage;
    }

    public WorkshopMode getRequiredToolType() {
        return requiredToolType;
    }

    public WorkshopToolsTiers getRequiredToolsTier() {
        return requiredToolsTier;
    }

    public int getDurabilityUsage() {
        return durabilityUsage;
    }

}
