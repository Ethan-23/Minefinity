package org.evasive.me.minevolutionCore.forge.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeRecipes;
import org.evasive.me.minevolutionCore.forge.ForgeManager;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class ForgeGUICategories {

    final PlayerManager playerManager = MinevolutionCore.getPlayerManager();
    ForgeManager forgeManager = new ForgeManager();
    Inventory inventory;

    public void createCustomInventory(Player player, int category){
        inventory = Bukkit.createInventory(null, 54, Component.text("Forge Categories"));

        ItemStack grayPane = createNamedItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(""));
        ItemStack redPane = createNamedItem(Material.RED_STAINED_GLASS_PANE, ComponentUtils.makeText("LOCKED", NamedTextColor.RED, Boolean.TRUE));
        ItemStack greenPane = createNamedItem(Material.LIME_STAINED_GLASS_PANE, ComponentUtils.makeText("EMPTY", NamedTextColor.YELLOW, Boolean.TRUE));
        ItemStack orangePane = createNamedItem(Material.ORANGE_STAINED_GLASS_PANE, ComponentUtils.makeText("???", NamedTextColor.GOLD, Boolean.FALSE));
        ItemStack yellowPane = createNamedItem(Material.YELLOW_STAINED_GLASS_PANE, ComponentUtils.makeText("DONE", NamedTextColor.GREEN, Boolean.TRUE));
        ItemStack materialCategory = createNamedItem(Material.COBBLESTONE, ComponentUtils.makeText("Materials", NamedTextColor.YELLOW, false));
        ItemStack componentCategory = createNamedItem(Material.NETHER_STAR, ComponentUtils.makeText("Components", NamedTextColor.YELLOW, false));
        ItemStack pickaxeCategory = createNamedItem(Material.WOODEN_PICKAXE, ComponentUtils.makeText("Pickaxes", NamedTextColor.YELLOW, false));
        ItemStack storageCategory = createNamedItem(Material.PLAYER_HEAD, ComponentUtils.makeText("Storage", NamedTextColor.YELLOW, false));
        ItemStack backPage = createNamedItem(Material.ARROW, ComponentUtils.makeText("Back", NamedTextColor.WHITE, false));
        ItemStack exit = createNamedItem(Material.BARRIER, ComponentUtils.makeText("Close Menu", NamedTextColor.RED, Boolean.TRUE));

        switch (category){
            case 1 -> materialCategory = makeSelected(materialCategory);
            case 2 -> componentCategory = makeSelected(componentCategory);
            case 3 -> pickaxeCategory = makeSelected(pickaxeCategory);
            case 4 -> storageCategory = makeSelected(storageCategory);
        }

        for (int i = 0; i < 54; i++) {
            switch (i){
                case 37 -> inventory.setItem(i, materialCategory);
                case 38 -> inventory.setItem(i, componentCategory);
                case 39 -> inventory.setItem(i, pickaxeCategory);
                case 40 -> inventory.setItem(i, storageCategory);
                case 41, 42, 43 -> inventory.setItem(i, orangePane);
                case 45 -> inventory.setItem(i, backPage);
                case 49 -> inventory.setItem(i, exit);
                default -> inventory.setItem(i, grayPane);
            }
        }

        for(int i = 10; i < 35; i++){
            if(i % 9 == 0 || i%9 == 8)
                continue;
            inventory.setItem(i, orangePane);
        }

        int start = 10;
        for(ForgeRecipes forgeRecipes : ForgeRecipes.values()){
            if(forgeRecipes.getForgeItemBuilder().getForgeCategory().ordinal() + 1 == category){
                ItemStack displayItem = forgeRecipes.getItemBuilder().getItem().clone();
                ItemMeta displayMeta = displayItem.getItemMeta();
                List<Component> lore = new ArrayList<>();
                for(ItemStack item : forgeRecipes.getForgeItemBuilder().getRecipe()) {
                    //Make look nice later
                    ItemStack temp = item.clone();
                    temp.setAmount(1);
                    if(item.hasItemMeta())
                        lore.add(ComponentUtils.makeText(item.getAmount() + " ", NamedTextColor.WHITE, false).append(item.getItemMeta().displayName()));
                    else
                        lore.add(ComponentUtils.makeText(item.getAmount() + item.getType().name().toLowerCase().replace('_', ' ') + ": " + item.getAmount(), NamedTextColor.WHITE, false));
                    //REMOVE ELSE ONCE YOU MAKE THINGY
                }
                displayMeta.lore(lore);
                displayItem.setItemMeta(displayMeta);
                inventory.setItem(start, displayItem);
                start++;
            }
        }


    }

    public ItemStack createNamedItem(Material material, Component name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack makeSelected(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.makeText("SELECTED", NamedTextColor.GREEN, Boolean.TRUE));
        meta.lore(lore);
        itemStack.setItemMeta(meta);
        return  itemStack;
    }

    public void openInventory(Player player, int category){
        createCustomInventory(player, category);
        player.openInventory(inventory);
    }

}
