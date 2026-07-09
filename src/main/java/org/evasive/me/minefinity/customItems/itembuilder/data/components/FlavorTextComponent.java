package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.FLAVOR_TEXT_KEY;

public class FlavorTextComponent implements ItemComponent, EditableComponent<String> {

    private String flavorText;

    private static final String SECTION_ID = "flavor-text";

    @Override
    public void load(PersistentDataContainer pdc) {
        flavorText = pdc.get(FLAVOR_TEXT_KEY, PersistentDataType.STRING);
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.setFlavorText(flavorText);
    }

    @Override
    public void addLore(List<String> lore) {
        if (flavorText != null && !flavorText.isBlank()) {
            lore.add("<gray><italic>" + flavorText);
        }
    }

    @Override
    public void setValue(String value) {
        flavorText = value;
    }

    @Override
    public String getValue() {
        return flavorText;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptString(value -> flavorText = value.isBlank() ? null : value);
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (flavorText != null)
            s.set(SECTION_ID, flavorText);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        if (s.isSet(SECTION_ID))
            flavorText = s.getString(SECTION_ID);
    }
}
