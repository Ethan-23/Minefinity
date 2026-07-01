package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

public interface EditableComponent<T> {

    void setValue(T value);

    T getValue();

    void openEditor(EditContext ctx);
}
