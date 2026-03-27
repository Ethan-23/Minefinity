package org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes;

import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.WorkshopToolsTiers;

import java.util.Map;

public class BaseWorkshopRecipe extends BaseItemRecipe {
    WorkshopMode requiredToolType;
    WorkshopToolsTiers requiredToolsTier;
    int durabilityUsage;
    int unlockLevel;

    public BaseWorkshopRecipe(Map<String, Integer> recipe, String result, WorkshopMode requiredToolType, WorkshopToolsTiers requiredToolsTier, int unlockLevel, int durabilityUsage) {
        super(recipe, result);
        this.requiredToolType = requiredToolType;
        this.requiredToolsTier = requiredToolsTier;
        this.durabilityUsage = durabilityUsage;
        this.unlockLevel = unlockLevel;
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

    public int getUnlockLevel() {
        return unlockLevel;
    }
}
