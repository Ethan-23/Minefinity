package org.evasive.me.minefinity.core.economy;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

public class EconomyService {

    private final PlayerDataService playerDataService;

    public EconomyService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    private PlayerData getPlayerData(Player player) {
        return playerDataService.getPlayerData(player.getUniqueId());
    }

    public boolean hasBalance(Player player){
        return getPlayerData(player) != null;
    }

    public double getBalance(Player player) {
        return getPlayerData(player).getBalance();
    }

    public void setBalance(Player player, double amount) {
        getPlayerData(player).setBalance(amount);
    }

    public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public void subBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }
}
