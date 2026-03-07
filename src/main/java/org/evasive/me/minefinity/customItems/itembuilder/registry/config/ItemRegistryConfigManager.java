package org.evasive.me.minefinity.customItems.itembuilder.registry.config;

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

    public ItemRegistryConfigManager() {
        this.itemRegistryConfigFile = new File(Minefinity.getCore().getDataFolder(), "itemregistry.yml");
        this.itemRegistryConfig = new YamlConfiguration();

        createItemRegistryConfig(); // load first

        ConfigurationSection itemSection = itemRegistryConfig.getConfigurationSection("items");
        if(itemSection == null)
            itemRegistryConfig.createSection("items");

        saveItemRegistryConfig();
    }

    public void createItemRegistryConfig() {
        if(!itemRegistryConfigFile.exists()){
            Minefinity.getCore().saveResource("itemregistry.yml",false);
        }

        try {
            itemRegistryConfig.load(itemRegistryConfigFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
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
        return itemRegistryConfig.getConfigurationSection("items");
    }

}
