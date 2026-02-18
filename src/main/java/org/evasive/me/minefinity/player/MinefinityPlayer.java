package org.evasive.me.minefinity.player;

import org.bukkit.Material;
import org.evasive.me.minefinity.automation.miner.data.AutoMiner;
import org.evasive.me.minefinity.ranks.PlayerRank;
import org.evasive.me.minefinity.ranks.StaffRank;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.forge.data.ForgeItem;
import org.evasive.me.minefinity.resourceblock.ResourceData;
import org.evasive.me.minefinity.town.Town;
import org.evasive.me.minefinity.workshop.data.Engineer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinefinityPlayer {

    private final UUID uuid;
    private Town town;
    private double balance;
    private BlockType blockType;
    private int selectedBlockTier;
    private long blocksMined;

    private Map<BlockType, ResourceData> blockMilestones;
    private final AutoMiner autoMiner;
    private Engineer engineer;
    private Map<String, Integer> backpackStorage;
    private final Map<Integer, ForgeItem> forgeItems;

    //Not implemented yet
    private int quest;
    //Does not need to save between instance
    private int selectedForge;

    public MinefinityPlayer(UUID uuid) {
        this.uuid = uuid;
        this.blockType = BlockType.OAK_LOG;
        this.selectedBlockTier = 0;
        this.blocksMined = 0;
        this.balance = 0;
        this.autoMiner = new AutoMiner();
        this.engineer = new Engineer();
        this.town = new Town();
        this.quest = 0;
        this.blockMilestones = new HashMap<>();
        for(BlockType blockType : BlockType.values()){
            blockMilestones.put(blockType, new ResourceData(1, 0, 0));
        }
        this.backpackStorage = new HashMap<>();
        this.forgeItems = new HashMap<>();
    }

    public MinefinityPlayer(UUID uuid, BlockType blockType, int selectedBlockTier, long blocksMined, double balance) {
        this.uuid = uuid;
        this.blockType = blockType;
        this.selectedBlockTier = selectedBlockTier;
        this.balance = balance;
        this.blocksMined = blocksMined;
        this.autoMiner = new AutoMiner();
        this.engineer = new Engineer();
        this.town = new Town();
        this.quest = 0;
        this.blockMilestones = new HashMap<>();
        this.backpackStorage = new HashMap<>();
        this.forgeItems = new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public BlockType getBlockTier() {
        return blockType;
    }

    public void setBlockTier(BlockType blockType) {
        this.blockType = blockType;
    }

    public int getSelectedBlockTier() {
        return selectedBlockTier;
    }

    public void setSelectedBlockTier(int selectedBlockTier) {
        this.selectedBlockTier = selectedBlockTier;
    }

    public long getBlocksMined() {
        return blocksMined;
    }

    public void setBlocksMined(long blocksMined) {
        this.blocksMined = blocksMined;
    }

    public Map<BlockType, ResourceData> getBlockMilestones() {
        return blockMilestones;
    }

    public void setBlockMilestones(Map<BlockType, ResourceData> blockMilestones) {
        this.blockMilestones = blockMilestones;
    }

    public AutoMiner getAutoMiner() {
        return autoMiner;
    }

    public Map<String, Integer> getBackpackStorage() {
        return backpackStorage;
    }

    public void setBackpackStorage(Map<String, Integer> backpackStorage) {
        this.backpackStorage = backpackStorage;
    }

    public Map<Integer, ForgeItem> getForgeItems() {
        return forgeItems;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    public int getSelectedForge() {
        return selectedForge;
    }

    public void setSelectedForge(int selectedForge) {
        this.selectedForge = selectedForge;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public Engineer getEngineer() {
        return engineer;
    }

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
    }

}
