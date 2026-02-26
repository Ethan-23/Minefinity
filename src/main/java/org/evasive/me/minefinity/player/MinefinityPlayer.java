package org.evasive.me.minefinity.player;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.backpack.BackpackStorage;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.forge.service.ForgeMap;
import org.evasive.me.minefinity.miner.AutoMiner;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.smelter.Smelter;
import org.evasive.me.minefinity.town.Town;
import org.evasive.me.minefinity.workshop.Engineer;

import java.util.UUID;

public class MinefinityPlayer {

    private final UUID uuid;
    private final String username;
    private BlockType blockType;
    private int selectedBlockTier;
    private double balance;

    private Town town;
    private BlockMilestone blockMilestones;
    private AutoMiner autoMiner;
    private Engineer engineer;
    private Smelter smelter;
    private BackpackStorage backpackStorage;
    private ForgeMap forgeItems;

    //Not implemented yet
    private int quest;
    //Does not need to save between instance
    private int selectedForge;

    public MinefinityPlayer(UUID uuid) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(uuid).getName();
        this.blockType = BlockType.OAK_LOG;
        this.selectedBlockTier = 0;
        this.balance = 0;
        this.autoMiner = new AutoMiner();
        this.engineer = new Engineer();
        this.smelter = new Smelter();
        this.town = new Town();
        this.quest = 0;
        this.blockMilestones = new BlockMilestone();
        this.backpackStorage = new BackpackStorage();
        this.forgeItems = new ForgeMap();
    }

    public MinefinityPlayer(UUID uuid, BlockType blockType, int selectedBlockTier, double balance) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(uuid).getName();
        this.blockType = blockType;
        this.selectedBlockTier = selectedBlockTier;
        this.balance = balance;
        this.autoMiner = new AutoMiner();
        this.engineer = new Engineer();
        this.town = new Town();
        this.smelter = new Smelter();
        this.quest = 0;
        this.blockMilestones = new BlockMilestone();
        this.backpackStorage = new BackpackStorage();
        this.forgeItems = new ForgeMap();
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

    public Smelter getSmelter() {
        return smelter;
    }

    public void setSmelter(Smelter smelter) {
        this.smelter = smelter;
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

    public BlockMilestone getBlockMilestones() {
        return blockMilestones;
    }

    public void setBlockMilestones(BlockMilestone blockMilestones) {
        this.blockMilestones = blockMilestones;
    }

    public AutoMiner getAutoMiner() {
        return autoMiner;
    }

    public BackpackStorage getBackpackStorage() {
        return backpackStorage;
    }

    public void setBackpackStorage(BackpackStorage backpackStorage) {
        this.backpackStorage = backpackStorage;
    }

    public ForgeMap getForgeItems() {
        return this.forgeItems;
    }

    public void setForgeItems(ForgeMap forgeItems) {
        this.forgeItems = forgeItems;
    }

    public void setAutoMiner(AutoMiner autoMiner) {
        this.autoMiner = autoMiner;
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

    public String getUsername() {
        return username;
    }
}
