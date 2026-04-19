package org.evasive.me.minefinity.playerdata.stats.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;

public class StatsListeners implements Listener {

    private final StatsService statsService;

    public StatsListeners(StatsService statsService) {
        this.statsService = statsService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    @EventHandler
    public void onItemSwap(PlayerItemHeldEvent e){
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    @EventHandler
    public void onEquipmentSwap(PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent e) {
        int slot = e.getSlot();
        Player player = (Player) e.getWhoClicked();
        int selectedSlot = player.getInventory().getHeldItemSlot();
        if(slot != selectedSlot) return;
        recalculateStats(player);
    }

    @EventHandler
    public void dropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    @EventHandler
    public void pickupEvent(PlayerAttemptPickupItemEvent e) {
        Player player = e.getPlayer();
        recalculateStats(player);
    }

    private void recalculateStats(Player player) {
        Bukkit.getScheduler().runTaskLater(Minefinity.getCore(), () -> {
            statsService.recalculateStats(player);
        }, 1L);

    }

}
