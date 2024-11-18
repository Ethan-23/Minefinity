package org.evasive.me.minevolutionCore.mining.enchantments.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.mining.enchantments.enchants.EnchantType;
import org.evasive.me.minevolutionCore.mining.enchantments.enchants.*;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;
import org.evasive.me.minevolutionCore.utils.RomanNumeralUtil;

import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;

public class EnchantFunctions {

    public void addEnchant(Player player, EnchantType enchantType, int amount){
        //Combine all add enchants need to be able to use each enchant type in 1 function

        PickaxeEnchantBuilder enchant = enchantType.getPickaxeEnchantBuilder();

        ItemStack pickaxe = player.getInventory().getItem(0);

        int level = pickaxe.getPersistentDataContainer().has(enchant.getKey()) ? pickaxe.getPersistentDataContainer().get(enchant.getKey(), PersistentDataType.INTEGER) : 0; // Set level to the found efficiency level if needed otherwise keep at 0

        if(level >= enchant.getMaxLevel()){
            player.sendMessage("This enchant is already max level");
            return;
        }

        if(player.getExp() < enchant.getExperienceCost(level)){
            player.sendMessage("Not enough experience to purchase this enchant");
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

        //Mining Speed Calc
        int baseSpeed = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.baseSpeedKey, PersistentDataType.INTEGER);
        float miningSpeed = (float) (baseSpeed + ((level * 0.01) * baseSpeed));

        //Set Item Lore
        List<Component> lore = pickaxeMeta.lore();

        if(enchant.getType() == EnchantType.EFFICIENCY){
            //Update mining speed if efficiency changed
            lore.set(0, ComponentUtils.makeText("Mining Speed: ", NamedTextColor.GRAY, false)
                    .append(makeText("" + miningSpeed, NamedTextColor.WHITE, true)));
        }

        List<PickaxeEnchantBuilder> order = Arrays.asList(new Efficiency(), new Fortune(), new Wisdom(), new Compact(), new SuperBreaker());

        int enchantIndex = 3;

        for (PickaxeEnchantBuilder tempEnchant : order){
            if(!pickaxeMeta.getPersistentDataContainer().has(tempEnchant.getKey()))
                continue;
            lore.remove(enchantIndex);
            int tempLevel = pickaxeMeta.getPersistentDataContainer().get(tempEnchant.getKey(), PersistentDataType.INTEGER);

            Component enchantLore = ComponentUtils.makeText(tempEnchant.getSymbol(), NamedTextColor.WHITE, false)
                    .append(ComponentUtils.makeText(" " + tempEnchant.getName(), NamedTextColor.YELLOW, false)
                            .append(ComponentUtils.makeText(" " + RomanNumeralUtil.intToRoman(tempLevel), NamedTextColor.YELLOW, true)));

            lore.add(enchantIndex, enchantLore);
            enchantIndex++;
        }

        if(level - amount == 0)
            lore.add(enchantIndex, Component.text(""));


        pickaxeMeta.lore(lore);
        pickaxe.setItemMeta(pickaxeMeta);
        player.sendMessage(enchant.getName() + " level increased to " + level);

    }
}
