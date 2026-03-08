package org.evasive.me.minefinity.town;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;

import java.util.List;

public class BaseStructure {

    private final int maxLevel;
    private final List<BaseItemRecipe> upgradeMaps;
    Material displayMaterial;

    public BaseStructure(List<BaseItemRecipe> upgradeMaps, Material displayMaterial) {
        this.maxLevel = upgradeMaps.size();
        this.upgradeMaps = upgradeMaps;
        this.displayMaterial = displayMaterial;
    }

    public BaseItemRecipe getUpgradeMap(int level){
        return upgradeMaps.get(level);
    }

    public int getMaxLevel(){
        return maxLevel;
    }

    public Material getDisplayMaterial(){
        return displayMaterial;
    }
}
