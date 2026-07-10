package org.evasive.me.minefinity.customItems.types.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;

import java.util.List;

public class BaseAxeItem extends BaseToolItem {

    public BaseAxeItem(ItemStack itemStack) {
        super(itemStack);
    }

    public BaseAxeItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.AXE);
    }

    @Override
    public List<PartSlots> getToolSlots() {
        return List.of(PartSlots.HEAD, PartSlots.HANDLE);
    }

    @Override
    public ItemStack buildItem() {
        return new CustomItemBuilder(super.buildItem())
                .addUnbreakable()
                .build();
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseAxeItem(this.buildItem());
    }
}
