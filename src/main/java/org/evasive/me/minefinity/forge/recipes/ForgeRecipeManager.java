package org.evasive.me.minefinity.forge.recipes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ForgeRecipeManager {

    Map<String, BaseForgeRecipe> recipes = new HashMap<>();

    public BaseForgeRecipe getRecipe(String id){
        return recipes.get(id);
    }

    public boolean containsRecipe(String id){
        return recipes.containsKey(id);
    }

    public void addRecipe(String id, BaseForgeRecipe recipe){
        recipes.put(id, recipe);
    }

    public Map<String, BaseForgeRecipe> getRecipes() {
        return Collections.unmodifiableMap(recipes);
    }
}
