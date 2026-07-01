package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.TOOL_PARTS_KEY;

public class ToolPartComponent implements ItemComponent, EditableComponent<Map<PartSlots, String>> {

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<PartSlots, String>>() {}.getType();

    private Map<PartSlots, String> partMap = new LinkedHashMap<>();

    @Override
    public void load(PersistentDataContainer pdc) {
        this.partMap = new LinkedHashMap<>();

        if (!pdc.has(TOOL_PARTS_KEY, PersistentDataType.STRING)) return;

        String json = pdc.get(TOOL_PARTS_KEY, PersistentDataType.STRING);
        if (json == null || json.isEmpty()) return;

        Map<PartSlots, String> loaded = GSON.fromJson(json, MAP_TYPE);
        if (loaded != null) {
            this.partMap = new LinkedHashMap<>(loaded);
        }
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
        PartSlots[] slots = (ctx.item() instanceof BaseToolItem tool)
                ? tool.getToolSlots().toArray(new PartSlots[0])
                : PartSlots.values();

        ctx.openSelector(slots, new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(PartSlots slot) {
                String id = partMap.get(slot);
                CustomItemBuilder icon = new CustomItemBuilder(Material.IRON_INGOT, TextConversions.formatItemName(slot.name()));
                icon.addLore(id != null ? "<white>Part: <yellow>" + id : "<red>Empty");
                icon.addLore("<gray>Click to set a part id (blank clears)");
                if (id != null) icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(PartSlots slot, ClickType click, OptionsGui<PartSlots> gui) {
                ctx.promptString(input -> setPart(slot, input.trim()), gui::reopenSelf);
            }
        });
    }
}
