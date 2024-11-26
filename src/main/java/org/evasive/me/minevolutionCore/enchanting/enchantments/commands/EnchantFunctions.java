package org.evasive.me.minevolutionCore.enchanting.enchantments.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeItems;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeStatFunctions;
import org.evasive.me.minevolutionCore.enchanting.enchantments.enchants.*;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

public class EnchantFunctions {

    public void addEnchant(Player player, EnchantType enchantType, int amount){
        //Combine all add enchants need to be able to use each enchant type in 1 function

        PickaxeEnchantBuilder enchant = enchantType.getPickaxeEnchantBuilder();

        ItemStack pickaxe = player.getInventory().getItemInMainHand();

        if(new PickaxeStatFunctions().notHoldingPickaxe(pickaxe.getItemMeta()))
            return;

        int level = pickaxe.getPersistentDataContainer().has(enchant.getKey()) ? pickaxe.getPersistentDataContainer().get(enchant.getKey(), PersistentDataType.INTEGER) : 0; // Set level to the found efficiency level if needed otherwise keep at 0

        if(level >= enchant.getMaxLevel()){
            player.sendMessage("This enchant is already max level");
            return;
        }

        if(level + amount > enchant.getMaxLevel()){
            player.sendMessage("This amount will make the enchant level exceed the maximum");
            return;
        }

        //Increase level by 1
        level += amount;

        //Get item meta
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        pickaxeMeta.getPersistentDataContainer().set(enchant.getKey(), PersistentDataType.INTEGER, level);

        String id = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.itemID, PersistentDataType.STRING);
        int tier = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.tierKey, PersistentDataType.INTEGER);
        player.getInventory().setItemInMainHand(PickaxeItems.valueOf(id).getPickaxeBuilder().buildItem(tier, 0, pickaxeMeta));
    }

    public ItemStack addEnchant(ItemStack itemStack, EnchantType enchantType, int amount){
        //Combine all add enchants need to be able to use each enchant type in 1 function

        if(new PickaxeStatFunctions().notHoldingPickaxe(itemStack.getItemMeta()))
            return itemStack;

        PickaxeEnchantBuilder enchant = enchantType.getPickaxeEnchantBuilder();

        int level = itemStack.getPersistentDataContainer().has(enchant.getKey()) ? itemStack.getPersistentDataContainer().get(enchant.getKey(), PersistentDataType.INTEGER) : 0; // Set level to the found efficiency level if needed otherwise keep at 0

        if(level >= enchant.getMaxLevel()){
            return itemStack;
        }

        if(level + amount > enchant.getMaxLevel()){
            return itemStack;
        }

        //Increase level by 1
        level += amount;

        //Get item meta
        ItemMeta pickaxeMeta = itemStack.getItemMeta();
        pickaxeMeta.getPersistentDataContainer().set(enchant.getKey(), PersistentDataType.INTEGER, level);

        //Set Item Lore
        String id = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.itemID, PersistentDataType.STRING);
        int tier = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.tierKey, PersistentDataType.INTEGER);
        return PickaxeItems.valueOf(id).getPickaxeBuilder().buildItem(tier, 0, pickaxeMeta);
    }
}
