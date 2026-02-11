package org.evasive.me.minevolutionCore.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.automation.miner.data.AutoMiner;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;
import org.evasive.me.minevolutionCore.resourceblock.milestones.Milestone;
import org.evasive.me.minevolutionCore.forge.data.ForgeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;

    private int quest;

    private int blockTier;
    private int selectedBlockTier;
    private int blocksMined;
    private Map<Material, Milestone> blockMilestones = new HashMap<>();

    private final AutoMiner autoMiner;

    private Map<String, Integer> backpackStorage =  new HashMap<>();

    private int selectedForge;
    private final Map<Integer, ForgeItem> forgeItems = new HashMap<>();

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.blocksMined = 0;
        this.blockTier = 0;
        this.selectedBlockTier = 0;
        this.quest = 0;
        this.autoMiner = new AutoMiner();
        for(BlockType blockType : BlockType.values()){
            blockMilestones.put(blockType.getBlock().material(), new Milestone(0, 0));
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public void setBlocksMined(int blocksMined) {
        this.blocksMined = blocksMined;
    }

    public int getBlockTier() {
        return blockTier;
    }

    public void setBlockTier(int blockTier) {
        this.blockTier = blockTier;
    }

    public int getQuest() {
        return quest;
    }

    public void setQuest(int quest) {
        this.quest = quest;
    }

    public int getSelectedBlockTier() {
        return selectedBlockTier;
    }

    public void setSelectedBlockTier(int selectedBlockTier) {
        this.selectedBlockTier = selectedBlockTier;
    }

    public Map<Integer, ForgeItem> getForgeItems() {
        return forgeItems;
    }

    public int getSelectedForge() {
        return selectedForge;
    }

    public void setSelectedForge(int selectedForge) {
        this.selectedForge = selectedForge;
    }

    //Change to a view only map
    public Map<Material, Milestone> getBlockMilestones() {
        return blockMilestones;
    }

    public void setBlockMilestones(Map<Material, Milestone> blockMilestones) {
        this.blockMilestones = blockMilestones;
    }

    public AutoMiner getAutoMiner() {
        return autoMiner;
    }

    public Map<String, Integer> getBackpackStorage() {
        return backpackStorage;
    }

    public void clearBackpack() {
        backpackStorage = new HashMap<>();
    }
}
