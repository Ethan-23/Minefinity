package org.evasive.me.minevolutionCore.customItems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.customItems.ItemFunctions;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeSlot;
import org.evasive.me.minevolutionCore.customItems.pickaxe.functions.PickaxeComponentFunctions;
import org.evasive.me.minevolutionCore.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MPickaxeAdd implements CommandExecutor {

    public MPickaxeAdd(){
        Objects.requireNonNull(MinevolutionCore.getCore().getCommand("mpickaxeadd")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if(!(commandSender instanceof Player player))
            return true;

        if(strings.length != 2)
            return true;

        ItemStack item = player.getInventory().getItemInMainHand();
        if(!ItemFunctions.isPickaxe(item)) return true;

        String partId = strings[1].toUpperCase();

        PickaxeSlot slot = PickaxeSlot.fromString(strings[0]);
        if (slot == null) {
            handleIncorrectComponentName(player);
            return true;
        }

        PickaxeComponent component;
        try {
            component = PickaxeComponent.valueOf(partId);
        } catch (IllegalArgumentException e) {
            handleIncorrectComponentName(player);
            return true;
        }

        if (!component.getBuilder().getItemType().equals(slot.getExpectedType())) {
            handleIncorrectComponentType(player);
            return true;
        }

        PickaxeComponentFunctions.setPickaxePart(player, item, slot.getKey(), partId);
        handleSuccess(player, partId);
        return true;
    }

    private void handleIncorrectComponentName(Player player){
        player.sendMessage(Messages.parse("<red>Invalid Pickaxe Component."));
    }

    private void handleIncorrectComponentType(Player player){
        player.sendMessage(Messages.parse("<red>Incorrect Pickaxe Component Type."));
    }

    private void handleSuccess(Player player, String partId){
        player.sendMessage(Messages.parse("<green>Successfully added " + partId + " to pickaxe"));
    }


}
