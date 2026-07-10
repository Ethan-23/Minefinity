package org.evasive.me.minefinity.towns.structures.mines.miner.gui.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.mines.miner.gui.selection.MinerBlockSelectionGui;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;

import java.util.Map;


public class MinerMainHandler {

    private final CustomItemRegistryService customItemRegistryService;
    private final ItemPickupService itemPickupService;
    private final AutoMinerService autoMinerService;
    private final BlockTierService blockTierService;

    public MinerMainHandler(CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, AutoMinerService autoMinerService,BlockTierService blockTierService) {
        this.autoMinerService = autoMinerService;
        this.blockTierService = blockTierService;
        this.itemPickupService = itemPickupService;
        this.customItemRegistryService = customItemRegistryService;
    }

    public void handleCollectSlot(InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        Map<String, Integer> itemMap = autoMinerService.getAutoMinerStorage(player);

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            int remaining = entry.getValue();

            int overflow = itemPickupService.attemptBackpackStorage(player, customItemRegistryService.getRegisteredItemStack(entry.getKey()), remaining);

            autoMinerService.removeAutoMinerStorage(player, entry.getKey(), remaining - overflow);

        }
    }

    public boolean handlePickaxeSlot(Player player, ItemStack cursorItem) {

        if(!cursorItem.isEmpty()) {
            return false;
        }

        BaseCustomItem baseCustomItem = customItemRegistryService.getRegisteredBaseItem(cursorItem);

        if(!(baseCustomItem instanceof BasePickaxeItem basePickaxeItem))
            return false;

        if(cursorItem.isEmpty()){
            autoMinerService.setAutoMinerPickaxe(player, null);
            return true;
        }

        autoMinerService.setAutoMinerPickaxe(player, basePickaxeItem);
        return true;

    }

    public void handleBlockSlot(Player player){
        new MinerBlockSelectionGui(player, customItemRegistryService, itemPickupService, autoMinerService, blockTierService).open();
    }



}
