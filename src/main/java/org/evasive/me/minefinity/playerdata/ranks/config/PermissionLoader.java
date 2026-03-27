package org.evasive.me.minefinity.playerdata.ranks.config;

import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.playerdata.model.Rank;
import org.evasive.me.minefinity.playerdata.ranks.data.RankCategory;
import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;

import java.util.HashSet;
import java.util.Set;

public class PermissionLoader {

    private final PermissionConfigManager configManager;
    private final RankRegistry registry;

    public PermissionLoader(PermissionConfigManager configManager, RankRegistry registry) {
        this.configManager = configManager;
        this.registry = registry;
    }

    public void loadRanks() {
        ConfigurationSection section = configManager.getPermissionConfig().getConfigurationSection("ranks");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String categoryString = section.getString(key + ".category");
            String prefix = section.getString(key + ".prefix", "");
            int weight = section.getInt(key + ".weight", 0);
            Set<String> permissions = new HashSet<>(section.getStringList(key + ".permissions"));

            if (categoryString == null) continue;

            RankCategory category = RankCategory.valueOf(categoryString.toUpperCase());

            Rank rank = new Rank(key, prefix, weight, permissions, category);
            registry.register(rank);
        }
    }


}
