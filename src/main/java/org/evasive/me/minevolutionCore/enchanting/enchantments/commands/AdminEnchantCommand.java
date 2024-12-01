package org.evasive.me.minevolutionCore.enchanting.enchantments.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.jetbrains.annotations.NotNull;

public class AdminEnchantCommand implements CommandExecutor {

    final EnchantFunctions enchantFunctions = new EnchantFunctions();

    public AdminEnchantCommand(){
        MinevolutionCore.getCore().getCommand("minechant").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage("This command must be used by a player");
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
            case "critical":
                enchantFunctions.addEnchant(player, EnchantType.CRITICAL, amount);
                break;
            case "efficiency":
                enchantFunctions.addEnchant(player, EnchantType.EFFICIENCY, amount);
                break;
            case "fortune":
                enchantFunctions.addEnchant(player, EnchantType.FORTUNE, amount);
                break;
            case "alchemist":
                enchantFunctions.addEnchant(player, EnchantType.ALCHEMIST, amount);
                break;
            case "compact":
                enchantFunctions.addEnchant(player, EnchantType.COMPACT, amount);
                break;
            case "wisdom":
                enchantFunctions.addEnchant(player, EnchantType.WISDOM, amount);
                break;
            case "explosive":
                enchantFunctions.addEnchant(player, EnchantType.EXPLOSIVE, amount);
                break;
            case "superbreaker":
                enchantFunctions.addEnchant(player, EnchantType.SUPERBREAKER, amount);
                break;
            case "orbitalminer":
                enchantFunctions.addEnchant(player, EnchantType.ORBITALMINER, amount);
                break;
        }
    }
}
