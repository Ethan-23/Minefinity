package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.VALUE_KEY;

public class ValueComponent implements ItemComponent, EditableComponent<Float> {

    private Float value;

    private static final String SECTION_ID = "sell-value";

    @Override
    public void load(PersistentDataContainer pdc) {
        value = pdc.get(VALUE_KEY, PersistentDataType.FLOAT);
    }

    @Override
    public void save(CustomItemBuilder builder) {
        if (value != null)
            builder.setValue(value);
    }

    @Override
    public void addLore(List<String> lore) {
        if (value != null && value > 0) {
            lore.add("<gold>Value: <yellow>" + value);
        }
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptFloat(v -> value = v);
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (value != null)
            s.set(SECTION_ID, value);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        if (s.isSet(SECTION_ID))
            value = Math.max(0f, (float) s.getDouble(SECTION_ID));
    }
}
