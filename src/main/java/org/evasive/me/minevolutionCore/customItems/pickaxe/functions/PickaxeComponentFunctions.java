package org.evasive.me.minevolutionCore.customItems.pickaxe.functions;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minevolutionCore.customItems.pickaxe.PickaxeItem;

import static org.evasive.me.minevolutionCore.customItems.ItemFunctions.getItemId;

public class PickaxeComponentFunctions {

    public static void setPickaxePart(Player player, ItemStack itemStack, NamespacedKey namespacedKey, String partId){
        BasePickaxeItem pickaxe = (BasePickaxeItem) PickaxeItem.valueOf(getItemId(itemStack)).getBuilder();
        ItemStack updatedPickaxe = pickaxe.addPart(itemStack, namespacedKey, partId);
        player.getInventory().setItemInMainHand(updatedPickaxe);
    }
}
