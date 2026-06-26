package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ItemComponent {

    void load(PersistentDataContainer pdc);

    void save(ItemBuilder builder);

    void addLore(List<String> lore);
}