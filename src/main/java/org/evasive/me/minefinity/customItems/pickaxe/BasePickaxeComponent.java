package org.evasive.me.minefinity.customItems.pickaxe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.ItemBuilder;

public class BasePickaxeComponent extends BaseCustomItem {

    private final PickaxeItem minimumRequiredTemplateTier;
    private final String colorCode;

    public BasePickaxeComponent(String id, Material material, Rarity rarity, CustomItemType itemType, PickaxeItem minimumRequiredTemplateTier, String colorCode) {
        super(id, material, rarity, itemType, -1);
        this.minimumRequiredTemplateTier = minimumRequiredTemplateTier;
        this.colorCode = colorCode;
    }

    public PickaxeItem getMinimumRequiredTemplateTier() {
        return minimumRequiredTemplateTier;
    }

    public String getColorCode() {
        return colorCode;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem()).addUniqueTag().build();
    }
}

