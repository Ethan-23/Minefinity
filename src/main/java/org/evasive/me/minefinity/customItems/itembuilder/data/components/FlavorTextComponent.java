package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.FLAVOR_TEXT_KEY;

public class FlavorTextComponent implements ItemComponent, EditableComponent<String>{

    String flavorText;

    @Override
    public void load(PersistentDataContainer pdc) {
        flavorText = pdc.has(FLAVOR_TEXT_KEY) ? pdc.get(FLAVOR_TEXT_KEY, PersistentDataType.STRING) : null;
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.setFlavorText(flavorText);
    }

    @Override
    public void addLore(List<String> lore) {
        lore.add(flavorText);
    }

    @Override
    public Class<?> type() {
        return String.class;
    }

    @Override
    public void setValue(String value) {
        flavorText = value;
    }

    @Override
    public String getValue() {
        return flavorText;
    }
}
