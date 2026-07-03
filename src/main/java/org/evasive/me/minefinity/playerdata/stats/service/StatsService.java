package org.evasive.me.minefinity.playerdata.stats.service;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.PlayerDefaults;
import org.evasive.me.minefinity.playerdata.stats.StatContributor;
import org.evasive.me.minefinity.playerdata.stats.StatContributorRegistry;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class StatsService {

    private final Map<UUID, EnumMap<Stats, Integer>> cachedStats = new HashMap<>();
    private final PlayerDataService playerDataService;
    private final StatContributorRegistry contributorRegistry;

    public StatsService(PlayerDataService playerDataService, StatContributorRegistry contributorRegistry) {
        this.playerDataService = playerDataService;
        this.contributorRegistry = contributorRegistry;
    }

    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        cachedStats.put(uuid, playerDataService.getPlayerData(uuid).getPlayerStats());
        recalculateStats(player);
    }

    public EnumMap<Stats, Integer> getStats(UUID uuid) {
        return cachedStats.getOrDefault(uuid, PlayerDefaults.BASE);
    }

    public Map<String, Integer> getStringIdStats(UUID uuid) {
        Map<Stats, Integer> stats = cachedStats.getOrDefault(uuid, PlayerDefaults.BASE);

        return stats.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    public void recalculateStats(Player player) {
        UUID uuid = player.getUniqueId();

        PlayerData data = playerDataService.getPlayerData(player);
        if (data == null) return;

        EnumMap<Stats, Integer> result = new EnumMap<>(Stats.class);
        result.putAll(PlayerDefaults.BASE);

        // Feature modules push their stat sources (equipment, milestones, ...) through the registry;
        // this service never names a feature type.
        for (StatContributor contributor : contributorRegistry.all()) {
            for (var entry : contributor.contribute(player).entrySet()) {
                result.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        for (var entry : data.getPlayerStats().entrySet()) {
            result.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        cachedStats.put(uuid, result);
    }

    public int getStat(UUID uuid, Stats stat) {
        EnumMap<Stats, Integer> map = cachedStats.get(uuid);

        if (map == null) return PlayerDefaults.BASE.getOrDefault(stat, 0);

        return map.getOrDefault(stat, 0);
    }

    public void removePlayer(Player player) {
        cachedStats.remove(player.getUniqueId());
    }

    public void addStats(Player player, Map<Stats, Integer> stats) {
        for (Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            playerDataService.getPlayerData(player).addPlayerStats(entry.getKey(), entry.getValue());
        }
    }

    public void removeStats(Player player, Map<Stats, Integer> stats) {
        for (Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            playerDataService.getPlayerData(player).removePlayerStats(entry.getKey(), entry.getValue());
        }
    }
}
