package org.evasive.me.minevolutionCore.automation;

import org.bukkit.Bukkit;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.automation.miner.data.AutoMiner;
import org.evasive.me.minevolutionCore.player.PlayerData;

import java.util.Map;
import java.util.UUID;

public class AutomationTimer {

    public void startAutomation(){
        automationTimer();
    }

    private void automationTimer(){
        //Recode so it works on a global player scale offline capped at offline timer
        Map<UUID, PlayerData> playerDataMap = MinevolutionCore.playerManager.getAllPlayersData();
        Bukkit.getScheduler().runTaskTimer(MinevolutionCore.getCore(), () -> {

            if(playerDataMap.isEmpty()) return;

            for (PlayerData playerData : playerDataMap.values()) {
                AutoMiner autoMiner = playerData.getAutoMiner();
                if(autoMiner.getBlockType() == null || autoMiner.getPickaxe() == null || autoMiner.isFull()) return;
                autoMiner.addProgress();
            }
        }, 20L, 20L);
    }

}
