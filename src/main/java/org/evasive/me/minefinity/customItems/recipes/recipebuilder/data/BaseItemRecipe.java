package org.evasive.me.minefinity.customItems.recipes.recipebuilder.data;

import org.evasive.me.minefinity.playerdata.model.PlayerData;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseItemRecipe {

    private final Map<String, Integer> recipe;
    private final float cost;
    private final String resultId;
    private final List<RecipeRequirement> requirements;

    public BaseItemRecipe(Map<String, Integer> recipe, float cost, List<RecipeRequirement> requirements) {
        this.recipe = recipe;
        this.cost = cost;
        this.resultId = null;
        this.requirements = requirements;
    }

    public BaseItemRecipe(Map<String, Integer> recipe, String resultId, float cost, List<RecipeRequirement> requirements) {
        this.recipe = recipe;
        this.resultId = resultId;
        this.cost = cost;
        this.requirements = requirements;
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

    public List<RecipeRequirement> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    public boolean hasRequirements() {
        return !requirements.isEmpty();
    }

    public boolean completedRequirements(PlayerData playerData){
        for(RecipeRequirement requirement : requirements){
            if(!requirement.isMet(playerData))
                return false;
        }
        return true;
    }
}
