package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STACK_SIZE_KEY;

public class StackSizeComponent implements ItemComponent, EditableComponent<Integer> {

    private Integer stackSize;

    @Override
    public void load(PersistentDataContainer pdc) {
        stackSize = pdc.get(STACK_SIZE_KEY, PersistentDataType.INTEGER);
    }

    @Override
    public void save(ItemBuilder builder) {
        if (stackSize != null) builder.setStackSize(stackSize);
    }

    @Override
    public void addLore(List<String> lore) {
        // visual only
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
        ctx.promptInt(value -> stackSize = Math.max(1, Math.min(99, value)));
    }
}
