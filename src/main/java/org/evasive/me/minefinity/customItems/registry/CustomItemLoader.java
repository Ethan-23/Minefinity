package org.evasive.me.minefinity.customItems.registry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.registry.CustomItemRegistry;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.customItems.itembuilder.data.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.registry.config.ItemRegistryConfigManager;

import java.util.*;
import java.util.stream.Collectors;

public class CustomItemLoader {

    CustomItemRegistry customItemRegistry;

    public CustomItemLoader(CustomItemRegistry customItemRegistry) {
        this.customItemRegistry = customItemRegistry;
    }

    public void registerConfigItems(ItemRegistryConfigManager itemRegistryConfigManager) {

        ConfigurationSection itemsSection = itemRegistryConfigManager
                .getItemRegistryConfig()
                .getConfigurationSection("items");

        if(itemsSection == null) return;

        for(String itemID : itemsSection.getKeys(false)) {
            ConfigurationSection customItemSection = itemsSection.getConfigurationSection(itemID);

            if(customItemSection == null) continue;

            Material material = Material.valueOf(customItemSection.getString("material"));
            String displayName = customItemSection.getString("display-name");
            Rarity rarity = Rarity.valueOf(customItemSection.getString("rarity"));

            Set<EquipmentSlot> equipmentSlotSet =
                    customItemSection.getStringList("equipment-slot").stream()
                            .map(EquipmentSlot::valueOf)
                            .collect(Collectors.toSet());

            Map<String, Integer> statsMap = new HashMap<>();

            ConfigurationSection statsSection = customItemSection.getConfigurationSection("stats");

            if (statsSection != null) {
                for (String key : statsSection.getKeys(false)) {
                    try {
                        Stats stat = Stats.valueOf(key); // convert String → Enum
                        int value = statsSection.getInt(key);

                        statsMap.put(stat.name(), value);
                    } catch (IllegalArgumentException e) {
                        // Optional: handle invalid stat names in config
                        Bukkit.getLogger().warning("Invalid stat found: " + key);
                    }
                }
            }



            CustomItemType customItemType = CustomItemType.valueOf(customItemSection.getString("custom-item-type"));
            BaseCustomItem baseCustomItem = customItemType.create(itemID, material, displayName, rarity);

            baseCustomItem.setStatsMap(statsMap);
            baseCustomItem.setEquipmentSlots(equipmentSlotSet);
            baseCustomItem.setItemType(customItemType);

            if(customItemSection.get("sell-value") != null)
                baseCustomItem.setValue((float) customItemSection.getDouble("sell-value"));
            if(customItemSection.get("visual-material") != null)
                baseCustomItem.setVisualMaterial(Material.valueOf(customItemSection.getString("visual-material")));
            if(customItemSection.get("flavor-text") != null)
                baseCustomItem.setFlavorText(customItemSection.getString("flavor-text"));
            if(customItemSection.get("glowing") != null)
                baseCustomItem.setGlowing(customItemSection.getBoolean("glowing"));
            if(customItemSection.get("soulbound") != null)
                baseCustomItem.setSoulbound(customItemSection.getBoolean("soulbound"));
            if(customItemSection.get("stack-size") != null)
                baseCustomItem.setStackSize(customItemSection.getInt("stack-size"));


            if(baseCustomItem instanceof BasePickaxeItem){
                ((BasePickaxeItem) baseCustomItem).setPickaxeHeadId(customItemSection.getString("pickaxe-head"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeCoreId(customItemSection.getString("pickaxe-core"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeHandleId(customItemSection.getString("pickaxe-handle"));
            }else if(baseCustomItem instanceof BasePickaxeComponent){
                for (String string : customItemSection.getStringList("pickaxe-abilities")) {
                    ((BasePickaxeComponent) baseCustomItem).changePickaxeAbilityList(string);
                }
            }else if(baseCustomItem instanceof BaseFuelItem){
                ((BaseFuelItem) baseCustomItem).setFuelAmount(customItemSection.getInt("fuel-amount"));
            }else if(baseCustomItem instanceof BaseBackpackItem){
                ((BaseBackpackItem) baseCustomItem).setStoredItemAmount(customItemSection.getInt("storage-amount"));
                for (String string : customItemSection.getStringList("storage-list")) {
                    ((BaseBackpackItem) baseCustomItem).changeStoredItemIdList(string);
                }
            }


            customItemRegistry.registerCustomItem(baseCustomItem);
        }
    }
}
