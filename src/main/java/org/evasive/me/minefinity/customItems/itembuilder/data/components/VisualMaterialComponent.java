package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.VISUAL_MATERIAL_KEY;

public class VisualMaterialComponent implements ItemComponent, EditableComponent<Material> {

    private Material material;

    private static final String SECTION_ID = "visual-material";

    @Override
    public void load(PersistentDataContainer pdc) {
        String materialId = pdc.get(VISUAL_MATERIAL_KEY, PersistentDataType.STRING);
        if (materialId != null) {
            material = Material.getMaterial(materialId);
        }
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.setVisualMaterial(material);
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Material value) {
        material = value;
    }

    @Override
    public Material getValue() {
        return material;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptString(input -> {
            Material parsed = Material.matchMaterial(input.trim());
            if (parsed == null) {
                ctx.player().sendMessage(TextConversions.parse("<red>Unknown material: " + input));
                return;
            }
            material = parsed;
        });
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (material != null)
            s.set(SECTION_ID, material.name());
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        if (s.isSet(SECTION_ID)) {
            String materialId = s.getString(SECTION_ID);
            if(materialId == null)
                return;
            material = Material.getMaterial(materialId);
        }

    }
}
