package org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.BaseSmelterRecipe;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.SmelterRecipeManager;

import java.util.HashMap;
import java.util.Map;

public class SmelterRecipeConfig extends BaseConfig {

    private final SmelterRecipeManager smelterRecipeManager;

    public SmelterRecipeConfig(JavaPlugin plugin, SmelterRecipeManager smelterRecipeManager) {
        super(plugin, "recipes/smelter-recipes.yml");
        this.smelterRecipeManager = smelterRecipeManager;
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


            ConfigurationSection ingredients = recipeSection.getConfigurationSection("recipe");

            int fuelTier = recipeSection.getInt("fuel-tier");
            int fuelAmount = recipeSection.getInt("fuel-amount");
            int unlockLevel = recipeSection.getInt("unlock-level");

            String firstKey = null;

            if (ingredients != null) {
                ingredients.getKeys(false).forEach(recipeKey -> {
                    int amount = ingredients.getInt(recipeKey);
                    recipeMap.put(recipeKey, amount);
                });
                firstKey = ingredients.getKeys(false).iterator().next();
            }



            //Smelter map puts input as key instead of output to search inventory since its 1:1
            smelterRecipeManager.addRecipe(firstKey, new BaseSmelterRecipe(recipeMap, key, fuelTier, fuelAmount, unlockLevel));

        });
    }
}
