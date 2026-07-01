package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

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
        return List.of(PartSlots.AXE_HEAD, PartSlots.AXE_HANDLE);
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
