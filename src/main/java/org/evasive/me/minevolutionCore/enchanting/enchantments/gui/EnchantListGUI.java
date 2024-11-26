package org.evasive.me.minevolutionCore.enchanting.enchantments.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.evasive.me.minevolutionCore.enchanting.enchantments.enchants.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.enchantments.rarity.Rarity;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.Arrays;

public class EnchantListGUI {

    Inventory inventory;

    public void createCustomInventory(Player player) {
        // Create a 27-slot inventory (a half chest)
        inventory = Bukkit.createInventory(null, 54, ComponentUtils.makeText("Enchant List", NamedTextColor.GRAY, false));

        // Create the grey glass pane item
        ItemStack greyPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = greyPane.getItemMeta();
        paneMeta.displayName(Component.text(""));
        greyPane.setItemMeta(paneMeta);

        for(int i = 0; i < 9; i++){
            inventory.setItem(i, greyPane);
        }

        for(int i = 9; i <45 ; i+= 9){
            inventory.setItem(i, greyPane);
        }

        for(int i = 17; i <53 ; i+= 9){
            inventory.setItem(i, greyPane);
        }

        for(int i = 45; i < 54; i++){
            inventory.setItem(i, greyPane);
        }

        // Fill the inventory with grey glass panes
        int index = 10;
        for (EnchantType enchantType : EnchantType.values()){
            while(inventory.getItem(index) != null && inventory.getItem(index).getType() != Material.AIR)
                index++;
            inventory.setItem(index, createEnchantItem(enchantType));
            index++;
        }
    }

    public ItemStack createEnchantItem(EnchantType enchantType){
        ItemStack minor = new ItemStack(Material.GRAY_DYE);
        ItemStack unique = new ItemStack(Material.LIME_DYE);
        ItemStack radiant = new ItemStack(Material.CYAN_DYE);
        ItemStack exquisite = new ItemStack(Material.PURPLE_DYE);
        ItemStack pristine = new ItemStack(Material.ORANGE_DYE);

        String name = enchantType.getPickaxeEnchantBuilder().getName();
        Rarity rarity = enchantType.getPickaxeEnchantBuilder().getRarity();
        String description = enchantType.getPickaxeEnchantBuilder().getDescription();
        int max = enchantType.getPickaxeEnchantBuilder().getMaxLevel();
        String symbol = enchantType.getPickaxeEnchantBuilder().getSymbol();
        ItemStack itemStack = new ItemStack(Material.AIR);
        TextColor enchantColor = TextColor.fromHexString("#FFFFFF");
        itemStack = switch (rarity) {
            case MINOR -> {
                enchantColor = TextColor.fromHexString("#BFBFBF");
                yield minor;
            }
            case UNIQUE -> {
                enchantColor = TextColor.fromHexString("#55FF55");
                yield unique;
            }
            case RADIANT -> {
                enchantColor = TextColor.fromHexString("#00FFFF");
                yield radiant;
            }
            case EXQUISITE -> {
                enchantColor = TextColor.fromHexString("#bc63ff");
                yield exquisite;
            }
            case PRISTINE -> {
                enchantColor = TextColor.fromHexString("#FFAA00");
                yield pristine;
            }
            default -> itemStack;
        };

        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(ComponentUtils.makeText(symbol + " " + name, enchantColor, false));
        meta.lore(Arrays.asList(ComponentUtils.makeText(description, NamedTextColor.WHITE, false), ComponentUtils.makeText("Max Level: " + max, NamedTextColor.GRAY, false)));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void openInventory(Player player){
        createCustomInventory(player);
        player.openInventory(inventory);
    }

}
