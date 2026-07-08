package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.SOULBOUND_KEY;

public class SoulboundComponent implements ItemComponent, EditableComponent<Boolean> {

    private boolean soulbound;

    private static final String SECTION_ID = "soulbound";

    @Override
    public void load(PersistentDataContainer pdc) {
        soulbound = pdc.has(SOULBOUND_KEY);
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.setSoulbound(soulbound);
    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public void addFooter(List<String> footer){
        if (soulbound) {
            footer.add("");
            footer.add("<dark_gray>* Soulbound *");
        }
    }

    @Override
    public void setValue(Boolean value) {
        soulbound = Boolean.TRUE.equals(value);
    }

    @Override
    public Boolean getValue() {
        return soulbound;
    }

    @Override
    public void openEditor(EditContext ctx) {
        soulbound = !soulbound;
        ctx.reopen();
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        s.set(SECTION_ID, soulbound);
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        soulbound = s.getBoolean(SECTION_ID);
    }
}
