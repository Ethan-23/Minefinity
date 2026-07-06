package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;

import java.util.List;

public interface ItemComponent {

    void load(PersistentDataContainer pdc);

    void save(CustomItemBuilder builder);

    void addLore(List<String> lore);

    default boolean isInstanceData() { return false; }
}