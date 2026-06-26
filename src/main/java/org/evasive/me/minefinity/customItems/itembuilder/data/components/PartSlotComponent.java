package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.PART_SLOT_KEY;

public class PartSlotComponent implements ItemComponent, EditableComponent<Set<PartSlots>> {

    private Set<PartSlots> partSlots;

    @Override
    public void load(PersistentDataContainer pdc) {

        String partSlotData = pdc.get(PART_SLOT_KEY, PersistentDataType.STRING);

        if (partSlotData != null && !partSlotData.isEmpty()) {

            this.partSlots = Arrays.stream(partSlotData.split(";;"))
                    .map(slot -> {
                        try {
                            return PartSlots.valueOf(slot);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void save(ItemBuilder builder) {

        String joinedToolSlots = partSlots.stream()
                .map(Enum::name)
                .collect(Collectors.joining(";;"));

        builder.addPersistentDataContainer(PART_SLOT_KEY, PersistentDataType.STRING, joinedToolSlots);

    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public Class<?> type() {
        return PartSlots.class;
    }

    @Override
    public void setValue(Set<PartSlots> value) {
        this.partSlots = value;
    }

    @Override
    public Set<PartSlots> getValue() {
        return this.partSlots;
    }
}
