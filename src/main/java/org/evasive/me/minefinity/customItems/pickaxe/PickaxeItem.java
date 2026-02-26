package org.evasive.me.minefinity.customItems.pickaxe;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;

public enum PickaxeItem implements CustomItem {
    WOODEN_TEMPLATE(Material.WOODEN_PICKAXE, Rarity.MINOR, 1f),
    STONE_TEMPLATE(Material.STONE_PICKAXE, Rarity.MINOR, 3f),
    COPPER_TEMPLATE(Material.COPPER_PICKAXE, Rarity.UNIQUE, 6f),
    IRON_TEMPLATE(Material.IRON_PICKAXE, Rarity.RADIANT, 12f),
    GOLD_TEMPLATE(Material.GOLDEN_PICKAXE, Rarity.EXQUISITE, 24f),
    DIAMOND_TEMPLATE(Material.DIAMOND_PICKAXE, Rarity.PRISTINE, 48f)
    ;


    private final BasePickaxeItem builder;
    PickaxeItem(Material material, Rarity rarity, float baseMiningSpeed) {
        // Use the enum constant name as the ID automatically
        this.builder = new BasePickaxeItem (this.name(), material, rarity, CustomItemType.PICKAXE, baseMiningSpeed, null, null, null);
    }

    public static boolean contains(String id) {
        try {
            PickaxeItem.valueOf(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    @Override
    public BasePickaxeItem getBuilder() {
        return builder;
    }
}
