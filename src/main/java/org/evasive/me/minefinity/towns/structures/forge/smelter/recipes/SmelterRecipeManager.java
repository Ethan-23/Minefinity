package org.evasive.me.minefinity.towns.structures.forge.smelter.recipes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SmelterRecipeManager {

    Map<String, BaseSmelterRecipe> recipes = new HashMap<>();

    public BaseSmelterRecipe getRecipe(String id){
        return recipes.get(id);
    }

    public boolean containsRecipe(String id){
        return recipes.containsKey(id);
    }

    public void addRecipe(String id, BaseSmelterRecipe recipe){
        recipes.put(id, recipe);
    }

    public Map<String, BaseSmelterRecipe> getRecipes() {
        return Collections.unmodifiableMap(recipes);
    }

}
