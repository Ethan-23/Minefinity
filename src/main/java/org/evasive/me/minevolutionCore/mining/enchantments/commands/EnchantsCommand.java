package org.evasive.me.minevolutionCore.mining.enchantments.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.enchantments.enchants.*;
import org.evasive.me.minevolutionCore.mining.enchantments.gui.EnchantGUI;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class EnchantsCommand implements CommandExecutor {

    EnchantFunctions enchantFunctions = new EnchantFunctions();

    public EnchantsCommand(){
        MinevolutionCore.getCore().getCommand("minechant").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("This command must be used by a player");
            return true;
        }

        Player player = (Player) commandSender;

        if(strings.length == 0){
            new EnchantGUI().openInventory(player);
            return true;
        }

        if(strings.length == 1){

            applyEnchant(strings[0], player, 1);

        }else if(strings.length == 2){

            applyEnchant(strings[0], player, Integer.parseInt(strings[1]));

        }
        return true;
    }

    public void applyEnchant(String input, Player player, int amount){
        switch (input.toLowerCase()){
            case "efficiency":
                enchantFunctions.addEnchant(player, EnchantType.EFFICIENCY, amount);
                break;
            case "fortune":
                enchantFunctions.addEnchant(player, EnchantType.FORTUNE, amount);
                break;
            case "compact":
                enchantFunctions.addEnchant(player, EnchantType.COMPACT, amount);
                break;
            case "wisdom":
                enchantFunctions.addEnchant(player, EnchantType.WISDOM, amount);
                break;
            case "superbreaker":
                enchantFunctions.addEnchant(player, EnchantType.SUPERBREAKER, amount);
                break;
        }
    }
}
