package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STORAGE_AMOUNT_KEY;

public class StorageAmountComponent implements ItemComponent, EditableComponent<Integer> {

    private int storageAmount;

    private static final String SECTION_ID = "storage-amount";

    @Override
    public void load(PersistentDataContainer pdc) {
        if (pdc.has(STORAGE_AMOUNT_KEY)) {
            this.storageAmount = Objects.requireNonNullElse(pdc.get(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER), 0);
        }
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.addPersistentDataContainer(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER, this.storageAmount);
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Integer value) {
        this.storageAmount = value == null ? 0 : value;
    }

    @Override
    public Integer getValue() {
        return this.storageAmount;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptInt(value -> storageAmount = Math.max(0, value));
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        s.set(SECTION_ID, storageAmount);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        storageAmount = s.getInt(SECTION_ID);
    }
}
