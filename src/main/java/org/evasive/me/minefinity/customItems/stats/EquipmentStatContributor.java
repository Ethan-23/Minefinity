package org.evasive.me.minefinity.customItems.stats;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.EquipmentSlotComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StatsComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.playerdata.stats.StatContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contributes the stats granted by a player's equipped custom items (armour + held pickaxe).
 * Owned by customItems; registered with playerdata's stat registry at startup.
 */
public class EquipmentStatContributor implements StatContributor {

    @Override
    public Map<Stats, Integer> contribute(Player player) {
        Map<Stats, Integer> statsMap = new HashMap<>();

        PlayerInventory inventory = player.getInventory();

        List<EquipmentSlot> slots = List.of(EquipmentSlot.HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        List<ItemStack> equipmentItems = new ArrayList<>();
        equipmentItems.add(inventory.getItemInMainHand());
        equipmentItems.add(inventory.getHelmet());
        equipmentItems.add(inventory.getChestplate());
        equipmentItems.add(inventory.getLeggings());
        equipmentItems.add(inventory.getBoots());

        for (int i = 0; i < equipmentItems.size(); i++) {
            ItemStack item = equipmentItems.get(i);
            if (item == null) continue;

            String itemId = CustomItemRegistryService.get().getItemId(item);
            if (itemId == null) continue;

            BaseCustomItem baseCustomItem = CustomItemRegistryService.get().getRegisteredBaseItem(equipmentItems.get(i));
            EquipmentSlot equipmentSlot = slots.get(i);
            if (baseCustomItem == null || !baseCustomItem.getComponent(EquipmentSlotComponent.class).getValue().contains(equipmentSlot))
                continue;

            Map<String, Integer> equipmentStatMap = baseCustomItem.getComponent(StatsComponent.class).getValue();

            if (baseCustomItem instanceof BasePickaxeItem basePickaxeItem) {
                ToolItemData pickaxeData = new ToolItemData(basePickaxeItem);
                equipmentStatMap = basePickaxeItem.getTotalStats(pickaxeData.getPickaxeParts());
            }

            if (equipmentStatMap == null) continue;

            for (Map.Entry<String, Integer> entry : equipmentStatMap.entrySet()) {
                statsMap.merge(Stats.valueOf(entry.getKey()), entry.getValue(), Integer::sum);
            }
        }
        return statsMap;
    }
}
