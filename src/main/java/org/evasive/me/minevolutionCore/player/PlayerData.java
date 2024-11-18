package org.evasive.me.minevolutionCore.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private int blocksMined;
    private int blockTier;
    private int selectedBlockTier;
    private int quest;

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


}
