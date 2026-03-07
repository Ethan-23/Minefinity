package org.evasive.me.minefinity.workshop.recipes;

import org.evasive.me.minefinity.core.recipe.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

import java.util.Map;

public class BaseWorkshopRecipe extends BaseItemRecipe {
    WorkshopMode requiredToolType;
    WorkshopToolsTiers requiredToolsTier;
    int durabilityUsage;
    int unlockLevel;

    public BaseWorkshopRecipe(Map<CustomItem, Integer> recipe, CustomItem result, WorkshopMode requiredToolType, WorkshopToolsTiers requiredToolsTier, int unlockLevel, int durabilityUsage) {
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
