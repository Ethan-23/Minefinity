package org.evasive.me.minevolutionCore.customItems.items;

import org.bukkit.Material;
import org.evasive.me.minevolutionCore.core.items.BaseCustomItem;
import org.evasive.me.minevolutionCore.core.items.CustomItem;
import org.evasive.me.minevolutionCore.rarity.Rarity;

public enum FuelItem implements CustomItem {
    COAL(Material.COAL, Rarity.MINOR),
    ENHANCED_COAL(Material.COAL, Rarity.RADIANT),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.EXQUISITE)
    ;

    private final BaseCustomItem builder;
    FuelItem(Material material, Rarity rarity) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseCustomItem(this.name(), material, rarity, CustomItemType.FUEL, -1);
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
