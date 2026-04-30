package org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.util.RequirementParser;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeCategories;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.ForgeRecipeManager;

import java.util.HashMap;
import java.util.Map;

public class ForgeRecipeConfig extends BaseConfig {

    private final ForgeRecipeManager forgeRecipeManager;

    public ForgeRecipeConfig(JavaPlugin plugin, ForgeRecipeManager forgeRecipeManager) {
        super(plugin, "recipes/forge-recipes.yml");
        this.forgeRecipeManager = forgeRecipeManager;
        load();
    }

    @Override
    public void load() {
        super.load();
        ConfigurationSection configurationSection = this.config.getConfigurationSection("recipes");
        if (configurationSection == null) {
            return;
        }

        Bukkit.getConsoleSender().sendMessage("LOADING FORGE RECIPES");

        configurationSection.getKeys(false).forEach(key -> {

            ConfigurationSection recipeSection = configurationSection.getConfigurationSection(key);

            if (recipeSection == null) {
                return;
            }

            Map<String, Integer> recipeMap = new HashMap<>();

            int craftTime = recipeSection.getInt("craft-time");
            float cost = (float) recipeSection.getDouble("cost");
            String categoryString = recipeSection.getString("forge-category");

            ForgeCategories forgeCategory = ForgeCategories.valueOf(categoryString);

            ConfigurationSection requirements = recipeSection.getConfigurationSection("requirements");
            ConfigurationSection ingredients = recipeSection.getConfigurationSection("recipe");

            if (ingredients != null) {
                ingredients.getKeys(false).forEach(recipeKey ->
                        recipeMap.put(recipeKey, ingredients.getInt(recipeKey))
                );
            }

            forgeRecipeManager.addRecipe(key, new BaseForgeRecipe(recipeMap, key, 1, craftTime, cost, forgeCategory, RequirementParser.parse(requirements)));

        });
    }
}
