package org.evasive.me.minefinity.towns.structures.shops.merchant.service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ValueComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;

public class MerchantHandler {

    private final EconomyService economyService;
    private final CustomItemRegistryService customItemRegistryService;
    private final BackpackService backpackService;

    public MerchantHandler(EconomyService economyService, CustomItemRegistryService customItemRegistryService, BackpackService backpackService) {
        this.economyService = economyService;
        this.customItemRegistryService = customItemRegistryService;
        this.backpackService = backpackService;
    }

    public void handleInventorySell(Player player){
        Inventory inventory = player.getInventory();

        Map<String, Integer> salesMap = new HashMap<>();

        for(ItemStack item : inventory.getContents()){

            BaseCustomItem baseCustomItem = customItemRegistryService.getRegisteredBaseItem(item);

            if(baseCustomItem == null)
                continue;

            int stackSize = item.getAmount();
            Float value = baseCustomItem.getComponent(ValueComponent.class).getValue();
            float singleCost = value != null ? value : 0;

            if(singleCost <= 0) continue;

            String itemId = baseCustomItem.getID();

            if(salesMap.containsKey(itemId))
                salesMap.put(itemId, salesMap.get(itemId) + stackSize);
            else
                salesMap.put(itemId, stackSize);
            item.setAmount(0);
        }
        sellMapData(player, salesMap);

    }

    public void handleBackpackSell(Player player){
        Map<String, Integer> playerBackpackMap = backpackService.getBackpackStorage(player);
        sellMapData(player, playerBackpackMap);
        backpackService.clearBackpackStorage(player);
    }

    public void sellMapData(Player player, Map<String, Integer> playerSaleMap){
        float totalCost = 0;
        for (Map.Entry<String, Integer> entry : playerSaleMap.entrySet()) {
            String itemId = entry.getKey();
            BaseCustomItem baseCustomItem = customItemRegistryService.getBaseItemById(itemId);
            Float value = baseCustomItem.getComponent(ValueComponent.class).getValue();
            float singleCost = value != null ? value : 0;
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
