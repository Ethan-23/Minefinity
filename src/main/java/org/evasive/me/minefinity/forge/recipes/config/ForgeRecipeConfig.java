package org.evasive.me.minefinity.forge.recipes.config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.recipes.BaseForgeRecipe;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipeManager;

import java.util.HashMap;
import java.util.Map;

public class ForgeRecipeConfig extends BaseConfig {

    private final ForgeRecipeManager forgeRecipeManager;

    public ForgeRecipeConfig(JavaPlugin plugin, ForgeRecipeManager forgeRecipeManager) {
        super(plugin, "recipes/forge-recipes.yml");
        this.forgeRecipeManager = forgeRecipeManager;
    }

    public void loadForgeRecipes(){
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

            int craftTime = recipeSection.getInt("craft-time");

            String categoryString = recipeSection.getString("forge-category");
            ForgeCategories forgeCategory = ForgeCategories.valueOf(categoryString);

            ConfigurationSection ingredients = recipeSection.getConfigurationSection("recipe");

            if (ingredients != null) {
                ingredients.getKeys(false).forEach(recipeKey ->
                        recipeMap.put(recipeKey, ingredients.getInt(recipeKey))
                );
            }

            forgeRecipeManager.addRecipe(key, new BaseForgeRecipe(recipeMap, key, 1, craftTime, forgeCategory));

        });
    }
}
