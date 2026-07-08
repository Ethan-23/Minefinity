package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.EQUIPMENT_SLOT_KEY;

public class EquipmentSlotComponent implements ItemComponent, EditableComponent<Set<EquipmentSlot>> {

    private Set<EquipmentSlot> equipmentSlots = new HashSet<>();

    private static final String SECTION_ID = "equipment-slot";

    @Override
    public void load(PersistentDataContainer pdc) {
        this.equipmentSlots = new HashSet<>();

        if (!pdc.has(EQUIPMENT_SLOT_KEY))
            return;

        String joined = pdc.get(EQUIPMENT_SLOT_KEY, PersistentDataType.STRING);
        if (joined == null || joined.isEmpty())
            return;

        for (String slot : joined.split(";;")) {
            try {
                equipmentSlots.add(EquipmentSlot.valueOf(slot));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.addPersistentDataContainer(
                EQUIPMENT_SLOT_KEY,
                PersistentDataType.STRING,
                equipmentSlots.stream().map(Enum::name).collect(Collectors.joining(";;"))
        );
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Set<EquipmentSlot> value) {
        equipmentSlots = value == null ? new HashSet<>() : value;
    }

    @Override
    public Set<EquipmentSlot> getValue() {
        return equipmentSlots;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.openSelector(EquipmentSlot.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(EquipmentSlot slot) {
                CustomItemBuilder icon = new CustomItemBuilder(Material.ARMOR_STAND, TextConversions.formatItemName(slot.name()));
                icon.addLore("<gray>Click to toggle");
                if (equipmentSlots.contains(slot))
                    icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(EquipmentSlot slot, ClickType click, OptionsGui<EquipmentSlot> gui) {
                if (!equipmentSlots.remove(slot)) {
                    equipmentSlots.add(slot);
                }
            }
        });
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (!equipmentSlots.isEmpty()) {
            s.set(SECTION_ID, equipmentSlots.stream().map(Enum::name).toList());
        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        this.equipmentSlots = new HashSet<>();
        for (String slot : s.getStringList(SECTION_ID)) {
            try {
                equipmentSlots.add(EquipmentSlot.valueOf(slot));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
