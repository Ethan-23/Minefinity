package org.evasive.me.minefinity.customItems.types;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.rarity.Rarity;

public enum FuelItem implements CustomItem {
    BARK (Material.OAK_LOG, Rarity.MINOR, 1),
    CHARCOAL(Material.CHARCOAL, Rarity.MINOR, 3),
    COAL(Material.COAL, Rarity.UNIQUE, 6),
    ENRICHED_COAL(Material.MUD, Rarity.RADIANT, 12),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.EXQUISITE, 36),
    MAGMA_COAL(Material.BLAZE_POWDER, Rarity.PRISTINE, 108)
    ;

    private final BaseFuelItem builder;
    FuelItem(Material material, Rarity rarity, int fuelAmount) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseFuelItem(this.name(), material, rarity, CustomItemType.FUEL, -1, fuelAmount);
    }

    public static boolean contains(String value) {
        for (FuelItem fuelItem : FuelItem.values()) {
            if (fuelItem.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    @Override
    public BaseFuelItem getBuilder() {
        return builder;
    }
}
