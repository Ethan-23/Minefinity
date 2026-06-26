package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.STATS_KEY;

public class StatsComponent implements ItemComponent, EditableComponent<Map<String, Integer>> {

    private Map<String, Integer> statsMap;

    @Override
    public void load(PersistentDataContainer pdc) {
        this.statsMap = new HashMap<>();

        if (pdc.has(STATS_KEY)) {

            String mapJson = pdc.get(STATS_KEY, PersistentDataType.STRING);

            if (mapJson != null && !mapJson.isEmpty()) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Integer>>() {}.getType();

                Map<String, Integer> parsed = gson.fromJson(mapJson, type);
                if (parsed != null) {
                    this.statsMap.putAll(parsed);
                }
            }
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        Gson gson = new Gson();
        String json = gson.toJson(statsMap);
        builder.addPersistentDataContainer(STATS_KEY, PersistentDataType.STRING, json);

    }

    @Override
    public void addLore(List<String> lore) {
        if(statsMap == null || statsMap.isEmpty())
            return;

        for(Stats stats : Stats.values()){
            if(!statsMap.containsKey(stats.name()))
                continue;
            int value = statsMap.get(stats.name());
            lore.add(stats.getDisplay() + ": " + (value < 0 ? "<red>" : "") + value);
        }
    }

    @Override
    public Class<?> type() {
        return Map.class;
    }

    @Override
    public void setValue(Map<String, Integer> value) {
        this.statsMap = value;
    }

    @Override
    public Map<String, Integer> getValue() {
        return statsMap;
    }

    public int getStatAmount(Stats stats){
        if(!statsMap.containsKey(stats.name()))
            return 0;
        return this.statsMap.get(stats.name());
    }

    public void addStatsMap(Stats stats, Integer value) {
        String statId = stats.name();
        if(!statsMap.containsKey(statId) || value != 0){
            statsMap.put(statId, value);
        }else {
            statsMap.remove(statId);
        }

    }
}
