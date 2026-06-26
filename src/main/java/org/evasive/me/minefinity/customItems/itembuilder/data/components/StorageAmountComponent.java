package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STORAGE_AMOUNT_KEY;

public class StorageAmountComponent implements ItemComponent, EditableComponent<Integer> {

    private int storageAmount;

    @Override
    public void load(PersistentDataContainer pdc) {

        if(!pdc.has(STORAGE_AMOUNT_KEY))
            return;

        this.storageAmount = Objects.requireNonNullElse(pdc.get(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER), 0);
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER, this.storageAmount);
    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public Class<?> type() {
        return Integer.class;
    }

    @Override
    public void setValue(Integer value) {
        this.storageAmount = value;
    }

    @Override
    public Integer getValue() {
        return this.storageAmount;
    }
}
