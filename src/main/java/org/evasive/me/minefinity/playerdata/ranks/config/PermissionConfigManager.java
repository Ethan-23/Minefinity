package org.evasive.me.minefinity.playerdata.ranks.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.evasive.me.minefinity.Minefinity;

import java.io.File;
import java.io.IOException;

public class PermissionConfigManager {

    private File permissionConfigFile;
    private FileConfiguration permissionConfig;

    public void createPermissionConfig(){
        permissionConfigFile = new File(Minefinity.getCore().getDataFolder(),  "permissions.yml");
        if(!permissionConfigFile.exists()){
            Minefinity.getCore().saveResource("permissions.yml",false);
        }

        permissionConfig = new YamlConfiguration();

        try {
            permissionConfig.load(permissionConfigFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getPermissionConfig() {
        return permissionConfig;
    }
}
