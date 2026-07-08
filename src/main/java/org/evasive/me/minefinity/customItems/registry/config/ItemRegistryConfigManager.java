package org.evasive.me.minefinity.customItems.registry.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.evasive.me.minefinity.Minefinity;

import java.io.File;
import java.io.IOException;

public class ItemRegistryConfigManager {

    private final File itemRegistryConfigFile;
    private final FileConfiguration itemRegistryConfig;

    private static final String CONFIG_FILE = "custom-item-registry.yml";
    private static final String ITEM_SECTION = "items";

    public ItemRegistryConfigManager() {
        this.itemRegistryConfigFile = new File(Minefinity.getCore().getDataFolder(), CONFIG_FILE);
        this.itemRegistryConfig = new YamlConfiguration();

        if(!createItemRegistryConfig()){
            return;
        }

        ConfigurationSection itemSection = itemRegistryConfig.getConfigurationSection(ITEM_SECTION);
        if(itemSection == null)
            itemRegistryConfig.createSection(ITEM_SECTION);

        saveItemRegistryConfig();
    }

    public boolean createItemRegistryConfig() {
        if (!itemRegistryConfigFile.exists()) {
            Minefinity.getCore().saveResource(CONFIG_FILE, false);
        }

        try {
            itemRegistryConfig.load(itemRegistryConfigFile);
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FileConfiguration getItemRegistryConfig() {
        return itemRegistryConfig;
    }

    public void saveItemRegistryConfig() {
        try {
            itemRegistryConfig.save(itemRegistryConfigFile);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public ConfigurationSection getItemConfigSection(){
        return itemRegistryConfig.getConfigurationSection(ITEM_SECTION);
    }

}
