package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ACCEPTABLE_TOOLS_KEY;

public class AcceptableToolsComponent implements ItemComponent, EditableComponent<Set<CustomItemType>> {

    private Set<CustomItemType> acceptableItemTypes = new HashSet<>();

    private static final String SECTION_ID = "acceptable-tools";

    @Override
    public void load(PersistentDataContainer pdc) {
        this.acceptableItemTypes = new HashSet<>();

        if (!pdc.has(ACCEPTABLE_TOOLS_KEY))
            return;

        String joined = pdc.get(ACCEPTABLE_TOOLS_KEY, PersistentDataType.STRING);
        if (joined == null || joined.isEmpty())
            return;

        for (String type : joined.split(";;")) {
            try {
                acceptableItemTypes.add(CustomItemType.valueOf(type));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.addPersistentDataContainer(
                ACCEPTABLE_TOOLS_KEY,
                PersistentDataType.STRING,
                acceptableItemTypes.stream().map(Enum::name).collect(Collectors.joining(";;"))
        );
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Set<CustomItemType> value) {
        acceptableItemTypes = value == null ? new HashSet<>() : value;
    }

    @Override
    public Set<CustomItemType> getValue() {
        return acceptableItemTypes;
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.openSelector(CustomItemType.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(CustomItemType type) {
                CustomItemBuilder icon = new CustomItemBuilder(type.getDisplayMaterial(), TextConversions.formatItemName(type.name()));
                icon.addLore("<gray>Click to toggle");
                if (acceptableItemTypes.contains(type))
                    icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(CustomItemType type, ClickType click, OptionsGui<CustomItemType> gui) {
                if (!acceptableItemTypes.remove(type)) {
                    acceptableItemTypes.add(type);
                }
            }
        });
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (!acceptableItemTypes.isEmpty()) {
            s.set(SECTION_ID, acceptableItemTypes.stream().map(Enum::name).toList());
        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        this.acceptableItemTypes = new HashSet<>();
        for (String type : s.getStringList(SECTION_ID)) {
            try {
                acceptableItemTypes.add(CustomItemType.valueOf(type));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
