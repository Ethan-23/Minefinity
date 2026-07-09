package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.PART_SLOT_KEY;

public class PartSlotComponent implements ItemComponent, EditableComponent<Set<PartSlots>> {

    private Set<PartSlots> partSlots = new HashSet<>();

    private static final String SECTION_ID = "part-slot";

    @Override
    public void load(PersistentDataContainer pdc) {
        this.partSlots = new HashSet<>();

        String partSlotData = pdc.get(PART_SLOT_KEY, PersistentDataType.STRING);
        if (partSlotData == null || partSlotData.isEmpty())
            return;

        this.partSlots = Arrays.stream(partSlotData.split(";;"))
                .map(PartSlots::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public void save(CustomItemBuilder builder) {
        String joined = partSlots.stream().map(Enum::name).collect(Collectors.joining(";;"));
        builder.addPersistentDataContainer(PART_SLOT_KEY, PersistentDataType.STRING, joined);
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Set<PartSlots> value) {
        this.partSlots = value == null ? new HashSet<>() : value;
    }

    @Override
    public Set<PartSlots> getValue() {
        return this.partSlots;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.openSelector(PartSlots.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(PartSlots slot) {
                CustomItemBuilder icon = new CustomItemBuilder(Material.PAPER, TextConversions.formatItemName(slot.name()));
                icon.addLore("<gray>Click to toggle");
                if (partSlots.contains(slot))
                    icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(PartSlots slot, ClickType click, OptionsGui<PartSlots> gui) {
                if (!partSlots.remove(slot)) {
                    partSlots.add(slot);
                }
            }
        });
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (!partSlots.isEmpty())
            s.set(SECTION_ID, partSlots.stream().map(Enum::name).toList());
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        this.partSlots = new HashSet<>();
        for (String slot : s.getStringList(SECTION_ID)) {
            PartSlots parsed = PartSlots.fromString(slot);
            if (parsed != null)
                partSlots.add(parsed);
        }
    }
}
