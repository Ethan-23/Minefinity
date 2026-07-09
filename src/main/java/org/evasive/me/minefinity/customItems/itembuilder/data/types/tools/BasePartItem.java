package org.evasive.me.minefinity.customItems.itembuilder.data.types.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.AcceptableToolsComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartAbilityComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.PartSlotComponent;

import java.util.Set;

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
        addComponent(new AcceptableToolsComponent());
    }

    public PartAbilityComponent abilityComponent() {
        return getComponent(PartAbilityComponent.class);
    }

    public PartSlotComponent slotComponent() {
        return getComponent(PartSlotComponent.class);
    }

    public AcceptableToolsComponent acceptableToolsComponent() {
        return getComponent(AcceptableToolsComponent.class);
    }

    public Set<CustomItemType> acceptableTools() {
        return acceptableToolsComponent().getValue();
    }

    public boolean fits(PartSlots toolCategory, CustomItemType toolType) {
        return slotComponent().getValue().contains(toolCategory)
                && (acceptableTools().isEmpty() || acceptableTools().contains(toolType));
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePartItem(this.buildItem());
    }
}
