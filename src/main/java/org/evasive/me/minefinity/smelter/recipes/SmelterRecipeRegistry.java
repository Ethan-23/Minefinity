package org.evasive.me.minefinity.smelter.recipes;

import org.evasive.me.minefinity.core.items.CustomItem;

import java.util.HashMap;
import java.util.Map;

public class SmelterRecipeRegistry {

    private static final Map<CustomItem, SmelterRecipes> loadedSmelterRecipes = new HashMap<>();

    public static void loadRecipes(){
        for (SmelterRecipes smelterRecipes : SmelterRecipes.values() ) {
            smelterRecipes.getSmelterRecipe().getRecipe().forEach((key, value) ->
                    loadedSmelterRecipes.put(key, smelterRecipes));

        }
    }

    public static SmelterRecipes getRecipe(CustomItem item){
        return loadedSmelterRecipes.getOrDefault(item, null);
    }

}
