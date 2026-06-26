package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.SOULBOUND_KEY;

public class SoulboundComponent implements ItemComponent, EditableComponent<Boolean> {

    private boolean soulbound;

    @Override
    public void load(PersistentDataContainer pdc) {
        soulbound = pdc.has(SOULBOUND_KEY);
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.setSoulbound(soulbound);
    }

    @Override
    public void addLore(List<String> lore) {
        lore.add("<gray>Soulbound");
    }

    @Override
    public Class<?> type() {
        return Boolean.class;
    }

    @Override
    public void setValue(Boolean value) {
        soulbound = value;
    }

    @Override
    public Boolean getValue() {
        return soulbound;
    }
}
