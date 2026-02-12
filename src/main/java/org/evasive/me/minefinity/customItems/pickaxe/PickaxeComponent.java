package org.evasive.me.minefinity.customItems.pickaxe;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;

public enum PickaxeComponent implements CustomItem {
    WOOD_HEAD(Material.OAK_PLANKS, Rarity.MINOR, CustomItemType.PICKAXE_HEAD, PickaxeItem.WOODEN_TEMPLATE, "#AF855C"),
    WOOD_CORE(Material.OAK_PLANKS, Rarity.MINOR, CustomItemType.PICKAXE_CORE, PickaxeItem.WOODEN_TEMPLATE, "#AF855C"),
    WOOD_HANDLE(Material.OAK_PLANKS, Rarity.MINOR, CustomItemType.PICKAXE_HANDLE, PickaxeItem.WOODEN_TEMPLATE, "#AF855C"),
    STONE_HEAD(Material.COBBLESTONE, Rarity.MINOR, CustomItemType.PICKAXE_HEAD, PickaxeItem.WOODEN_TEMPLATE, "#B2BEB5"),
    CLAY_HANDLE(Material.CLAY, Rarity.MINOR, CustomItemType.PICKAXE_HANDLE, PickaxeItem.WOODEN_TEMPLATE, "#D3D3D3"),
    BRICK_CORE(Material.BRICK, Rarity.MINOR, CustomItemType.PICKAXE_CORE, PickaxeItem.WOODEN_TEMPLATE, "#BC4A3C"),
    FLINT_HEAD(Material.FLINT, Rarity.MINOR, CustomItemType.PICKAXE_HEAD, PickaxeItem.WOODEN_TEMPLATE, "#6F6A61"),
    ANDESITE_CORE(Material.ANDESITE, Rarity.MINOR,  CustomItemType.PICKAXE_CORE, PickaxeItem.WOODEN_TEMPLATE, "#777C7C"),
    COPPER_HEAD(Material.COPPER_INGOT, Rarity.UNIQUE, CustomItemType.PICKAXE_HEAD, PickaxeItem.COPPER_TEMPLATE, "#B87333"),
    COPPER_CORE(Material.COPPER_INGOT, Rarity.UNIQUE, CustomItemType.PICKAXE_CORE, PickaxeItem.COPPER_TEMPLATE, "#B87333"),
    COPPER_HANDLE(Material.COPPER_INGOT, Rarity.UNIQUE, CustomItemType.PICKAXE_HANDLE, PickaxeItem.COPPER_TEMPLATE, "#B87333"),
    ;

    private final BasePickaxeComponent builder;

    PickaxeComponent(Material material, Rarity rarity, CustomItemType partType, PickaxeItem minimumRequiredTemplateTier, String colorCode) {
        this.builder = new BasePickaxeComponent(this.name(), material, rarity, partType, minimumRequiredTemplateTier, colorCode);
    }

    public static boolean contains(String value) {
        for (PickaxeComponent component : PickaxeComponent.values()) {
            if (component.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    @Override
    public BasePickaxeComponent getBuilder() {
        return builder;
    }
}
