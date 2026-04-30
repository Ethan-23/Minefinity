package org.evasive.me.minefinity.towns.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.customItems.recipes.RecipeUnlockManager;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

public class JoinListener implements Listener {

    PlayerDataService playerDataService;
    RecipeUnlockManager recipeUnlockManager;


    public JoinListener(PlayerDataService playerDataService, RecipeUnlockManager recipeUnlockManager) {
        this.playerDataService = playerDataService;
        this.recipeUnlockManager = recipeUnlockManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        recipeUnlockManager.registerPlayerUnlocks(playerDataService.getPlayerData(event.getPlayer().getUniqueId()));
    }

}
