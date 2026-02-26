package org.evasive.me.minefinity.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.AutosaveService;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.smelter.service.SmelterService;

import java.sql.SQLException;
import java.util.*;

public class RepeatingTick {

    AutoMinerService autoMinerService = Minefinity.getCore().getAutoMinerService();
    SmelterService smelterService = Minefinity.getCore().getSmelterService();
    AutosaveService autosaveService = Minefinity.getCore().getAutosaveService();
    private final long REFRESH = 20L;

    public void startAutomation(){
        automationTimer();
    }

    private void automationTimer(){
        long DELAY = 20L;
        Bukkit.getScheduler().runTaskTimer(Minefinity.getCore(), () -> {
            Collection<? extends Player> onlinePlayerList = Bukkit.getOnlinePlayers();
            for (Player player : onlinePlayerList) {
                autoMinerService.attemptAutomine(player, (int) (REFRESH/20));
                smelterService.attemptSmelt(player, (int) (REFRESH/20));
            }
            attemptAutosave();
        }, DELAY, REFRESH);
    }

    private void attemptAutosave(){
        try {
            autosaveService.attemptSave();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
