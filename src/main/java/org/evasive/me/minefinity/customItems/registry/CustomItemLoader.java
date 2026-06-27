package org.evasive.me.minefinity.customItems.registry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.*;
import org.evasive.me.minefinity.customItems.registry.config.ItemRegistryConfigManager;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

            ConfigurationSection componentsSection = customItemSection.getConfigurationSection("components");

            baseCustomItem.setItemType(customItemType);
            baseCustomItem.getComponent(StatsComponent.class).setValue(statsMap);
            baseCustomItem.getComponent(EquipmentSlotComponent.class).setValue(equipmentSlotSet);

            if(customItemSection.get("sell-value") != null)
                baseCustomItem.getComponent(ValueComponent.class).setValue((float) customItemSection.getDouble("sell-value"));
            if(customItemSection.get("visual-material") != null)
                baseCustomItem.getComponent(VisualMaterialComponent.class).setValue(Material.valueOf(customItemSection.getString("visual-material")));
            if(customItemSection.get("flavor-text") != null)
                baseCustomItem.getComponent(FlavorTextComponent.class).setValue(customItemSection.getString("flavor-text"));
            if(customItemSection.get("glowing") != null)
                baseCustomItem.getComponent(GlowComponent.class).setValue(customItemSection.getBoolean("glowing"));
            if(customItemSection.get("soulbound") != null)
                baseCustomItem.getComponent(SoulboundComponent.class).setValue(customItemSection.getBoolean("soulbound"));
            if(customItemSection.get("stack-size") != null)
                baseCustomItem.getComponent(StackSizeComponent.class).setValue(customItemSection.getInt("stack-size"));


            if(baseCustomItem instanceof BaseToolItem toolItem){

                //Need to update to a map under a different section so you can create any amount of parts instead of set names on set types
                if(componentsSection != null){
                    for(String key : componentsSection.getKeys(false)) {
                        PartSlots component = PartSlots.fromString(key);
                        toolItem.setPart(component, componentsSection.getString(key));
                    }
                }

            }else if(baseCustomItem instanceof BasePartItem partItem){
                //Change name to abilities to allow anything to have abilities instead of just pickaxe parts
                for (String string : customItemSection.getStringList("pickaxe-abilities")) {
                    partItem.abilityComponent().toggle(string);
                }

                for (String string : customItemSection.getStringList("component-slot")) {
                    PartSlots toolComponent = PartSlots.fromString(string);
                    if(toolComponent == null)
                        continue;
                    partItem.slotComponent().getValue().add(toolComponent);
                }
            }else if(baseCustomItem instanceof BaseFuelItem fuelItem){
                fuelItem.getComponent(FuelAmountComponent.class).setValue(customItemSection.getInt("fuel-amount"));
            }else if(baseCustomItem instanceof BaseBackpackItem backpackItem){
                backpackItem.storageAmountComponent().setValue(customItemSection.getInt("storage-amount"));
                for (String string : customItemSection.getStringList("storage-list")) {
                    backpackItem.storageListComponent().toggle(string);
                }
            }


            customItemRegistry.registerCustomItem(baseCustomItem);
        }
    }
}
