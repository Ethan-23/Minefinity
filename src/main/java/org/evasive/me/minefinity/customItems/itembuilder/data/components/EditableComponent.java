package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;

public interface EditableComponent<T> {

    Class<?> type();

    void setValue(T value);

    T getValue();

    void openEditor(Player player, BaseCustomItem item, Runnable onClose);

}
