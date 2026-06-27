package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.FUEL_AMOUNT_KEY;

public class FuelAmountComponent implements ItemComponent, EditableComponent<Integer> {

    private int fuelAmount;

    @Override
    public void load(PersistentDataContainer pdc) {
        if (pdc.has(FUEL_AMOUNT_KEY)) {
            this.fuelAmount = Objects.requireNonNullElse(pdc.get(FUEL_AMOUNT_KEY, PersistentDataType.INTEGER), 1);
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(FUEL_AMOUNT_KEY, PersistentDataType.INTEGER, this.fuelAmount);
    }

    @Override
    public void addLore(List<String> lore) {
        lore.add("<gray>Fuel Amount: " + this.fuelAmount);
    }

    @Override
    public void setValue(Integer value) {
        this.fuelAmount = value == null ? 0 : value;
    }

    @Override
    public Integer getValue() {
        return this.fuelAmount;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptInt(value -> fuelAmount = Math.max(0, value));
    }
}
