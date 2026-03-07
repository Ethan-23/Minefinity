package org.evasive.me.minefinity.town;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.recipe.BaseItemRecipe;

import java.util.List;
import java.util.Map;

public enum Structure {
    TOWNHALL(List.of(),
            Material.LECTERN
    ),
    MERCHANT(List.of(),
            Material.CHEST
    ),
    FORGE(List.of(),
            Material.FURNACE
            ),
    WORKSHOP(List.of(),
            Material.STONECUTTER
    ),
    /*DUNGEON(1, List.of(Map.of()), Material.SKELETON_SKULL)*/;

    final BaseStructure baseStructure;

    Structure(List<BaseItemRecipe> upgradeMaps, Material displayMaterial) {
        this.baseStructure = new BaseStructure(upgradeMaps, displayMaterial);
    }

    public BaseStructure getBaseStructure(){
        return baseStructure;
    }

    public int getMaxLevel(){
        return getBaseStructure().getMaxLevel();
    }

    public BaseItemRecipe getUpgradeMap(int level){
        return getBaseStructure().getUpgradeMap(level);
    }

    public static boolean contains(String value) {
        for (Structure structure : Structure.values()) {
            if (structure.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
