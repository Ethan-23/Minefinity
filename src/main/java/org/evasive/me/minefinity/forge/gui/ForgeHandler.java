package org.evasive.me.minefinity.forge.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.forge.data.ForgeCategories;
import org.evasive.me.minefinity.forge.recipes.ForgeRecipes;
import org.evasive.me.minefinity.player.sevices.ForgeService;
import org.evasive.me.minefinity.utils.InventoryItemPurchase;
import org.evasive.me.minefinity.utils.TextConversions;

import java.time.Instant;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;

public class ForgeHandler {

    ForgeService forgeService = Minefinity.getCore().getForgeService();
    InventoryItemPurchase inventoryItemPurchase = new InventoryItemPurchase();

    public void startForgeAttempt(Player player, ItemStack result){
        boolean canCraft = inventoryItemPurchase.tryPurchaseItem(player, ForgeRecipes.valueOf(getItemId(result)).getCrafting());
        if(!canCraft) player.sendMessage(TextConversions.parse("<red>You do not have the correct amount of resources."));
    }


    public void handleMainForge(Player player, int slot){

        if(!ForgeGui.FORGE_SLOTS.contains(slot)) return;

        int selectedSlot = ForgeGui.FORGE_SLOTS.indexOf(slot) + 1;

        if(!forgeService.hasForgeItem(player, selectedSlot)) {
            forgeService.setSelectedForge(player, selectedSlot);
            new ForgeCategoriesGui(player, ForgeCategories.MATERIALS).open();
        }
        if(forgeService.hasForgeItem(player, selectedSlot) && forgeService.getForgeFinishTime(player, selectedSlot) < Instant.now().toEpochMilli()){
            player.getInventory().addItem(forgeService.getForgeItemStack(player, selectedSlot));
            forgeService.removeForgeItem(player, selectedSlot);
            new ForgeGui(player).open();
        }
    }



}
