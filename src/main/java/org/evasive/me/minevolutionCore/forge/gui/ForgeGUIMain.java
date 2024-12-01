package org.evasive.me.minevolutionCore.forge.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.forge.ForgeManager;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ForgeGUIMain {

    final PlayerManager playerManager = MinevolutionCore.getPlayerManager();
    ForgeManager forgeManager = new ForgeManager();
    Inventory inventory;

    public void createCustomInventory(Player player){
        inventory = Bukkit.createInventory(null, 54, Component.text("Forge"));

        ItemStack grayPane = glassPane(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack redPane = glassPane(Material.RED_STAINED_GLASS_PANE);
        ItemStack greenPane = glassPane(Material.LIME_STAINED_GLASS_PANE);
        ItemStack orangePane = glassPane(Material.ORANGE_STAINED_GLASS_PANE);
        ItemStack yellowPane = glassPane(Material.YELLOW_STAINED_GLASS_PANE);

        for (int i = 0; i < 54; i++) {
            if (i % 9 == 0 || i % 9 == 1 || i % 9 == 7 || i % 9 == 8) {
                inventory.setItem(i, grayPane);
            }
        }

        for(int count = 1; count < 6; count++){
            //count = slot # so base on forge slot for what pane to use
            for(int i = 1 + count; i < 46 + count; i+= 9){
                if(i == 21 + count)
                    continue;
                if(!forgeManager.hasForgeItem(player, count)){
                    inventory.setItem(i, yellowPane);
                    continue;
                }
                ItemMeta meta = orangePane.getItemMeta();
                List<Component> lore = new ArrayList<>();
                long milliseconds = forgeManager.getForgeFinishTime(player, count) - Instant.now().toEpochMilli();
                long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;

                lore.add(ComponentUtils.makeText(hours + ":" + minutes + ":" + seconds, NamedTextColor.GOLD, false));
                meta.lore(lore);
                orangePane.setItemMeta(meta);
                if(milliseconds > 0)
                    inventory.setItem(i, orangePane);
                else
                    inventory.setItem(i, greenPane);
            }
        }

        for(int i = 47; i < 52; i++){
            inventory.setItem(i, new ItemStack(Material.GRAY_DYE));
        }

        for(int i = 1; i < 6; i++){
            ItemStack item = new ItemStack(Material.FURNACE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(ComponentUtils.makeText("Empty Forge Slot", NamedTextColor.YELLOW, false));
            meta.lore(List.of(ComponentUtils.makeText("Click to select a forge recipe", NamedTextColor.GRAY, false)));
            item.setItemMeta(meta);
            if(forgeManager.hasForgeItem(player, i))
                 item = forgeManager.getForgeItemStack(player, i);
            inventory.setItem(i + 19, item);
        }

    }

    public ItemStack glassPane(Material material){
        ItemStack pane = new ItemStack(material);
        ItemMeta paneMeta = pane.getItemMeta();
        switch (material){
            case GRAY_STAINED_GLASS_PANE -> paneMeta.displayName(Component.text(""));
            case RED_STAINED_GLASS_PANE -> paneMeta.displayName(ComponentUtils.makeText("LOCKED", NamedTextColor.RED, Boolean.TRUE));
            case YELLOW_STAINED_GLASS_PANE -> paneMeta.displayName(ComponentUtils.makeText("EMPTY", NamedTextColor.YELLOW, Boolean.TRUE));
            case ORANGE_STAINED_GLASS_PANE -> paneMeta.displayName(ComponentUtils.makeText("IN PROGRESS", NamedTextColor.GOLD, Boolean.TRUE));
            case LIME_STAINED_GLASS_PANE -> paneMeta.displayName(ComponentUtils.makeText("DONE", NamedTextColor.GREEN, Boolean.TRUE));
        }
        pane.setItemMeta(paneMeta);

        return pane;
    }

    public void openInventory(Player player){
        createCustomInventory(player);
        player.openInventory(inventory);
        createRefresh(player);
    }

    public void createRefresh(Player player){
        Bukkit.getScheduler().runTaskLater(MinevolutionCore.getCore(), () -> {
            if(player.getOpenInventory().title().equals(Component.text("Forge")))
                openInventory(player);
        }, 20).getTaskId();
    }

}
