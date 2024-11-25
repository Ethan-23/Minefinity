package org.evasive.me.minevolutionCore.enchantments.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeStatFunctions;
import org.evasive.me.minevolutionCore.enchantments.enchants.EnchantType;
import org.evasive.me.minevolutionCore.enchantments.enchants.*;
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

        ItemStack pickaxe = player.getInventory().getItemInMainHand();

        if(!new PickaxeStatFunctions().holdingPickaxe(pickaxe.getItemMeta()))
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

        //Mining Speed Calc
        float baseSpeed = pickaxeMeta.getPersistentDataContainer().get(PickaxeKeys.baseSpeedKey, PersistentDataType.FLOAT);
        float miningSpeed = (baseSpeed + ((level * 0.1f) * baseSpeed));

        //Set Item Lore
        List<Component> lore = pickaxeMeta.lore();

        if(enchant.getType() == EnchantType.EFFICIENCY){
            //Update mining speed if efficiency changed
            lore.set(0, ComponentUtils.makeText("Mining Speed: ", NamedTextColor.GRAY, false)
                    .append(makeText("" + Math.round(miningSpeed * 100f)/100f, NamedTextColor.WHITE, true)));
        }

        List<PickaxeEnchantBuilder> order = Arrays.asList(new Critcal(), new Efficiency(), new Alchemist(), new Fortune(), new SuperBreaker(), new Explosive(), new Wisdom(), new Compact(), new OrbitalMiner());

        int enchantIndex = 3;

        for (PickaxeEnchantBuilder tempEnchant : order){
            if(!pickaxeMeta.getPersistentDataContainer().has(tempEnchant.getKey())) {
                continue;
            }
            lore.remove(enchantIndex);
            int tempLevel = pickaxeMeta.getPersistentDataContainer().get(tempEnchant.getKey(), PersistentDataType.INTEGER);
            TextColor enchantColor = NamedTextColor.RED;
            switch (tempEnchant.getRarity()){
                case MINOR -> enchantColor = TextColor.fromHexString("#BFBFBF");
                case UNIQUE -> enchantColor = TextColor.fromHexString("#55FF55");
                case RADIANT -> enchantColor = TextColor.fromHexString("#00FFFF");
                case EXQUISITE -> enchantColor = TextColor.fromHexString("#bc63ff");
                case PRISTINE -> enchantColor = TextColor.fromHexString("#FFAA00");
            }

            TextColor symbolColor = NamedTextColor.WHITE;
            if(tempLevel >= tempEnchant.getMaxLevel()){
                symbolColor = NamedTextColor.RED;
            }

            Component enchantLore = ComponentUtils.makeText(tempEnchant.getSymbol(), symbolColor, false)
                    .append(ComponentUtils.makeText(" " + tempEnchant.getName(), enchantColor, false)
                            .append(ComponentUtils.makeText(" " + RomanNumeralUtil.intToRoman(tempLevel), enchantColor, true)));

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
