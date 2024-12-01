package org.evasive.me.minevolutionCore.forge.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.forge.recipes.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.forge.ForgeManager;
import org.evasive.me.minevolutionCore.player.PlayerManager;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.List;

public class ForgeGUIConfirmation {
    final PlayerManager playerManager = MinevolutionCore.getPlayerManager();
    ForgeManager forgeManager = new ForgeManager();
    Inventory inventory;

    public void createCustomInventory(Player player, ForgeItemBuilder forgeItemBuilder){
        inventory = Bukkit.createInventory(null, 54, Component.text("Forge Confirmation"));

        ItemStack grayPane = createNamedItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(""));
        ItemStack redPane = createNamedItem(Material.RED_STAINED_GLASS_PANE, ComponentUtils.makeText("LOCKED", NamedTextColor.RED, Boolean.TRUE));
        ItemStack greenPane = createNamedItem(Material.LIME_STAINED_GLASS_PANE, ComponentUtils.makeText("EMPTY", NamedTextColor.YELLOW, Boolean.TRUE));
        ItemStack orangePane = createNamedItem(Material.ORANGE_STAINED_GLASS_PANE, ComponentUtils.makeText("", NamedTextColor.GOLD, Boolean.FALSE));
        ItemStack yellowPane = createNamedItem(Material.YELLOW_STAINED_GLASS_PANE, ComponentUtils.makeText("DONE", NamedTextColor.GREEN, Boolean.TRUE));
        ItemStack backPage = createNamedItem(Material.ARROW, ComponentUtils.makeText("Back", NamedTextColor.WHITE, false));
        ItemStack exit = createNamedItem(Material.BARRIER, ComponentUtils.makeText("Close Menu", NamedTextColor.RED, Boolean.TRUE));
        ItemStack start = createNamedItem(Material.FURNACE, ComponentUtils.makeText("Start", NamedTextColor.GREEN, true));

        for (int i = 0; i < 54; i++) {
            switch (i){
                case 16 -> inventory.setItem(i, forgeItemBuilder.getItem().clone());
                case 22 -> inventory.setItem(i, start);
                case 45 -> inventory.setItem(i, backPage);
                case 49 -> inventory.setItem(i, exit);
                default -> inventory.setItem(i, grayPane);
            }
        }

        List<Integer> recipeSlots = List.of(10, 11, 19, 20, 28, 29);
        int index = 0;
        for(int i = 0; i < recipeSlots.size(); i++){
            if(index >= forgeItemBuilder.getRecipe().size()){
                inventory.setItem(recipeSlots.get(i), orangePane);
                index++;
                continue;
            }
            if(forgeItemBuilder.getRecipe().get(index).getAmount() <= 64) {
                inventory.setItem(recipeSlots.get(i), forgeItemBuilder.getRecipe().get(index));
                index++;
                continue;
            }
            for(int stack = 0; stack < forgeItemBuilder.getRecipe().get(index).getAmount()/64; stack++){
                inventory.setItem(recipeSlots.get(i), forgeItemBuilder.getRecipe().get(index));
                i++;
            }
            ItemStack remainder = forgeItemBuilder.getRecipe().get(index).clone();
            remainder.setAmount(forgeItemBuilder.getRecipe().get(index).getAmount()%64);
            inventory.setItem(recipeSlots.get(i), remainder);
            index++;
        }
    }

    public ItemStack createNamedItem(Material material, Component name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void openInventory(Player player, ForgeItemBuilder forgeItemBuilder){
        createCustomInventory(player, forgeItemBuilder);
        player.openInventory(inventory);
    }

}
