package org.evasive.me.minefinity.forge.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.framework.CustomItemRegistry;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.forge.data.BaseForgeItem;
import org.evasive.me.minefinity.player.PlayerManager;

import java.time.Instant;

public class ForgeService {

    private final PlayerManager playerManager;
    private final DirtyPlayerService dirtyPlayerService;

    public ForgeService(PlayerManager playerManager) {
        this.playerManager = playerManager;
        dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();
    }

    public ForgeMap getForgeMap(Player player){
        return playerManager.get(player).getForgeItems();
    }

    public boolean hasForgeItem(Player player, int slot){
        return getForgeMap(player).getForgeItemsMap().containsKey(slot);
    }

    public void addForgeItem(Player player, int slot, BaseForgeItem forgeItem){
        dirtyPlayerService.addDirtyPlayer(player);
        getForgeMap(player).setForgeItem(slot, forgeItem);
    }

    public void removeForgeItem(Player player, int slot){
        dirtyPlayerService.addDirtyPlayer(player);
        getForgeMap(player).clearForgeSlot(slot);
    }

    public BaseForgeItem getForgeItem(Player player, int slot){
        return getForgeMap(player).getForgeItem(slot);
    }

    public long getForgeFinishTime(Player player, int slot){
        return getForgeItem(player, slot).getTimeFinished();
    }

    public ItemStack getForgeItemStack(Player player, int slot){
        return CustomItemRegistry.getByID(getForgeItem(player, slot).getResultItemId()).getBuilder().buildItem().clone();
    }

    public void setSelectedForge(Player player, int forgeSlot){
        dirtyPlayerService.addDirtyPlayer(player);
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
