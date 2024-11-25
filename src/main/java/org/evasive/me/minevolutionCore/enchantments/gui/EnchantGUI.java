package org.evasive.me.minevolutionCore.enchantments.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.enchantments.enchants.EnchantType;
import org.evasive.me.minevolutionCore.enchantments.enchants.PickaxeEnchantBuilder;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class EnchantGUI {

    Inventory inventory;

    public void createCustomInventory(Player player) {
        // Create a 27-slot inventory (a half chest)
        inventory = Bukkit.createInventory(null, 27, "Enchanter");

        // Create the grey glass pane item
        ItemStack greyPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = greyPane.getItemMeta();
        paneMeta.displayName(Component.text(""));
        greyPane.setItemMeta(paneMeta);

        ItemStack efficiency = clickableItem(Material.REDSTONE, EnchantType.EFFICIENCY, player);
        ItemStack fortune = clickableItem(Material.LAPIS_LAZULI, EnchantType.FORTUNE, player);
        ItemStack compact = clickableItem(Material.ANVIL, EnchantType.COMPACT, player);
        ItemStack wisdom = clickableItem(Material.BOOK, EnchantType.WISDOM, player);
        ItemStack superbreaker = clickableItem(Material.SUGAR, EnchantType.SUPERBREAKER, player);

        // Fill the inventory with grey glass panes
        for (int i = 0; i < inventory.getSize(); i++) {
            switch (i){
                case 10:
                    inventory.setItem(i, efficiency);
                    break;
                case 11:
                    inventory.setItem(i, fortune);
                    break;
                case 12:
                    inventory.setItem(i, compact);
                    break;
                case 14:
                    inventory.setItem(i, wisdom);
                    break;
                case 15:
                    inventory.setItem(i, superbreaker);
                    break;
                case 16:
                    inventory.setItem(i, new ItemStack(Material.BARRIER));
                    break;
                default:
                    inventory.setItem(i, greyPane);
            }
        }
    }

    public ItemStack clickableItem(Material material, EnchantType enchantType, Player player){
        PickaxeEnchantBuilder enchant = enchantType.getPickaxeEnchantBuilder();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ComponentUtils.makeText(enchant.getName(), NamedTextColor.YELLOW, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.makeText("Max Level: ", NamedTextColor.GRAY, false).append(ComponentUtils.makeText("" + enchant.getMaxLevel(), NamedTextColor.WHITE, true)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(Player player){
        createCustomInventory(player);
        player.openInventory(inventory);
    }
}
