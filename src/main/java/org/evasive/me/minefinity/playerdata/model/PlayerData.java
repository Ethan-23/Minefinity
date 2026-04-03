package org.evasive.me.minefinity.playerdata.model;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.mining.milestones.BlockMilestone;
import org.evasive.me.minefinity.towns.data.TownData;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.BaseForgeItem;
import org.evasive.me.minefinity.towns.structures.forge.smelter.Smelter;
import org.evasive.me.minefinity.towns.structures.mines.miner.AutoMinerData;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.Engineer;

import java.util.*;

public class PlayerData {

    private final UUID uuid;
    private String username;

    //Mining Block Data
    private Map<String, Integer> unlockedBlockTiers;
    private Map<String, String> selectedBlockTiers;

    private BlockMilestone blockMilestones;

    //Economy Data
    private double balance;

    //Town Data
    private TownData townData;
    //Miner data
    private AutoMinerData autoMinerData;
    //Engineer data
    private Engineer engineer;
    //Forge data
    private Smelter smelter;
    //Max size of 5
    private Map<Integer, BaseForgeItem> forgeItems;
    //Backpack Storage Data
    private Map<String, Integer> backpackStorage;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(uuid).getName();

        //Access world registry and set all worlds to tier 1 block

        this.unlockedBlockTiers = new HashMap<>(Map.of("world",0, "world_mines", 0, "world_nether", 0, "world_the_end", 0));
        this.selectedBlockTiers = new HashMap<>(Map.of("world", "DIRT", "world_mines", "DEEPSLATE", "world_nether", "NETHERRACK", "world_the_end", "END_STONE")); //Update eventually to add all worlds from game
        this.blockMilestones = new BlockMilestone();
        this.balance = 0;
        this.townData = new TownData();
        this.autoMinerData = new AutoMinerData();
        this.engineer = new Engineer();
        this.smelter = new Smelter();
        this.forgeItems = new HashMap<>();
        this.backpackStorage = new HashMap<>();
    }

    public PlayerData(UUID uuid, String username, Map<String, Integer> unlockedBlockTier, Map<String, String> selectedBlockTier, BlockMilestone blockMilestones, double balance, TownData townData, AutoMinerData autoMinerData, Engineer engineer, Smelter smelter, Map<Integer, BaseForgeItem> forgeItems, Map<String, Integer> backpackStorage) {
        this.uuid = uuid;
        this.username = username;
        this.unlockedBlockTiers = unlockedBlockTier;
        this.selectedBlockTiers = selectedBlockTier;
        this.blockMilestones = blockMilestones;
        this.balance = balance;
        this.townData = townData;
        this.autoMinerData = autoMinerData;
        this.engineer = engineer;
        this.smelter = smelter;
        this.forgeItems = forgeItems != null ? new HashMap<>(forgeItems) : new HashMap<>();
        this.backpackStorage = backpackStorage != null ? new HashMap<>(backpackStorage) : new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public BlockMilestone getBlockMilestones() {
        return blockMilestones;
    }

    public void setBlockMilestones(BlockMilestone blockMilestones) {
        this.blockMilestones = blockMilestones;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TownData getTownData() {
        return townData;
    }

    public void setTownData(TownData townData) {
        this.townData = townData;
    }

    public AutoMinerData getAutoMinerData() {
        return autoMinerData;
    }

    public void setAutoMinerData(AutoMinerData autoMinerData) {
        this.autoMinerData = autoMinerData;
    }

    public Engineer getEngineer() {
        return engineer;
    }

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
    }

    public Smelter getSmelter() {
        return smelter;
    }

    public void setSmelter(Smelter smelter) {
        this.smelter = smelter;
    }

    public Map<Integer, BaseForgeItem> getForgeItems() {
        return Collections.unmodifiableMap(forgeItems);
    }

    public BaseForgeItem getForgeItem(int slot) {
        return forgeItems.get(slot);
    }

    public void setForgeItem(int slot, BaseForgeItem baseForgeItem) {
        if (slot < 1 || slot > 5)
            throw new IllegalArgumentException("Forge slot out of bounds");
        forgeItems.put(slot, baseForgeItem);
    }

    public void setForgeItems(Map<Integer, BaseForgeItem> forgeItems) {
        this.forgeItems = forgeItems;
    }

    public boolean removeForgeItem(int slot) {
        return forgeItems.remove(slot) != null;
    }

    public boolean isForgeSlotEmpty(int slot) {
        return !forgeItems.containsKey(slot);
    }

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

}
