package org.evasive.me.minefinity.customItems.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.FuelAmountComponent;

public class BaseFuelItem extends BaseCustomItem {

    public BaseFuelItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.FUEL);
    }

    public BaseFuelItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        addComponent(new FuelAmountComponent());
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseFuelItem(this.buildItem());
    }
}
