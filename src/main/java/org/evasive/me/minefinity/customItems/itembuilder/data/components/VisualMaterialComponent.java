package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STACK_SIZE_KEY;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.VISUAL_MATERIAL_KEY;

public class VisualMaterialComponent implements ItemComponent, EditableComponent<Material> {

    private Material material;

    @Override
    public void load(PersistentDataContainer pdc) {

        if(!pdc.has(VISUAL_MATERIAL_KEY))
            return;

        String materialId = pdc.get(STACK_SIZE_KEY, PersistentDataType.STRING);

        if(materialId == null)
            return;

        material = Material.getMaterial(materialId);
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.setVisualMaterial(material);
    }

    @Override
    public void addLore(List<String> lore) {

    }


    @Override
    public Class<?> type() {
        return String.class;
    }

    @Override
    public void setValue(Material value) {
        material = value;
    }

    @Override
    public Material getValue() {
        return material;
    }
}
