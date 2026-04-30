package org.evasive.me.minefinity.towns.structures.data;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.BaseItemRecipe;

import java.util.List;
import java.util.Map;

public record Structure(String id, String name, Material displayMaterial, List<BaseItemRecipe> upgrades,
                        List<Map<String, Integer>> milestoneRequirements) {

    public int getMaxLevel(){
        return upgrades.size();
    }

    public BaseItemRecipe getUpgradeMap(int level){
        return upgrades.get(level);
    }

    public Map<String, Integer> getMilestoneRequirements(int level){
        return milestoneRequirements.get(level);
    }

}
