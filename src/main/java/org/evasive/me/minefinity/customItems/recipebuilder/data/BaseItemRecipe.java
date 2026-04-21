package org.evasive.me.minefinity.customItems.recipebuilder.data;

import java.util.Map;

public class BaseItemRecipe {

    private final Map<String, Integer> recipe;
    private final float cost;
    private final String resultId;

    public BaseItemRecipe(Map<String, Integer> recipe){
        this.recipe = recipe;
        this.cost = 0;
        this.resultId = null;
    }

    public BaseItemRecipe(Map<String, Integer> recipe, float cost){
        this.recipe = recipe;
        this.cost = cost;
        this.resultId = null;
    }

    public BaseItemRecipe(Map<String, Integer> recipe, String resultId) {
        this.recipe = recipe;
        this.resultId = resultId;
        this.cost = 0;
    }

    public BaseItemRecipe(Map<String, Integer> recipe, String resultId, float cost) {
        this.recipe = recipe;
        this.resultId = resultId;
        this.cost = cost;
    }

    public Map<String, Integer> getRecipe() {
        return recipe;
    }

    public String getResult() {
        return resultId;
    }

    public float getCost() {
        return cost;
    }
}
