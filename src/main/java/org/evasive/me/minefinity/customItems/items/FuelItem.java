package org.evasive.me.minefinity.customItems.items;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.BaseFuelItem;
import org.evasive.me.minefinity.rarity.Rarity;

public enum FuelItem implements CustomItem {
    CHARCOAL(Material.CHARCOAL, Rarity.MINOR, 1),
    COAL(Material.COAL, Rarity.UNIQUE, 3),
    ENRICHED_COAL(Material.MUD, Rarity.RADIANT, 9),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.EXQUISITE, 27),
    MAGMA_COAL(Material.BLAZE_POWDER, Rarity.PRISTINE, 81)
    ;

    private final BaseFuelItem builder;
    FuelItem(Material material, Rarity rarity, int fuelAmount) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseFuelItem(this.name(), material, rarity, CustomItemType.FUEL, -1, fuelAmount);
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    @Override
    public BaseCustomItem getBuilder() {
        return builder;
    }
}
