package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.GLOWING_KEY;

public class GlowComponent implements ItemComponent, EditableComponent<Boolean> {

    private boolean glowing;

    private static final String SECTION_ID = "glowing";

    @Override
    public void load(PersistentDataContainer pdc) {
        glowing = pdc.has(GLOWING_KEY);
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.setGlow(glowing);
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Boolean value) {
        glowing = Boolean.TRUE.equals(value);
    }

    @Override
    public Boolean getValue() {
        return glowing;
    }

    @Override
    public void openEditor(EditContext ctx) {
        glowing = !glowing;
        ctx.reopen();
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        s.set(SECTION_ID, glowing);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        glowing = s.getBoolean(SECTION_ID);
    }
}
