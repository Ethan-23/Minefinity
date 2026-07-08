package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STACK_SIZE_KEY;

public class StackSizeComponent implements ItemComponent, EditableComponent<Integer> {

    private Integer stackSize;

    private static final String SECTION_ID = "stack-size";

    @Override
    public void load(PersistentDataContainer pdc) {
        stackSize = pdc.get(STACK_SIZE_KEY, PersistentDataType.INTEGER);
    }

    @Override
    public void save(CustomItemBuilder builder) {
        if (stackSize != null)
            builder.setStackSize(stackSize);
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Integer value) {
        stackSize = value;
    }

    @Override
    public Integer getValue() {
        return stackSize;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptInt(value -> stackSize = Math.clamp(value, 1, 99));
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (stackSize != null)
            s.set(SECTION_ID, stackSize);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        if (s.isSet(SECTION_ID))
            stackSize = s.getInt(SECTION_ID);
    }
}
