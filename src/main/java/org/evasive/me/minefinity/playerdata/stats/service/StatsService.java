package org.evasive.me.minefinity.playerdata.stats.service;
import org.evasive.me.minefinity.mining.milestones.BlockMilestone;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.EquipmentSlotComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StatsComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.core.data.MilestoneTier;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.playerdata.stats.PlayerDefaults;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;

import java.util.*;
import java.util.stream.Collectors;

public class StatsService {

    private final Map<UUID, EnumMap<Stats, Integer>> cachedStats = new HashMap<>();
    private final PlayerDataService playerDataService;

    public StatsService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    private PlayerData getPlayerData(Player player){
        return playerDataService.getPlayerData(player.getUniqueId());
    }

    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        cachedStats.put(uuid, playerDataService.getPlayerData(uuid).getPlayerStats());

        recalculateStats(player);
    }

    public EnumMap<Stats, Integer> getStats(UUID uuid) {
        return cachedStats.getOrDefault(uuid, PlayerDefaults.BASE);
    }

    public Map<String, Integer> getStringIdStats(UUID uuid) {
        Map<Stats, Integer> stats = cachedStats.getOrDefault(uuid, PlayerDefaults.BASE);

        return stats.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    public void recalculateStats(Player player) {
        UUID uuid = player.getUniqueId();

        //player.sendMessage("CALCULATING STATS");

        PlayerData data = getPlayerData(player);
        if (data == null) return;

        EnumMap<Stats, Integer> result = new EnumMap<>(Stats.class);

        result.putAll(PlayerDefaults.BASE);

        for(var entry: calculateEquipmentStats(player).entrySet() ){
            result.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        for (var entry : data.getPlayerStats().entrySet()) {
            result.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        for (var entry : calculateMilestoneStats(player).entrySet()) {
            result.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        cachedStats.put(uuid, result);
    }

    private Map<Stats, Integer> calculateMilestoneStats(Player player) {
        Map<Stats, Integer> statsMap = new HashMap<>();

        List<BaseBlock> blockList = BlockTypeRegistryService.getInstance().getAllBlocks();

        for (BaseBlock baseBlock : blockList) {
            List<MilestoneTier> milestoneTiers = baseBlock.milestoneUnlocks();
            String blockId = baseBlock.name();
            int tier = playerDataService.getPlayerData(player.getUniqueId()).get(BlockMilestone.class).getTier(blockId);
            for(int i = 0; i <= tier; i++){
                if(i == 0)
                    continue;

                Map<String, Integer> statsStringMap = milestoneTiers.get(i-1).stats();

                for(String stat : statsStringMap.keySet()){
                    Stats currentStat = Stats.valueOf(stat);
                    statsMap.put(currentStat, statsMap.getOrDefault(currentStat, 0) + statsStringMap.get(stat));
                }

            }
        }
        return statsMap;
    }

    private Map<Stats, Integer> calculateEquipmentStats(Player player) {
        Map<Stats, Integer> statsMap = new HashMap<>();

        PlayerInventory inventory = player.getInventory();

        List<EquipmentSlot> slots = List.of(EquipmentSlot.HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        List<ItemStack> equipmentItems = new ArrayList<>();
        equipmentItems.add(inventory.getItemInMainHand());
        equipmentItems.add(inventory.getHelmet());
        equipmentItems.add(inventory.getChestplate());
        equipmentItems.add(inventory.getLeggings());
        equipmentItems.add(inventory.getBoots());

        for(int i = 0; i < equipmentItems.size(); i++) {
            ItemStack item =  equipmentItems.get(i);

            if(item == null) continue;

            String itemId = CustomItemRegistryService.get().getItemId(item);
            if(itemId == null) continue;

            BaseCustomItem baseCustomItem = CustomItemRegistryService.get().getRegisteredBaseItem(equipmentItems.get(i));
            EquipmentSlot equipmentSlot = slots.get(i);
            if(baseCustomItem == null || !baseCustomItem.getComponent(EquipmentSlotComponent.class).getValue().contains(equipmentSlot)) continue;

            Map<String, Integer> equipmentStatMap = baseCustomItem.getComponent(StatsComponent.class).getValue();

            if(baseCustomItem instanceof BasePickaxeItem basePickaxeItem){
                ToolItemData pickaxeData = new ToolItemData(basePickaxeItem);
                equipmentStatMap = basePickaxeItem.getTotalStats(pickaxeData.getPickaxeParts());
            }

            if(equipmentStatMap == null) continue;

            for(Map.Entry<String, Integer> entry : equipmentStatMap.entrySet()) {
                Stats stats = Stats.valueOf(entry.getKey());
                if(!statsMap.containsKey(stats)){
                    statsMap.put(stats, entry.getValue());
                } else {
                    statsMap.put(stats, statsMap.get(stats)+entry.getValue());
                }
            }
        }
        return statsMap;
    }

    public int getStat(UUID uuid, Stats stat) {
        EnumMap<Stats, Integer> map = cachedStats.get(uuid);

        if (map == null) return PlayerDefaults.BASE.getOrDefault(stat, 0);

        return map.getOrDefault(stat, 0);
    }

    public void removePlayer(Player player) {
        cachedStats.remove(player.getUniqueId());
    }

    public void addStats(Player player, Map<Stats, Integer> stats) {
        for(Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            getPlayerData(player).addPlayerStats(entry.getKey(), entry.getValue());
        }
    }

    public void removeStats(Player player, Map<Stats, Integer> stats) {
        for(Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            getPlayerData(player).removePlayerStats(entry.getKey(), entry.getValue());
        }
    }

}
