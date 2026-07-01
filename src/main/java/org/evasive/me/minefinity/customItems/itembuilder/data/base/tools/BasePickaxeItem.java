package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StatsComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasePickaxeItem extends BaseToolItem {

    public BasePickaxeItem(ItemStack itemStack) {
        super(itemStack);
    }

    public BasePickaxeItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.PICKAXE);
    }

    @Override
    public List<PartSlots> getToolSlots() {
        return List.of(PartSlots.PICKAXE_HEAD, PartSlots.PICKAXE_CORE, PartSlots.PICKAXE_HANDLE);
    }

    @Override
    public ItemStack buildItem() {
        return new CustomItemBuilder(super.buildItem())
                .addUnbreakable()
                .build();
    }

    public ItemStack buildItem(ToolItemData data) {
        return buildItem();
    }

    public Map<String, Integer> getTotalStats(List<BasePartItem> parts) {
        Map<String, Integer> total = new HashMap<>();

        StatsComponent own = getComponent(StatsComponent.class);
        if (own != null) {
            own.getValue().forEach((key, value) -> total.merge(key, value, Integer::sum));
        }

        for (BasePartItem part : parts) {
            if (part == null) continue;
            StatsComponent partStats = part.getComponent(StatsComponent.class);
            if (partStats != null) {
                partStats.getValue().forEach((key, value) -> total.merge(key, value, Integer::sum));
            }
        }

        return total;
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePickaxeItem(this.buildItem());
    }
}
