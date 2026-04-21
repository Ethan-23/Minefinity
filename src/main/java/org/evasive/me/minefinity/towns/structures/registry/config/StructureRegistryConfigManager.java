package org.evasive.me.minefinity.towns.structures.registry.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.core.registry.StructureRegistry;
import org.evasive.me.minefinity.customItems.recipebuilder.data.BaseItemRecipe;
import org.evasive.me.minefinity.towns.structures.data.Structure;

import java.util.*;

public class StructureRegistryConfigManager extends BaseConfig {

    private final static String CONFIGURATION_SECTION = "worlds";
    private final StructureRegistry structureRegistry;

    public StructureRegistryConfigManager(StructureRegistry structureRegistry) {
        super(Minefinity.getCore(), "structure-registry.yml");
        this.structureRegistry = structureRegistry;
        load();
    }

    @Override
    public void load() {
        super.load();

        ConfigurationSection configurationSection = this.config.getConfigurationSection(CONFIGURATION_SECTION);

        if (configurationSection == null) {
            return;
        }

        configurationSection.getKeys(false).forEach(key -> {

            ConfigurationSection worldSection = configurationSection.getConfigurationSection(key);

            if (worldSection == null) {
                return;
            }

            worldSection.getKeys(false).forEach(structureName -> {

                ConfigurationSection structureVariables = worldSection.getConfigurationSection(structureName);
                if(structureVariables == null)
                    return;

                String structureId = key.toUpperCase() + "_" + structureName.toUpperCase();
                String display = structureVariables.getString("display");
                if(display == null) display = "BARRIER";
                Material displayMaterial = Material.getMaterial(display.toUpperCase());

                ConfigurationSection levelsSection = structureVariables.getConfigurationSection("levels");

                List<BaseItemRecipe> recipes = new ArrayList<>();
                List<Map<String, Integer>> milestoneRequirements = new ArrayList<>();




                if (levelsSection != null) {
                    for (String level : levelsSection.getKeys(false)) {

                        ConfigurationSection levelSection = levelsSection.getConfigurationSection(level);

                        if(levelSection == null) continue;

                        ConfigurationSection recipeSection = levelSection.getConfigurationSection(".recipe");

                        if (recipeSection != null){
                            Map<String, Integer> recipeMap = new LinkedHashMap<>();

                            for (String materialKey : recipeSection.getKeys(false)) {
                                int amount = recipeSection.getInt(materialKey);
                                recipeMap.put(materialKey, amount);
                            }

                            float cost = (float) levelSection.getDouble("cost");
                            BaseItemRecipe baseItemRecipe = new BaseItemRecipe(recipeMap, cost);
                            recipes.add(baseItemRecipe);
                        }

                        ConfigurationSection milestoneSection = levelSection.getConfigurationSection(".milestones");

                        if(milestoneSection != null){
                            Map<String, Integer> milestoneMap = new LinkedHashMap<>();

                            for (String materialKey : milestoneSection.getKeys(false)) {
                                int amount = milestoneSection.getInt(materialKey);
                                milestoneMap.put(materialKey, amount);
                            }

                            milestoneRequirements.add(milestoneMap);
                        }
                    }
                }


                Structure structure = new Structure(structureId, structureName, displayMaterial, recipes, milestoneRequirements);

                structureRegistry.registerStructure(structure);

            });
        });

    }
    
}
