package org.evasive.me.minefinity.customItems.recipes.recipebuilder.data;

import org.evasive.me.minefinity.playerdata.model.PlayerData;

public interface RecipeRequirement {
    boolean isMet(PlayerData player);
    String getDisplay();
}
