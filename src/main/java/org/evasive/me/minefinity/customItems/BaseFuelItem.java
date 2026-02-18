package org.evasive.me.minefinity.customItems;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;

public class BaseFuelItem extends BaseCustomItem {
    private int fuelAmount;


    public BaseFuelItem(String id, Material material, Rarity rarity, CustomItemType itemType, float value, int fuelAmount) {
        super(id, material, rarity, itemType, value);
        this.fuelAmount = fuelAmount;
    }
}
