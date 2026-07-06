package org.evasive.me.minefinity.playerdata.model;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.playerdata.component.ComponentType;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponentRegistry;
import org.evasive.me.minefinity.core.data.Stats;

import java.util.*;

public class PlayerData {

    private final UUID uuid;
    private String username;
    private double balance;

    //Transient combat/mining stats
    private EnumMap<Stats, Integer> playerStats;

    private final Map<Class<? extends PlayerDataComponent>, PlayerDataComponent> components = new HashMap<>();

    /**
     * New player: default scalar data plus a fresh default instance of every registered component.
     */
    public PlayerData(UUID uuid, PlayerDataComponentRegistry registry) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(uuid).getName();
        this.playerStats = new EnumMap<>(Stats.class);

        this.balance = 0;

        for (ComponentType<?> type : registry.all()) {
            set(type.defaultFactory().get());
        }
    }

    /**
     * Loaded player: scalar fields come from the database; components are populated afterwards by the
     * repository via {@link #set(PlayerDataComponent)}.
     */
    public PlayerData(UUID uuid, String username, double balance) {
        this.uuid = uuid;
        this.username = username;
        this.balance = balance;
        this.playerStats = new EnumMap<>(Stats.class);
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

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    // ---- Economy ----

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
