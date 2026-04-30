package org.evasive.me.minefinity.towns.structures;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterService;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;

import java.util.Collection;

public class RepeatingTick {

    private final AutoMinerService autoMinerService;
    private final SmelterService smelterService;
    private final long REFRESH = 20L;

    public RepeatingTick(AutoMinerService autoMinerService, SmelterService smelterService) {
        this.autoMinerService = autoMinerService;
        this.smelterService = smelterService;
    }

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
        }, DELAY, REFRESH);
    }
}
