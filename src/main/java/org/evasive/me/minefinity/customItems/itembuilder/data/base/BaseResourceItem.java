package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;

public class BaseResourceItem extends BaseCustomItem {

    public BaseResourceItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.RESOURCE);
    }

    public BaseResourceItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseResourceItem(this.buildItem());
    }
}
