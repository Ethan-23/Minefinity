package org.evasive.me.minefinity.customItems.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemOptions;
import org.evasive.me.minefinity.customItems.types.tools.BaseAxeItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.factories.ItemStackFactory;
import org.evasive.me.minefinity.customItems.itembuilder.factories.NewItemFactory;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemOptions.*;

public enum CustomItemType {
    CUSTOM_ITEM(BaseCustomItem::new, BaseCustomItem::new, Material.COMMAND_BLOCK),
    RESOURCE(BaseResourceItem::new, BaseResourceItem::new, Material.COPPER_ORE),
    PICKAXE(BasePickaxeItem::new, BasePickaxeItem::new, Material.WOODEN_PICKAXE, TOOL_PARTS),
    AXE(BaseAxeItem::new, BaseAxeItem::new, Material.WOODEN_AXE, TOOL_PARTS),
    TOOL_PART(BasePartItem::new, BasePartItem::new, Material.STICK, PART_SLOT, PART_ABILITY, ACCEPTABLE_TOOLS),
    FUEL(BaseFuelItem::new, BaseFuelItem::new, Material.CHARCOAL, FUEL_AMOUNT),
    STORAGE(BaseBackpackItem::new, BaseBackpackItem::new, Material.CHEST, STORAGE_AMOUNT, STORAGE_LIST);

    private final NewItemFactory newItemFactory;
    private final ItemStackFactory itemStackFactory;
    private final Material displayMaterial;
    private final List<ItemOptions> extraOptions;

    CustomItemType(NewItemFactory newItemFactory, ItemStackFactory itemStackFactory, Material displayMaterial, ItemOptions... extraOptions) {
        this.newItemFactory = newItemFactory;
        this.itemStackFactory = itemStackFactory;
        this.displayMaterial = displayMaterial;
        this.extraOptions = List.of(extraOptions);
    }

    private static List<ItemOptions> commonOptions() {
        return List.of(
                MINEFINITY_ID, MATERIAL, DISPLAY_NAME, CUSTOM_ITEM_TYPE, RARITY,
                STATS, EQUIPMENT_SLOT,
                SELL_VALUE, VISUAL_MATERIAL, FLAVOR_LORE, GLOWING, STACK_SIZE, SOULBOUND
        );
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

    public List<ItemOptions> getAllOptions() {
        List<ItemOptions> all = new ArrayList<>(commonOptions());
        all.addAll(extraOptions);
        return all;
    }
}
