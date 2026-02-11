package org.evasive.me.minevolutionCore.customItems.pickaxe;

import org.bukkit.Material;
import org.evasive.me.minevolutionCore.core.items.CustomItem;
import org.evasive.me.minevolutionCore.customItems.items.CustomItemType;
import org.evasive.me.minevolutionCore.rarity.Rarity;

public enum PickaxeItem implements CustomItem {
    WOODEN_TEMPLATE(Material.WOODEN_PICKAXE, Rarity.MINOR, 1f),
    COPPER_TEMPLATE(Material.COPPER_PICKAXE, Rarity.UNIQUE, 5f),
    IRON_TEMPLATE(Material.IRON_PICKAXE, Rarity.RADIANT, 20f),
    GOLD_TEMPLATE(Material.GOLDEN_PICKAXE, Rarity.EXQUISITE, 50f),
    DIAMOND_TEMPLATE(Material.DIAMOND_PICKAXE, Rarity.PRISTINE, 100f),
    EMERALD_TEMPLATE(Material.DIAMOND_PICKAXE, Rarity.PRISTINE, 250f)
    ;


    private final BasePickaxeItem builder;
    PickaxeItem(Material material, Rarity rarity, float baseMiningSpeed) {
        // Use the enum constant name as the ID automatically
        this.builder = new BasePickaxeItem (this.name(), material, rarity, CustomItemType.PICKAXE, baseMiningSpeed);
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
