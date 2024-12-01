package org.evasive.me.minevolutionCore.player;

import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.forge.ForgeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private int blocksMined;
    private int blockTier;
    private int selectedBlockTier;
    private int quest;
    private int selectedForge;
    private Map<Integer, ForgeItem> forgeItems = new HashMap<>();

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.blocksMined = 0;
        this.blockTier = 0;
        this.selectedBlockTier = 0;
        this.quest = 0;
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

    public void setForgeItems(Map<Integer, ForgeItem> forgeItems) {
        this.forgeItems = forgeItems;
    }

    public int getSelectedForge() {
        return selectedForge;
    }

    public void setSelectedForge(int selectedForge) {
        this.selectedForge = selectedForge;
    }
}
