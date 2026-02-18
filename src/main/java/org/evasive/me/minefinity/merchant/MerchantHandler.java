package org.evasive.me.minefinity.merchant;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.player.sevices.EconomyService;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getItemId;
import static org.evasive.me.minefinity.customItems.ItemFunctions.hasItemId;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;

public class MerchantHandler {

    EconomyService economyService = Minefinity.core.getEconomyService();

    public void handleInventorySell(Player player){
        Inventory inventory = player.getInventory();

        Map<String, Integer> salesMap = new HashMap<>();

        for(ItemStack item : inventory.getContents()){
            if(!hasItemId(item)) continue;

            String itemId = getItemId(item);
            if(!ResourceItem.contains(itemId)) continue;

            int stackSize = item.getAmount();

            BaseCustomItem baseCustomItem = CustomItemRegistry.getByID(itemId).getBuilder();
            float singleCost = baseCustomItem.getBuilder().getValue();
            if(singleCost <= 0) continue;

            if(salesMap.containsKey(itemId))
                salesMap.put(itemId, salesMap.get(itemId) + stackSize);
            else
                salesMap.put(itemId, stackSize);
            item.setAmount(0);
        }
        sellMapData(player, salesMap);

    }

    public void handleBackpackSell(Player player){
        Map<String, Integer> playerBackpackMap = Minefinity.getCore().getBackpackService().getBackpackStorage(player);
        sellMapData(player, playerBackpackMap);
        Minefinity.getCore().getBackpackService().clearBackpackStorage(player);
    }

    public void sellMapData(Player player, Map<String, Integer> playerSaleMap){
        float totalCost = 0;
        for (Map.Entry<String, Integer> entry : playerSaleMap.entrySet()) {
            String itemId = entry.getKey();
            BaseCustomItem baseCustomItem = CustomItemRegistry.getByID(itemId).getBuilder();
            float singleCost = baseCustomItem.getBuilder().getValue();
            int stackSize = entry.getValue();
            if(stackSize <= 0 || singleCost <= 0) continue;
            totalCost += stackSize * singleCost;
            player.sendMessage(TextConversions.parse("<gray>Sold " + buildRarityColor(itemId, baseCustomItem.getRarity()) + " <gray>x" + stackSize + " for <green>$" + stackSize * singleCost));
        }
        if(totalCost == 0)
            player.sendMessage(TextConversions.parse("<red>You do not have any items to sell"));
        else
            economyService.addBalance(player, totalCost);
    }

}
