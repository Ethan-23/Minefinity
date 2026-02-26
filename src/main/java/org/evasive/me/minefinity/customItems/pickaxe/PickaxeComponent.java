package org.evasive.me.minefinity.customItems.pickaxe;

import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.types.ResourceItem;
import org.evasive.me.minefinity.rarity.Rarity;

public enum PickaxeComponent implements CustomItem {
    FLINT_HEAD(ResourceItem.FLINT, Rarity.MINOR, CustomItemType.PICKAXE_HEAD, PickaxeItem.WOODEN_TEMPLATE, "#6e6e6e"),
    BRICK_CORE(ResourceItem.BRICK, Rarity.MINOR, CustomItemType.PICKAXE_CORE, PickaxeItem.WOODEN_TEMPLATE, "#ab5e59"),
    CLAY_HANDLE(ResourceItem.CLAY_BALL, Rarity.MINOR, CustomItemType.PICKAXE_HANDLE, PickaxeItem.WOODEN_TEMPLATE, "#d1d1d1"),

    ANDESITE_CORE(ResourceItem.ANDESITE, Rarity.MINOR, CustomItemType.PICKAXE_CORE, PickaxeItem.STONE_TEMPLATE, "#adadad"),
    ROCK_HANDLE(ResourceItem.ROCK, Rarity.MINOR, CustomItemType.PICKAXE_HANDLE, PickaxeItem.STONE_TEMPLATE, "#696969"),

    TUFF_HEAD(ResourceItem.TUFF, Rarity.UNIQUE, CustomItemType.PICKAXE_HEAD, PickaxeItem.COPPER_TEMPLATE, "#3d3d3d"),
    BRONZE_HEAD(ResourceItem.BRONZE_INGOT, Rarity.UNIQUE, CustomItemType.PICKAXE_HEAD, PickaxeItem.COPPER_TEMPLATE, "#bd921c"),
    TOUGH_STONE_CORE(ResourceItem.TOUGH_STONE, Rarity.UNIQUE, CustomItemType.PICKAXE_CORE, PickaxeItem.COPPER_TEMPLATE, "#54594a"),
    DIORITE_CORE(ResourceItem.DIORITE, Rarity.UNIQUE, CustomItemType.PICKAXE_CORE, PickaxeItem.COPPER_TEMPLATE, "#e6e6e6"),
    MOSSY_TIMBER_HANDLE(ResourceItem.MOSSY_TIMBER, Rarity.UNIQUE, CustomItemType.PICKAXE_HANDLE, PickaxeItem.COPPER_TEMPLATE, "#706311"),

    ;

    private final BasePickaxeComponent builder;

    PickaxeComponent(ResourceItem resourceItem, Rarity rarity, CustomItemType partType, PickaxeItem minimumRequiredTemplateTier, String colorCode) {
        this.builder = new BasePickaxeComponent(this.name(), resourceItem.getBuilder().getMaterial(), rarity, partType, minimumRequiredTemplateTier, colorCode);
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
