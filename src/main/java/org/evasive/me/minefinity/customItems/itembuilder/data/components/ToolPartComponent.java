package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.TOOL_PARTS_KEY;

public class ToolPartComponent implements ItemComponent, EditableComponent<Map<PartSlots, String>> {

    private static final Gson GSON = new Gson();

    private Map<PartSlots, String> partMap = new HashMap<>();

    @Override
    public void load(PersistentDataContainer pdc) {

        if (!pdc.has(TOOL_PARTS_KEY, PersistentDataType.STRING)) {
            return;
        }

        String json = pdc.get(TOOL_PARTS_KEY, PersistentDataType.STRING);

        if (json == null || json.isEmpty()) {
            return;
        }

        Type type = new TypeToken<Map<PartSlots, String>>() {}.getType();

        Map<PartSlots, String> loadedMap = GSON.fromJson(json, type);

        if (loadedMap != null) {
            this.partMap = loadedMap;
        }
    }

    @Override
    public void save(ItemBuilder builder) {

        String json = GSON.toJson(partMap);

        builder.addPersistentDataContainer(
                TOOL_PARTS_KEY,
                PersistentDataType.STRING,
                json
        );
    }

    @Override
    public void addLore(List<String> lore) {
        lore.add("<pink>HAS PARTS HAHAHAHAH");
    }

    @Override
    public Class<?> type() {
        return Map.class;
    }

    @Override
    public void setValue(Map<PartSlots, String> value) {
        this.partMap = value;
    }

    @Override
    public Map<PartSlots, String> getValue() {
        return this.partMap;
    }
}
