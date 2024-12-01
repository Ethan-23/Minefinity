package org.evasive.me.minevolutionCore.forge;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.player.PlayerManager;

import java.util.HashMap;
import java.util.Map;

public class ForgeManager {
    PlayerManager playerManager = MinevolutionCore.getPlayerManager();

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
        return getForgeItem(player, slot).itemStack;
    }

    public void setSelectedForge(Player player, int forgeSlot){
        playerManager.getPlayerData(player).setSelectedForge(forgeSlot);
    }

    public int getSelectedForge(Player player){
        return playerManager.getPlayerData(player).getSelectedForge();
    }

}
