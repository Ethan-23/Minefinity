package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.FUEL_AMOUNT_KEY;

public class BaseFuelItem extends BaseCustomItem {


    public BaseFuelItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity);
    }

    public BaseFuelItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

    }

    public BaseFuelItem(String id, Material material, Rarity rarity, CustomItemType itemType, int fuelAmount) {
        super(id, material, id, rarity, itemType);
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();
        return lore;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .build();
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseFuelItem(this.buildItem());
    }
}
