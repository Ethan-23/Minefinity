package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartAbilityComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartSlotComponent;

/**
 * A tool part. Carries the slots it can fill ({@link PartSlotComponent}) and the abilities it
 * grants ({@link PartAbilityComponent}).
 */
public class BasePartItem extends BaseCustomItem {

    public BasePartItem(String id, Material material, String name, Rarity rarity) {
        super(id, material, name, rarity, CustomItemType.TOOL_PART);
    }

    public BasePartItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        addComponent(new PartSlotComponent());
        addComponent(new PartAbilityComponent());
    }

    public PartAbilityComponent abilityComponent() {
        return getComponent(PartAbilityComponent.class);
    }

    public PartSlotComponent slotComponent() {
        return getComponent(PartSlotComponent.class);
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePartItem(this.buildItem());
    }
}
