package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.forge.data.ForgeItem;
import org.evasive.me.minefinity.player.PlayerManager;

import java.time.Instant;

public class ForgeService {

    private final PlayerManager playerManager;

    public ForgeService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public boolean hasForgeItem(Player player, int slot){
        return playerManager.get(player).getForgeItems().containsKey(slot);
    }

    public void addForgeItem(Player player, int slot, ForgeItem forgeItem){
        playerManager.get(player).getForgeItems().put(slot, forgeItem);
    }

    public void removeForgeItem(Player player, int slot){
        playerManager.get(player).getForgeItems().remove(slot);
    }

    public ForgeItem getForgeItem(Player player, int slot){
        return playerManager.get(player).getForgeItems().get(slot);
    }

    public long getForgeFinishTime(Player player, int slot){
        return getForgeItem(player, slot).getTimeFinished();
    }

    public ItemStack getForgeItemStack(Player player, int slot){
        return getForgeItem(player, slot).getItemStack().clone();
    }

    public void setSelectedForge(Player player, int forgeSlot){
        playerManager.get(player).setSelectedForge(forgeSlot);
    }

    public int getSelectedForge(Player player){
        return playerManager.get(player).getSelectedForge();
    }

    public long getMilisecondsRemaining(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli();
    }

    public boolean isFinished(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli() <= 0;
    }

}
