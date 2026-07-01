package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StorageAmountComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StorageListComponent;

public class BaseBackpackItem extends BaseCustomItem {

    public BaseBackpackItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.STORAGE);
    }

    public BaseBackpackItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        addComponent(new StorageAmountComponent());
        addComponent(new StorageListComponent());
    }

    public StorageAmountComponent storageAmountComponent() {
        return getComponent(StorageAmountComponent.class);
    }

    public StorageListComponent storageListComponent() {
        return getComponent(StorageListComponent.class);
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseBackpackItem(this.buildItem());
    }
}
