package org.evasive.me.minefinity.towns.structures.forge.blacksmith.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.BaseForgeItem;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.data.ForgeItems;
import org.evasive.me.minefinity.towns.structures.forge.blacksmith.recipes.BaseForgeRecipe;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForgeService {

    private final Map<UUID, Integer> selectedForgeMap;
    private final PlayerDataService playerDataService;
    private final CustomItemRegistryService customItemRegistryService;

    public ForgeService(PlayerDataService playerDataService, CustomItemRegistryService customItemRegistryService) {
        this.selectedForgeMap = new HashMap<>();
        this.playerDataService = playerDataService;
        this.customItemRegistryService = customItemRegistryService;
    }

    public Map<Integer, BaseForgeItem> getForgeMap(Player player){
        return playerDataService.getPlayerData(player).get(ForgeItems.class).getItems();
    }

    public boolean hasForgeItem(Player player, int slot){
        return getForgeMap(player).containsKey(slot);
    }

    public void addForgeItem(Player player, int slot, BaseForgeItem forgeItem){
        playerDataService.getPlayerData(player).get(ForgeItems.class).setItem(slot, forgeItem);
    }

    public void removeForgeItem(Player player, int slot){
        playerDataService.getPlayerData(player).get(ForgeItems.class).removeItem(slot);
    }

    public BaseForgeItem getForgeItem(Player player, int slot){
        return getForgeMap(player).get(slot);
    }

    public long getForgeFinishTime(Player player, int slot){
        return getForgeItem(player, slot).getTimeFinished();
    }

    public ItemStack getForgeItemStack(Player player, int slot){
        return customItemRegistryService.getBaseItemById(getForgeItem(player, slot).getResultItemId()).buildItem().clone();
    }

    public void setSelectedForge(Player player, int forgeSlot){
        selectedForgeMap.put(player.getUniqueId(), forgeSlot);
    }

    public int getSelectedForge(Player player){
        return selectedForgeMap.get(player.getUniqueId());
    }

    public long getMilisecondsRemaining(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli();
    }

    public boolean isFinished(Player player, int slot){
        return getForgeFinishTime(player, slot) - Instant.now().toEpochMilli() <= 0;
    }

    public boolean hasRecipeUnlocked(Player player, BaseForgeRecipe baseForgeRecipe){

        for(RecipeRequirement recipeRequirement : baseForgeRecipe.getRequirements()){
            if(!recipeRequirement.isMet(playerDataService.getPlayerData(player)))
                return false;
        }
        return true;
    }

}
