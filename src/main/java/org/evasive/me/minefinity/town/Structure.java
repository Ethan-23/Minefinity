package org.evasive.me.minefinity.town;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.recipe.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.types.ResourceItem;

import java.util.List;
import java.util.Map;

public enum Structure {
    TOWNHALL(List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 0, ResourceItem.ROCKY_STONE, 0)),
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 30, ResourceItem.ROCKY_STONE, 30)),
                    new BaseItemRecipe(Map.of(ResourceItem.MOSSY_TIMBER, 100, ResourceItem.ROCKY_STONE, 100))
                    ),
            Material.LECTERN
    ),
    MERCHANT(List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 0, ResourceItem.ROCKY_STONE, 0)),
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 30, ResourceItem.ROCKY_STONE, 30)),
                    new BaseItemRecipe(Map.of(ResourceItem.MOSSY_TIMBER, 100, ResourceItem.ROCKY_STONE, 100))
            ),
            Material.CHEST
    ),
    FORGE(List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 0, ResourceItem.ROCKY_STONE, 0)),
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 30, ResourceItem.ROCKY_STONE, 30)),
                    new BaseItemRecipe(Map.of(ResourceItem.MOSSY_TIMBER, 100, ResourceItem.ROCKY_STONE, 100))
            ),
            Material.FURNACE
            ),
    WORKSHOP(List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 0, ResourceItem.ROCKY_STONE, 0)),
                    new BaseItemRecipe(Map.of(ResourceItem.TIMBER, 30, ResourceItem.ROCKY_STONE, 30)),
                    new BaseItemRecipe(Map.of(ResourceItem.MOSSY_TIMBER, 100, ResourceItem.ROCKY_STONE, 100))
            ),
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
