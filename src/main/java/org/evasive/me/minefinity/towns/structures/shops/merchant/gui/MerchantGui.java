package org.evasive.me.minefinity.towns.structures.shops.merchant.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.shops.merchant.service.MerchantHandler;

public class MerchantGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int SELL_INVENTORY_SLOT = 21;
    private static final int SELL_BACKPACK_SLOT = 23;
    private final MerchantHandler merchantHandler;

    public MerchantGui(Player player, CustomItemRegistryService customItemRegistryService, EconomyService economyService, BackpackService backpackService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Merchant"));
        build();
        merchantHandler = new MerchantHandler(economyService, customItemRegistryService,  backpackService);
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        addButtons();
    }

    private void addButtons(){
        inventory.setItem(SELL_INVENTORY_SLOT, new CustomItemBuilder(Material.CHEST, TextConversions.parse("Sell Inventory")).build());
        inventory.setItem(SELL_BACKPACK_SLOT, new CustomItemBuilder(Material.BUNDLE, TextConversions.parse("Sell Backpacks")).build());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        e.setCancelled(true);

        int slot = e.getSlot();

        if(slot == SELL_INVENTORY_SLOT){
            merchantHandler.handleInventorySell(player);
            return;
        }

        if(slot == SELL_BACKPACK_SLOT){
            merchantHandler.handleBackpackSell(player);
            return;
        }
    }
}
