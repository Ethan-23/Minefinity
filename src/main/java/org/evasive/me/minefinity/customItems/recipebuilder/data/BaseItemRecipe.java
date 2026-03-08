package org.evasive.me.minefinity.customItems.recipebuilder.data;

import java.util.Map;

public class BaseItemRecipe {

    Map<String, Integer> recipe;
    String resultId;

    public BaseItemRecipe(Map<String, Integer> recipe){
        this.recipe = recipe;
        this.resultId = null;
    }

    public BaseItemRecipe(Map<String, Integer> recipe, String resultId) {
        this.recipe = recipe;
        this.resultId = resultId;
    }

    public Map<String, Integer> getRecipe() {
        return recipe;
    }

    public String getResult() {
        return resultId;
    }
}
