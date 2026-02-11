package org.evasive.me.minevolutionCore.forge.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;

import static org.evasive.me.minevolutionCore.MinevolutionCore.playerManager;

public class ForgeManager {

    public boolean hasForgeItem(Player player, int slot){
        return playerManager.getPlayerData(player).getForgeItems().containsKey(slot);
    }

    public void addForgeItem(Player player, int slot, ForgeItem forgeItem){
        playerManager.getPlayerData(player).getForgeItems().put(slot, forgeItem);
    }

    public void removeForgeItem(Player player, int slot){
        playerManager.getPlayerData(player).getForgeItems().remove(slot);
    }

    public ForgeItem getForgeItem(Player player, int slot){
        return playerManager.getPlayerData(player).getForgeItems().get(slot);
    }

    public long getForgeFinishTime(Player player, int slot){
        return getForgeItem(player, slot).timeFinished;
    }

    public ItemStack getForgeItemStack(Player player, int slot){
        return getForgeItem(player, slot).itemStack.clone();
    }

    public void setSelectedForge(Player player, int forgeSlot){
        playerManager.getPlayerData(player).setSelectedForge(forgeSlot);
    }

    public int getSelectedForge(Player player){
        return playerManager.getPlayerData(player).getSelectedForge();
    }

    public long getMilisecondsRemaining(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli();
    }

    public boolean isFinished(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli() <= 0;
    }

}
