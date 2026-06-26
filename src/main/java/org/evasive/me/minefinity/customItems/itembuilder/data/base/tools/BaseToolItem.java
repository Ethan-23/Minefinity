package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseToolItem extends BaseCustomItem {

    private final LinkedHashMap<PartSlots, String> partMap = new LinkedHashMap<>();

    public BaseToolItem(ItemStack itemStack) {
        super(itemStack);
    }

    public BaseToolItem(String id, Material material, String displayName, Rarity rarity, CustomItemType customItemType) {
        super(id, material, displayName, rarity, customItemType);
    }

    public void setPart(PartSlots partComponent, String partId) {
        partMap.put(partComponent, partId);
    }

    public String getPart(PartSlots partComponent) {
        return partMap.get(partComponent);
    }

    public Map<PartSlots, String> getPartMap() {
        return Collections.unmodifiableMap(partMap);
    }

}
