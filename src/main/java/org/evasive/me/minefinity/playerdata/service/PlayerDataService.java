package org.evasive.me.minefinity.playerdata.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.repository.PlayerDataRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class PlayerDataService {

    private final Map<UUID, PlayerData> playerCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "minefinity-db-" + count.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    });

    private final PlayerDataRepository repository; // Handles DB operations
    private final PlayerDataComponentRegistry componentRegistry;

    private static final int SAVE_INTERVAL_MINUTES = 5;

    private final Set<UUID> firstJoinPlayers = ConcurrentHashMap.newKeySet();

    public PlayerDataService(PlayerDataRepository repository, PlayerDataComponentRegistry componentRegistry) {
        this.repository = repository;
        this.componentRegistry = componentRegistry;

        // Schedule periodic dirty saves
        scheduler.scheduleAtFixedRate(this::saveDirtyPlayers, SAVE_INTERVAL_MINUTES, SAVE_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    public boolean loadPlayer(UUID uuid) {
        if (playerCache.containsKey(uuid)) return true;
        try {
            Optional<PlayerData> optional = repository.loadPlayer(uuid);
            boolean firstJoin = optional.isEmpty();

            PlayerData data = optional.orElseGet(() -> {
                PlayerData newData = new PlayerData(uuid, componentRegistry);
                repository.savePlayer(newData);
                return newData;
            });

            playerCache.put(uuid, data);
            if (firstJoin) firstJoinPlayers.add(uuid);
            return true;
        } catch (RuntimeException e) {
            Minefinity.getCore().getLogger().log(Level.SEVERE,
                    "Failed to load data for " + uuid + "; denying login to protect existing data.", e);
            return false;
        }
    }

    public boolean consumeFirstJoin(UUID uuid) {
        return firstJoinPlayers.remove(uuid);
    }

    /**
     * Get player data from cache.
     */
    public PlayerData getPlayerData(UUID uuid) {
        return playerCache.get(uuid);
    }

    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Mark player as dirty and save immediately.
     */
    public void savePlayer(PlayerData playerData, boolean removeIfOffline) {
        CompletableFuture.runAsync(() -> savePlayerSync(playerData, removeIfOffline), dbExecutor);
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
        shutdownExecutor(scheduler);
        shutdownExecutor(dbExecutor);
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
