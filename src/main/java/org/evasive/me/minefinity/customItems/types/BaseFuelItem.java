package org.evasive.me.minefinity.customItems.types;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.List;

import static org.evasive.me.minefinity.utils.TextConversions.buildItemRarity;
import static org.evasive.me.minefinity.utils.TextConversions.buildItemType;

public class BaseFuelItem extends BaseCustomItem {
    private final int fuelAmount;


    public BaseFuelItem(String id, Material material, Rarity rarity, CustomItemType itemType, float value, int fuelAmount) {
        super(id, material, rarity, itemType, value);
        this.fuelAmount = fuelAmount;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    @Override
    protected List<String> getLore() {
        return List.of(
                buildItemType(getItemType().name()),
                "<gray> Efficiency: " + this.fuelAmount,
                "",
                buildItemRarity(getRarity())
        );
    }
}
