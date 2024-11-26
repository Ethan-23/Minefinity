package org.evasive.me.minevolutionCore.enchanting.enchantments.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.enchanting.enchantments.gui.EnchantListGUI;
import org.jetbrains.annotations.NotNull;

public class EnchantListCommand implements CommandExecutor {

    public EnchantListCommand(){
        MinevolutionCore.getCore().getCommand("enchants").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player))
            return true;
        new EnchantListGUI().openInventory(player);


        return true;
    }
}
