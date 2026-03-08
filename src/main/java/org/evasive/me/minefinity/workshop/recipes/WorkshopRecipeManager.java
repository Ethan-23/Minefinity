package org.evasive.me.minefinity.workshop.recipes;

import org.evasive.me.minefinity.forge.recipes.BaseForgeRecipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkshopRecipeManager {

    Map<String, BaseWorkshopRecipe> recipes = new HashMap<>();

    public BaseWorkshopRecipe getRecipe(String id){
        return recipes.get(id);
    }

    public boolean containsRecipe(String id){
        return recipes.containsKey(id);
    }

    public void addRecipe(String id, BaseWorkshopRecipe recipe){
        recipes.put(id, recipe);
    }

    public Map<String, BaseWorkshopRecipe> getRecipes() {
        return Collections.unmodifiableMap(recipes);
    }
}
