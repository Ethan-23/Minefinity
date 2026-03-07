package org.evasive.me.minefinity.core.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.evasive.me.minefinity.Minefinity;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGui implements InventoryHolder {

    protected final Inventory inventory;
    protected final Player player;

    protected BaseGui(Player player, int size, Component title) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    protected abstract void build();

    public void open() {
        player.openInventory(inventory);
    }

    /* Hooks */
    public void onOpen(InventoryOpenEvent e) {}
    public void onClick(InventoryClickEvent e) {}
    public void onDrag(InventoryDragEvent e) {}
    public void onClose(InventoryCloseEvent e) {}

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public void rebuildInventory(){
        new BukkitRunnable() {
            @Override
            public void run() {
                build();
            }
        }.runTaskLater(Minefinity.getCore(), 1);
    }
}
