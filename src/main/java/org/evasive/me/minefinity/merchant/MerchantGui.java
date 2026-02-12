package org.evasive.me.minefinity.merchant;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;

import java.util.List;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;

public class MerchantGui extends BaseGui {

    private static final int INVENTORY_SIZE = 27;
    private static final int SELL_INVENTORY_SLOT = 12;
    private static final int SELL_BACKPACK_SLOT = 14;
    MerchantHandler merchantHandler;

    public MerchantGui(Player player) {
        super(player, INVENTORY_SIZE, Messages.parse("Merchant"));
        build();
        merchantHandler = new MerchantHandler();
    }

    @Override
    protected void build() {
        buildOutline();
        addButtons();
    }

    private void buildOutline(){
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(List.of(SELL_INVENTORY_SLOT, SELL_BACKPACK_SLOT).contains(i)) continue;
            inventory.setItem(i, fillerPane);
        }
    }

    private void addButtons(){
        inventory.setItem(SELL_INVENTORY_SLOT, new ItemBuilder(Material.CHEST, Messages.parse("Sell Inventory")).build());
        inventory.setItem(SELL_BACKPACK_SLOT, new ItemBuilder(Material.BUNDLE, Messages.parse("Sell Backpacks")).build());
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
