package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

/**
 * An {@link org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent} whose value can
 * be edited from the item creation GUI. Each component owns its own editor via
 * {@link #openEditor(EditContext)} so the GUI stays a thin dispatcher.
 *
 * @param <T> the value type this component stores
 */
public interface EditableComponent<T> {

    void setValue(T value);

    T getValue();

    /** Open whatever editor this component needs (chat prompt, enum selector, instant toggle, ...). */
    void openEditor(EditContext ctx);
}
