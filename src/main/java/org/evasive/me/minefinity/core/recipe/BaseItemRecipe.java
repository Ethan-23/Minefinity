package org.evasive.me.minefinity.core.recipe;

import org.evasive.me.minefinity.core.items.CustomItem;

import java.util.Map;

public class BaseItemRecipe {

    Map<CustomItem, Integer> recipe;
    CustomItem result;

    public BaseItemRecipe(Map<CustomItem, Integer> recipe){
        this.recipe = recipe;
        this.result = null;
    }

    public BaseItemRecipe(Map<CustomItem, Integer> recipe, CustomItem result) {
        this.recipe = recipe;
        this.result = result;
    }

    public Map<CustomItem, Integer> getRecipe() {
        return recipe;
    }

    public CustomItem getResult() {
        return result;
    }
}
