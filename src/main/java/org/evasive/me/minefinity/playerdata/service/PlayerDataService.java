package org.evasive.me.minefinity.playerdata.service;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.repository.PlayerDataRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class PlayerDataService {

    private final Map<UUID, PlayerData> playerCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final PlayerDataRepository repository; // Handles DB operations
    private final PlayerDataComponentRegistry componentRegistry;

    private static final int SAVE_INTERVAL_MINUTES = 5;

    public PlayerDataService(PlayerDataRepository repository, PlayerDataComponentRegistry componentRegistry) {
        this.repository = repository;
        this.componentRegistry = componentRegistry;

        // Schedule periodic dirty saves
        scheduler.scheduleAtFixedRate(this::saveDirtyPlayers, SAVE_INTERVAL_MINUTES, SAVE_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Load player data asynchronously on join.
     */
    public void loadPlayerAsync(UUID uuid, Consumer<Boolean> onComplete) {
        // If already loaded, just run the callback
        if (playerCache.containsKey(uuid)) {
            if (onComplete != null) onComplete.accept(false);
            return;
        }

        CompletableFuture
                .supplyAsync(() -> repository.loadPlayer(uuid))
                .thenAccept(optionalData -> {

                    boolean firstJoin = optionalData.isEmpty();

                    PlayerData data = optionalData.orElseGet(() -> {
                        PlayerData newData = new PlayerData(uuid, componentRegistry);
                        repository.savePlayer(newData);
                        return newData;
                    });

                    playerCache.put(uuid, data);

                    if (onComplete != null) {
                        onComplete.accept(firstJoin);
                    }
                });
    }

    /**
     * Get player data from cache.
     */
    public PlayerData getPlayerData(UUID uuid) {
        return playerCache.get(uuid);
    }

    /**
     * Mark player as dirty and save immediately.
     */
    public void savePlayer(PlayerData playerData, boolean removeIfOffline) {
        CompletableFuture.runAsync(() -> {
            savePlayerSync(playerData, removeIfOffline);
        });
    }

    public void savePlayerSync(PlayerData playerData, boolean removeIfOffline) {
        repository.savePlayer(playerData);
        if (removeIfOffline && Bukkit.getPlayer(playerData.getUuid()) == null) {
            playerCache.remove(playerData.getUuid());
        }
    }

    /**
     * Save all dirty players in cache.
     */
    public void saveDirtyPlayers() {
        playerCache.values().forEach(playerData -> savePlayer(playerData, true));
    }

    public void saveDirtyPlayersSync(){
        playerCache.values().forEach(playerData -> savePlayerSync(playerData, true));
    }

    /**
     * Remove player manually (on quit or cleanup)
     */
    public void removePlayer(UUID uuid) {
        playerCache.remove(uuid);
    }

    /**
     * Shutdown scheduler cleanly (on server stop)
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
