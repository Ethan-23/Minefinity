package org.evasive.me.minefinity.workshop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.player.sevices.EngineerService;
import org.evasive.me.minefinity.utils.InventoryItemPurchase;
import org.evasive.me.minefinity.utils.TextConversions;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.ItemFunctions.hasItemId;

public class EngineerHandler {

    InventoryItemPurchase inventoryItemPurchase = new InventoryItemPurchase();
    EngineerService engineerService = Minefinity.getCore().getEngineerService();

    public boolean handleResourceSlot(Player player, ItemStack cursorItem, WorkshopMode workshopMode) {

        if(!cursorItem.isEmpty() && !isResourceItem(cursorItem)) {
            return false;
        }

        if(cursorItem.isEmpty()){
            engineerService.setWorkshopCurrentResource(player, workshopMode, null);
            return true;
        }

        engineerService.setWorkshopCurrentResource(player, workshopMode, WorkshopToolsTiers.valueOf(getItemId(cursorItem)));
        engineerService.setWorkshopCurrentResourceCount(player, workshopMode, cursorItem.getAmount());
        return true;

    }

    //Change where this code is because all item shops will use this code in the future

    public boolean handlePurchase(Player player, BaseWorkshopRecipe workshopRecipe){

        boolean canPurchase = inventoryItemPurchase.tryPurchaseItem(player, workshopRecipe);

        if(!canPurchase)player.sendMessage(TextConversions.parse("<red>You do not have the correct amount of resources."));
        return canPurchase;
    }

    public boolean isResourceItem(ItemStack item){
        if(!hasItemId(item)) return false;
        return WorkshopToolsTiers.contains(getItemId(item));
    }



}
