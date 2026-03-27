package org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.BaseWorkshopRecipe;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.recipes.WorkshopRecipeManager;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.WorkshopToolsTiers;

import java.util.HashMap;
import java.util.Map;

public class WorkshopRecipeConfig extends BaseConfig {

    private final WorkshopRecipeManager workshopRecipeManager;

    public WorkshopRecipeConfig(JavaPlugin plugin, WorkshopRecipeManager workshopRecipeManager) {
        super(plugin, "recipes/workshop-recipes.yml");
        this.workshopRecipeManager = workshopRecipeManager;
        load();
    }

    @Override
    public void load() {
        super.load();
        ConfigurationSection configurationSection = this.config.getConfigurationSection("recipes");

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
