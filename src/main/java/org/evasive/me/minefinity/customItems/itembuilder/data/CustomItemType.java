package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseAxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.factories.ItemStackFactory;
import org.evasive.me.minefinity.customItems.itembuilder.factories.NewItemFactory;

import java.util.ArrayList;
import java.util.List;

public enum CustomItemType {
    CUSTOM_ITEM(
            BaseCustomItem::new, BaseCustomItem::new,
            Material.COMMAND_BLOCK
    ),
    RESOURCE(BaseResourceItem::new,BaseResourceItem::new,
            Material.COPPER_ORE
    ),
    PICKAXE(BasePickaxeItem::new, BasePickaxeItem::new,
            Material.WOODEN_PICKAXE
    ),
    AXE(BaseAxeItem::new, BaseAxeItem::new,
            Material.WOODEN_AXE
    ),
    TOOL_PART(BasePartItem::new, BasePartItem::new,
            Material.STICK),
    FUEL(BaseFuelItem::new,BaseFuelItem::new,
            Material.CHARCOAL
    ),
    STORAGE(BaseBackpackItem::new,BaseBackpackItem::new,
            Material.CHEST
    )
    ;

    private final NewItemFactory newItemFactory;
    private final ItemStackFactory itemStackFactory;
    private final Material displayMaterial;

    CustomItemType(NewItemFactory newItemFactory, ItemStackFactory itemStackFactory, Material displayMaterial) {
        this.newItemFactory = newItemFactory;
        this.itemStackFactory = itemStackFactory;
        this.displayMaterial = displayMaterial;
    }

    public BaseCustomItem create(String id, Material material, String name, Rarity rarity) {
        return newItemFactory.create(id, material, name, rarity);
    }

    public BaseCustomItem create(ItemStack itemStack) {
        return itemStackFactory.create(itemStack);
    }

    public Material getDisplayMaterial() {
        return displayMaterial;
    }
}
