package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.types.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.TOOL_PARTS_KEY;

public class ToolPartComponent implements ItemComponent, EditableComponent<Map<PartSlots, String>> {

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

    private Map<PartSlots, String> partMap = new LinkedHashMap<>();

    private static final String SECTION_ID = "parts";

    @Override
    public void load(PersistentDataContainer pdc) {
        this.partMap = new LinkedHashMap<>();

        if (!pdc.has(TOOL_PARTS_KEY, PersistentDataType.STRING))
            return;

        String json = pdc.get(TOOL_PARTS_KEY, PersistentDataType.STRING);
        if (json == null || json.isEmpty())
            return;

        Map<String, String> loaded = GSON.fromJson(json, MAP_TYPE);
        if (loaded == null)
            return;

        // Keys are read as raw strings and mapped through fromString, so unknown/legacy slot names
        // (e.g. a pre-refactor PICKAXE_HEAD) are dropped instead of deserialising to a null key.
        loaded.forEach((key, partId) -> {
            PartSlots slot = PartSlots.fromString(key);
            if (slot != null)
                setPart(slot, partId);
        });
    }

    @Override
    public void save(CustomItemBuilder builder) {
        builder.addPersistentDataContainer(TOOL_PARTS_KEY, PersistentDataType.STRING, GSON.toJson(partMap));
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(Map<PartSlots, String> value) {
        this.partMap = value == null ? new LinkedHashMap<>() : value;
    }

    @Override
    public Map<PartSlots, String> getValue() {
        return this.partMap;
    }

    public void setPart(PartSlots slot, String partId) {
        if (partId == null || partId.isEmpty()) {
            partMap.remove(slot);
        } else {
            partMap.put(slot, partId);
        }
    }

    public String getPart(PartSlots slot) {
        return partMap.get(slot);
    }

    @Override
    public void openEditor(EditContext ctx) {
        BaseToolItem tool = (ctx.item() instanceof BaseToolItem t) ? t : null;
        List<PartSlots> slots = tool != null ? tool.getToolSlots() : List.of(PartSlots.values());
        CustomItemType toolType = tool != null ? tool.getCustomItemType() : null;

        ctx.openSelector(slots, new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(PartSlots slot) {
                String id = partMap.get(slot);
                CustomItemBuilder icon = new CustomItemBuilder(Material.IRON_INGOT, TextConversions.formatItemName(slot.name()));
                icon.addLore(id != null ? "<white>Part: <yellow>" + id : "<red>Empty");
                icon.addLore("<gray>Click to choose a part");
                if (id != null)
                    icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(PartSlots slot, ClickType click, OptionsGui<PartSlots> gui) {
                openPartSelector(ctx, slot, toolType, gui);
            }
        });
    }

    /**
     * Second-level selector: lists every registered part that fits this slot on this tool type (via
     * {@code getCompatibleParts}), plus an "Empty" option that clears the slot. Returns to the slot
     * selector after a choice.
     */
    private void openPartSelector(EditContext ctx, PartSlots slot, CustomItemType toolType, OptionsGui<PartSlots> slotGui) {
        List<BasePartItem> options = new ArrayList<>();
        options.add(null); // sentinel: "Empty" / remove the installed part
        if (toolType != null)
            options.addAll(CustomItemRegistryService.get().getCompatibleParts(slot, toolType));

        ctx.openSelector(options, new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(BasePartItem part) {
                if (part == null) {
                    return new CustomItemBuilder(Material.BARRIER, "<red>Empty")
                            .addLore("<gray>Click to remove the installed part")
                            .build();
                }
                CustomItemBuilder icon = new CustomItemBuilder(part.buildItem());
                icon.addLore("<gray>Click to install");
                if (part.getID().equals(partMap.get(slot)))
                    icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(BasePartItem part, ClickType click, OptionsGui<BasePartItem> gui) {
                setPart(slot, part == null ? null : part.getID());
                slotGui.reopenSelf();
            }
        }, slotGui::reopenSelf);
    }

    @Override
    public boolean isInstanceData() {
        return true;
    }

    @Override
    public void saveToConfig(ConfigurationSection s) {
        if (!partMap.isEmpty()) {
            ConfigurationSection partsSection = s.createSection(SECTION_ID);
            partMap.forEach((slot, partId) -> partsSection.set(slot.name(), partId));
        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection s) {
        this.partMap = new LinkedHashMap<>();
        ConfigurationSection partsSection = s.getConfigurationSection(SECTION_ID);
        if (partsSection == null)
            return;
        for (String key : partsSection.getKeys(false)) {
            PartSlots slot = PartSlots.fromString(key);
            if (slot == null)
                continue;
            setPart(slot, partsSection.getString(key));
        }
    }
}
