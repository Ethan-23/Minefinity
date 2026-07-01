package org.evasive.me.minefinity.playerdata.model;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.playerdata.component.ComponentType;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.*;

public class PlayerData {

    private final UUID uuid;
    private String username;

    //Mining Block Data
    private Map<String, Integer> unlockedBlockTiers;
    private Map<String, String> selectedBlockTiers;
    private EnumMap<Stats, Integer> playerStats;

    //Economy Data
    private double balance;

    //Backpack Storage Data
    private Map<String, Integer> backpackStorage;

    // Feature-owned per-player data (engineer, smelter, town, ...), keyed by concrete component class.
    // playerdata never names these types - feature modules register them and access their own slice.
    private final Map<Class<? extends PlayerDataComponent>, PlayerDataComponent> components = new HashMap<>();

    /**
     * New player: default scalar data plus a fresh default instance of every registered component.
     */
    public PlayerData(UUID uuid, PlayerDataComponentRegistry registry) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(uuid).getName();
        this.playerStats = new EnumMap<>(Stats.class);

        BlockTypeRegistryService blockTypeRegistryService = BlockTypeRegistryService.getInstance();
        String worldId = blockTypeRegistryService.getWorldList().getFirst();

        //Block setup - all worlds start on tier 0
        this.unlockedBlockTiers = new HashMap<>();
        this.unlockedBlockTiers.put(worldId, 0);
        this.selectedBlockTiers = new HashMap<>();
        this.selectedBlockTiers.put(worldId, blockTypeRegistryService.getBlockIdByTier(worldId, 0));

        this.balance = 0;
        this.backpackStorage = new HashMap<>();

        for (ComponentType<?> type : registry.all()) {
            set(type.defaultFactory().get());
        }
    }

    /**
     * Loaded player: scalar fields come from the database; components are populated afterwards by the
     * repository via {@link #set(PlayerDataComponent)}.
     */
    public PlayerData(UUID uuid, String username, double balance,
                      Map<String, Integer> unlockedBlockTiers, Map<String, String> selectedBlockTiers,
                      Map<String, Integer> backpackStorage) {
        this.uuid = uuid;
        this.username = username;
        this.balance = balance;
        this.playerStats = new EnumMap<>(Stats.class);
        this.unlockedBlockTiers = unlockedBlockTiers != null ? unlockedBlockTiers : new HashMap<>();
        this.selectedBlockTiers = selectedBlockTiers != null ? selectedBlockTiers : new HashMap<>();
        this.backpackStorage = backpackStorage != null ? new HashMap<>(backpackStorage) : new HashMap<>();
    }

    // ---- Feature component access ----

    /**
     * Get this player's slice of a feature's data, e.g. {@code playerData.get(Engineer.class)}.
     */
    public <T extends PlayerDataComponent> T get(Class<T> type) {
        return type.cast(components.get(type));
    }

    /**
     * Store or replace a component (keyed by its concrete class).
     */
    public void set(PlayerDataComponent component) {
        components.put(component.getClass(), component);
    }

    // ---- Identity ----

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ---- Mining block tiers ----

    public void setUnlockedBlockTiers(HashMap<String, Integer> unlockedBlockTiers) {
        this.unlockedBlockTiers = unlockedBlockTiers;
    }

    public Map<String, Integer> getUnlockedBlockTiers() {
        return unlockedBlockTiers;
    }

    public void setSelectedBlockTiers(Map<String, String> selectedBlockTiers) {
        this.selectedBlockTiers = selectedBlockTiers;
    }

    public Map<String, String> getSelectedBlockTiers() {
        return selectedBlockTiers;
    }

    public void setUnlockedBlockTier(String worldName, int tier){
        this.unlockedBlockTiers.put(worldName, tier);
    }


    public int getUnlockedBlockTier(String worldName){
        if(!this.unlockedBlockTiers.containsKey(worldName)){
            return unlockedBlockTiers.get("world");
        }
        return this.unlockedBlockTiers.get(worldName);
    }

    public void setSelectedBlockTier(String worldName, String tierName){
        this.selectedBlockTiers.put(worldName, tierName);
    }

    public String getSelectedBlockTier(String worldName){
        if(!this.selectedBlockTiers.containsKey(worldName)){
            throw new IllegalArgumentException("Invalid world name: " + worldName);
        }
        return this.selectedBlockTiers.get(worldName);
    }

    public boolean hasWorldUnlocked(String worldName){
        return selectedBlockTiers.containsKey(worldName);
    }

    // ---- Economy ----

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // ---- Backpack ----

    public Map<String, Integer> getBackpackStorage() {
        return Collections.unmodifiableMap(backpackStorage);
    }

    public Integer getBackpackStorageValue(String itemId) {
        return backpackStorage.get(itemId);
    }

    public void setBackpackStorage(String itemId, Integer value) {
        backpackStorage.put(itemId, value);
    }

    public void changeBackpackStorage(String itemId, Integer value) {
        backpackStorage.put(itemId, backpackStorage.getOrDefault(itemId, 0) + value);
    }

    public void clearBackpackStorage() {
        backpackStorage.clear();
    }

    public void setBackpackStorage(Map<String, Integer> backpackStorage) {
        this.backpackStorage = backpackStorage;
    }

    // ---- Stats (transient, not persisted) ----

    public EnumMap<Stats, Integer> getPlayerStats() {
        return playerStats;
    }

    public void addPlayerStats(Stats stats, int amount) {
        playerStats.put(stats, playerStats.get(stats) + amount);
    }

    public void removePlayerStats(Stats stats, int amount) {
        playerStats.put(stats, playerStats.get(stats) + amount);
    }

    public void setPlayerStats(EnumMap<Stats, Integer> playerStats) {
        this.playerStats = playerStats;
    }
}
