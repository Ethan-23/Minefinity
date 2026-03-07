package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.List;

public class BaseResourceItem extends BaseCustomItem {


    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY
    );

    public BaseResourceItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.RESOURCE);
    }

    public BaseResourceItem(ItemStack itemStack) {
        super(itemStack);
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }
}
