package org.evasive.me.minefinity.admin.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.framework.ItemFunctions;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeSlot;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.evasive.me.minefinity.customItems.framework.ItemFunctions.getItemId;

public class MinePickaxeAdd implements CommandExecutor {

    public MinePickaxeAdd(){
        Objects.requireNonNull(Minefinity.getCore().getCommand("minepickaxeadd")).setExecutor(this);
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

        setPickaxePart(player, item, slot.getKey(), partId);
        handleSuccess(player, partId);
        return true;
    }

    private void handleIncorrectComponentName(Player player){
        player.sendMessage(TextConversions.parse("<red>Invalid Pickaxe Component."));
    }

    private void handleIncorrectComponentType(Player player){
        player.sendMessage(TextConversions.parse("<red>Incorrect Pickaxe Component Type."));
    }

    private void handleSuccess(Player player, String partId){
        player.sendMessage(TextConversions.parse("<green>Successfully added " + partId + " to pickaxe"));
    }

    private void setPickaxePart(Player player, ItemStack itemStack, NamespacedKey namespacedKey, String partId){
        BasePickaxeItem pickaxe = PickaxeItem.valueOf(getItemId(itemStack)).getBuilder();
        //ItemStack updatedPickaxe = pickaxe.addPart(itemStack, namespacedKey, partId);
        //player.getInventory().setItemInMainHand(updatedPickaxe);
    }


}
