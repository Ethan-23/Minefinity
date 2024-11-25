package org.evasive.me.minevolutionCore.customItems;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeStatFunctions;
import org.jetbrains.annotations.NotNull;

public class TestCommands implements CommandExecutor {

    public TestCommands(){
        MinevolutionCore.getCore().getCommand("tierup").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        PickaxeStatFunctions pickaxeStatFunctions = new PickaxeStatFunctions();

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if(!pickaxeStatFunctions.holdingPickaxe(heldItem.getItemMeta())){
            player.sendMessage("Player not holding a pickaxe");
            return true;
        }

        if(pickaxeStatFunctions.canGainProgress(heldItem.getItemMeta())){
            player.sendMessage("Pickaxe is not ready to tier up");
            return true;
        }

        heldItem = pickaxeStatFunctions.tierUp(heldItem);
        player.getInventory().setItemInMainHand(heldItem);
        player.sendMessage("Pickaxe has tiered up");
        return true;
    }
}
