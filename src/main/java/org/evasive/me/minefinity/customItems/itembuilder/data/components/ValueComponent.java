package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STACK_SIZE_KEY;

public class ValueComponent implements ItemComponent, EditableComponent<Float> {

    private Float value;

    @Override
    public void load(PersistentDataContainer pdc) {
        value = pdc.has(STACK_SIZE_KEY) ? pdc.get(STACK_SIZE_KEY, PersistentDataType.FLOAT) : null;
    }

    @Override
    public void save(ItemBuilder builder) {
        if(value != null) builder.setValue(value);
    }

    @Override
    public void addLore(List<String> lore) {

    }


    @Override
    public Class<?> type() {
        return Float.class;
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }
}
