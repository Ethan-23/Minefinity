package org.evasive.me.minefinity.town;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.core.items.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.items.ResourceItem;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseStructure {

    private final int maxLevel;
    private final List<BaseItemRecipe> upgradeMaps;
    Material displayMaterial;

    public BaseStructure(int maxLevel, List<BaseItemRecipe> upgradeMaps, Material displayMaterial) {
        this.maxLevel = maxLevel;
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
