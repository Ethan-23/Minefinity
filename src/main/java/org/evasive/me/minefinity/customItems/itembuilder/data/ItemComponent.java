package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;

import java.util.List;

public interface ItemComponent {

    void load(PersistentDataContainer pdc);

    void save(CustomItemBuilder builder);

    default void saveToConfig(ConfigurationSection s) {

    }
    default void loadFromConfig(ConfigurationSection s) {

    }

    void addLore(List<String> lore);

    default void addFooter(List<String> lore){}

    default boolean isInstanceData() { return false; }
}