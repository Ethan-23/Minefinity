package org.evasive.me.minefinity.towns.structures.resourceblock.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.config.BaseConfig;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.List;

public class MiningBlockRegistryConfig extends BaseConfig {

    private final String CONFIGURATION_SECTION = "worlds";
    private final BlockTypeRegistry blockTypeRegistry;

    public MiningBlockRegistryConfig(BlockTypeRegistry blockTypeRegistry) {
        super(Minefinity.getCore(), "mining-block-registry.yml");
        this.blockTypeRegistry = blockTypeRegistry;
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

            blockTypeRegistry.registerBlockTrack(key);

            worldSection.getKeys(false).forEach(blockName -> {

                ConfigurationSection blockVariables = worldSection.getConfigurationSection(blockName);
                if(blockVariables == null)
                    return;

                String materialName = blockVariables.getString("material");
                Material material = Material.getMaterial(materialName.toUpperCase());
                int health = blockVariables.getInt("health");
                int breakingPower = blockVariables.getInt("breaking-power");
                String dropId = blockVariables.getString("drop");
                String specialDropId = blockVariables.getString("special-drop");
                int unlockCost = blockVariables.getInt("unlock-cost");
                List<Integer> milestones = (List<Integer>) blockVariables.getList("milestones");

                BaseBlock baseBlock = new BaseBlock(blockName, material, breakingPower, health, dropId, specialDropId, unlockCost, milestones);

                blockTypeRegistry.registerBlock(key, baseBlock);

            });
        });

    }

}
