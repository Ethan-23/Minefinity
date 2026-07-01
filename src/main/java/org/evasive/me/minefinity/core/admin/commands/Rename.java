package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.lib.item.ItemBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Rename implements CommandExecutor {

    public Rename() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("rename")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))return true;

        if(args.length < 1) return true;

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR) return true;

        String name = String.join(" ", args);

        ItemBuilder itemBuilder = new ItemBuilder(item).setDisplayName(name);

        player.getInventory().setItemInMainHand(itemBuilder.build());

        return true;
    }
}
