package org.evasive.me.minefinity.workshop.recipes.config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipeManager;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.recipes.BaseWorkshopRecipe;
import org.evasive.me.minefinity.workshop.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

import java.util.HashMap;
import java.util.Map;

public class WorkshopRecipeConfig extends BaseConfig {

    private final WorkshopRecipeManager workshopRecipeManager;

    public WorkshopRecipeConfig(JavaPlugin plugin, WorkshopRecipeManager workshopRecipeManager) {
        super(plugin, "recipes/workshop-recipes.yml");
        this.workshopRecipeManager = workshopRecipeManager;
    }

    public void loadWorkshopRecipes(){
        ConfigurationSection configurationSection = getConfig().getConfigurationSection("recipes");

        if (configurationSection == null) {
            return;
        }

        configurationSection.getKeys(false).forEach(key -> {

            ConfigurationSection recipeSection = configurationSection.getConfigurationSection(key);

            if (recipeSection == null) {
                return;
            }

            Map<String, Integer> recipeMap = new HashMap<>();
            WorkshopMode workshopMode = WorkshopMode.valueOf(recipeSection.getString("workshop-mode"));
            WorkshopToolsTiers workshopToolsTier = WorkshopToolsTiers.valueOf(recipeSection.getString("workshop-tool-tier"));
            int unlockLevel = recipeSection.getInt("unlock-level");
            int durabilityUsage = recipeSection.getInt("durability-usage");
            ConfigurationSection ingredients = recipeSection.getConfigurationSection("recipe");

            if (ingredients != null) {
                ingredients.getKeys(false).forEach(recipeKey ->
                        recipeMap.put(recipeKey, ingredients.getInt(recipeKey))
                );
            }

            workshopRecipeManager.addRecipe(key, new BaseWorkshopRecipe(recipeMap, key, workshopMode, workshopToolsTier, unlockLevel, durabilityUsage));

        });
    }
}
