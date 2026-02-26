package org.evasive.me.minefinity.economy;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.MinefinityPlayer;
import org.evasive.me.minefinity.player.PlayerManager;

public class EconomyService {

    private final PlayerManager playerManager;
    DirtyPlayerService dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();

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
        dirtyPlayerService.addDirtyPlayer(player);
        playerManager.get(player).setBalance(amount);
    }

    public void addBalance(Player player, double amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        setBalance(player, getBalance(player) + amount);
    }

    public void subBalance(Player player, double amount) {
        dirtyPlayerService.addDirtyPlayer(player);
        setBalance(player, getBalance(player) - amount);
    }
}
