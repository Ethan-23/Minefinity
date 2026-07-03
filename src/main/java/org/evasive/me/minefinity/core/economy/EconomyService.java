package org.evasive.me.minefinity.core.economy;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

public class EconomyService {

    private final PlayerDataService playerDataService;

    public EconomyService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    public boolean hasBalance(Player player){
        return playerDataService.getPlayerData(player) != null;
    }

    public double getBalance(Player player) {
        return playerDataService.getPlayerData(player).getBalance();
    }

    public void setBalance(Player player, double amount) {
        playerDataService.getPlayerData(player).setBalance(amount);
    }

    public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public void subBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }
}
