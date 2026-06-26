package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.EQUIPMENT_SLOT_KEY;

public class EquipmentSlotComponent implements ItemComponent, EditableComponent<Set<EquipmentSlot>> {

    private Set<EquipmentSlot> equipmentSlots;

    @Override
    public void load(PersistentDataContainer pdc) {
        if (pdc.has(EQUIPMENT_SLOT_KEY)) {

            String joined = pdc.get(EQUIPMENT_SLOT_KEY, PersistentDataType.STRING);

            this.equipmentSlots = new HashSet<>();

            if (joined != null && !joined.isEmpty()) {
                for (String slot : joined.split(";;")) {
                    try {
                        equipmentSlots.add(EquipmentSlot.valueOf(slot));
                    } catch (IllegalArgumentException e) {
                        Bukkit.getConsoleSender().sendMessage("Invalid equipment slot: " + slot);
                    }
                }
            }

        } else {
            this.equipmentSlots = new HashSet<>();
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(EQUIPMENT_SLOT_KEY, PersistentDataType.STRING, equipmentSlots.stream().map(Enum::name).collect(Collectors.joining(";;")));
    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public Class<EquipmentSlot> type() {
        return EquipmentSlot.class;
    }

    @Override
    public void setValue(Set<EquipmentSlot> value) {
        equipmentSlots = value;
    }

    @Override
    public Set<EquipmentSlot> getValue() {
        return equipmentSlots;
    }

    @Override
    public void openEditor(Player player, BaseCustomItem item, Runnable onClose) {

    }
}
