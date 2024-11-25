package org.evasive.me.minevolutionCore.blocks.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.blocks.block.BlockBuilder;
import org.evasive.me.minevolutionCore.blocks.block.BlockType;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class BlockGUI {

    PlayerManager playerManager = MinevolutionCore.getPlayerManager();
    Inventory blockGui;

    public void createCustomInventory(Player player){
        blockGui = Bukkit.createInventory(null, 36, Component.text("Blocks"));
        int start = 0;
        for (BlockType blockType : BlockType.values()){
            if(start == 0){
                start++;
                continue;
            }
            BlockBuilder block = blockType.getBlockCreator();
            ItemStack temp = new ItemStack(block.getMaterial());
            ItemMeta tempMeta = temp.getItemMeta();
            List<Component> lore = new ArrayList<>();

            //Add drop chances for block drops

            if(playerManager.getBlockTier(player) > start-1){
                tempMeta.displayName(ComponentUtils.makeText("(",NamedTextColor.GRAY,false).append(ComponentUtils.makeText(""+(start), NamedTextColor.WHITE, true).append(ComponentUtils.makeText(")", NamedTextColor.GRAY, false).append(ComponentUtils.makeText(" " + block.getName(), NamedTextColor.WHITE, false)))));
                lore.add(ComponentUtils.makeText("Block Health: ", NamedTextColor.GRAY, false).append(ComponentUtils.makeText("" + block.getHealth(), NamedTextColor.WHITE, true)));
            }else{
                tempMeta.displayName(ComponentUtils.makeText("(",NamedTextColor.GRAY,false).append(ComponentUtils.makeText(""+(start), NamedTextColor.RED, true).append(ComponentUtils.makeText(")", NamedTextColor.GRAY, false).append(ComponentUtils.makeText(" " + block.getName(), NamedTextColor.RED, false)))));
                lore.add(ComponentUtils.makeText("Locked", NamedTextColor.RED, true));
            }

            if(start == playerManager.getSelectedBlockTier(player)){
                lore.add(Component.text(""));
                lore.add(ComponentUtils.makeText("Selected", NamedTextColor.GREEN, true));
                tempMeta.addEnchant(Enchantment.LURE, 1, true);
                tempMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            tempMeta.lore(lore);
            temp.setItemMeta(tempMeta);
            blockGui.setItem(start-1, temp);
            start++;
        }
    }

    public void openInventory(Player player){
        createCustomInventory(player);
        player.openInventory(blockGui);
    }

}
