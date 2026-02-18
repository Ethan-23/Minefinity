package org.evasive.me.minefinity.player;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    private final HashMap<UUID, MinefinityPlayer> playerDataMap = new HashMap<>();

    public void registerPlayer(UUID uuid) {
        playerDataMap.put(uuid, new MinefinityPlayer(uuid));
    }

    public void registerPlayer(UUID uuid, MinefinityPlayer minefinityPlayer) {
        playerDataMap.put(uuid, minefinityPlayer);
    }

    public boolean has(Player player){
        return playerDataMap.containsKey(player.getUniqueId());
    }

    public MinefinityPlayer get(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public MinefinityPlayer get(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public Map<UUID, MinefinityPlayer> getAll() {
        return Collections.unmodifiableMap(playerDataMap);
    }
}
