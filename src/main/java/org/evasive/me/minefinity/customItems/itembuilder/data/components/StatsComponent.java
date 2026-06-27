package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;
import org.evasive.me.minefinity.customItems.itembuilder.gui.OptionsGui;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STATS_KEY;

public class StatsComponent implements ItemComponent, EditableComponent<Map<String, Integer>> {

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Integer>>() {}.getType();

    private Map<String, Integer> statsMap = new HashMap<>();

    @Override
    public void load(PersistentDataContainer pdc) {
        this.statsMap = new HashMap<>();

        if (!pdc.has(STATS_KEY)) return;

        String mapJson = pdc.get(STATS_KEY, PersistentDataType.STRING);
        if (mapJson == null || mapJson.isEmpty()) return;

        Map<String, Integer> parsed = GSON.fromJson(mapJson, MAP_TYPE);
        if (parsed != null) {
            this.statsMap.putAll(parsed);
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(STATS_KEY, PersistentDataType.STRING, GSON.toJson(statsMap));
    }

    @Override
    public void addLore(List<String> lore) {
        if (statsMap.isEmpty()) return;

        for (Stats stat : Stats.values()) {
            Integer value = statsMap.get(stat.name());
            if (value == null) continue;
            lore.add(stat.getDisplay() + ": " + (value < 0 ? "<red>" : "<white>") + value);
        }
    }

    @Override
    public void setValue(Map<String, Integer> value) {
        this.statsMap = value == null ? new HashMap<>() : value;
    }

    @Override
    public Map<String, Integer> getValue() {
        return statsMap;
    }

    public int getStatAmount(Stats stats) {
        return statsMap.getOrDefault(stats.name(), 0);
    }

    /** Sets a stat; a value of 0 removes it. */
    public void setStat(Stats stats, int value) {
        if (value == 0) {
            statsMap.remove(stats.name());
        } else {
            statsMap.put(stats.name(), value);
        }
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.openSelector(Stats.values(), new OptionsGui.OptionAdapter<>() {
            @Override
            public ItemStack render(Stats stat) {
                Integer value = statsMap.get(stat.name());
                ItemBuilder icon = new ItemBuilder(stat.getMaterial(), stat.getDisplay());
                icon.addLore(value != null ? "<white>Value: <yellow>" + value : "<red>Not set");
                icon.addLore("<gray>Click to set a value (0 removes)");
                if (value != null) icon.addGlow();
                return icon.build();
            }

            @Override
            public void onClick(Stats stat, ClickType click, OptionsGui<Stats> gui) {
                ctx.promptInt(value -> setStat(stat, value), gui::reopenSelf);
            }
        });
    }
}
