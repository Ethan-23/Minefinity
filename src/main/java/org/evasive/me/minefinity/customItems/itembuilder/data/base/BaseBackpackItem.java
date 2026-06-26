package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ITEMID_STORAGE_LIST_KEY;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STORAGE_AMOUNT_KEY;

public class BaseBackpackItem extends BaseCustomItem {

    public BaseBackpackItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity);
    }

    public BaseBackpackItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();





    }

    //Update lore to take multiple lines if too long
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
        return new BaseBackpackItem(this.buildItem());
    }
}
