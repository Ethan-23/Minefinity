package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.player.PlayerManager;

public class EconomyService {

    private final PlayerManager playerManager;

    public EconomyService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public boolean hasBalance(Player player){
        return playerManager.has(player);
    }

    public double getBalance(Player player) {
        return playerManager.get(player).getBalance();
    }

    public void setBalance(Player player, double amount) {
        playerManager.get(player).setBalance(amount);
    }

    public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public void subBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }
}
