package org.evasive.me.minefinity.admin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.command.CommandFeedback;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StaffMode implements CommandExecutor {

    Map<UUID, ItemStack[]> staffInventory = new HashMap<>();

    public StaffMode() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("staffmode")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        if(!staffInventory.containsKey(player.getUniqueId())){
            player.sendMessage(CommandFeedback.STAFFMODE_ON);
            staffInventory.put(player.getUniqueId(), player.getInventory().getContents());
            player.getInventory().clear();
            player.getInventory().setItem(8, new ItemBuilder(Material.BLAZE_ROD, "<gold>Npc Stick").build());
            player.getInventory().setItem(0, new ItemBuilder(Material.BREEZE_ROD, "<blue>Freeze Stick").build());
        }else {
            player.sendMessage(CommandFeedback.STAFFMODE_OFF);
            player.getInventory().setContents(staffInventory.get(player.getUniqueId()));
            player.updateInventory();
            staffInventory.remove(player.getUniqueId());
        }
        return true;
    }

}
