package org.evasive.me.minefinity.town;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseItemRecipe;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.workshop.WorkshopToolsTiers;

import java.util.List;
import java.util.Map;

public enum Structure {
    TOWNHALL(5,
            List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 0, ResourceItem.COBBLESTONE, 0, ResourceItem.GRAVEL, 0)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 10, ResourceItem.COBBLESTONE, 10, ResourceItem.GRAVEL, 10)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000))
                    ),
            Material.LECTERN
    ),
    MERCHANT(5,
            List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000))
            ),
            Material.CHEST
    ),
    FORGE(5,
            List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000))
            ),
            Material.FURNACE
            ),
    WORKSHOP(5,
            List.of(
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000)),
                    new BaseItemRecipe(Map.of(ResourceItem.OAK_PLANK, 1000, ResourceItem.COBBLESTONE, 500, ResourceItem.GRAVEL, 2000))
            ),
            Material.STONECUTTER
    ),
    /*DUNGEON(1, List.of(Map.of()), Material.SKELETON_SKULL)*/;

    final BaseStructure baseStructure;

    Structure(int maxLevel, List<BaseItemRecipe> upgradeMaps, Material displayMaterial) {
        this.baseStructure = new BaseStructure(maxLevel, upgradeMaps, displayMaterial);
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
