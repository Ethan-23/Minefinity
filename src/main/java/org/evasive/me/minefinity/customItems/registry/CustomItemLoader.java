package org.evasive.me.minefinity.customItems.registry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.registry.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.itembuilder.data.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.registry.config.ItemRegistryConfigManager;

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

            CustomItemType customItemType = CustomItemType.valueOf(customItemSection.getString("custom-item-type"));
            BaseCustomItem baseCustomItem = customItemType.create(itemID, material, displayName, rarity);
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
                ((BasePickaxeItem) baseCustomItem).setBreakingPower(customItemSection.getInt("breaking-power"));
                ((BasePickaxeItem) baseCustomItem).setBaseMiningSpeed((float) customItemSection.getDouble("mining-speed"));
                ((BasePickaxeItem) baseCustomItem).setBaseMiningFortune((float) customItemSection.getDouble("mining-fortune"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeHeadId(customItemSection.getString("pickaxe-head"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeCoreId(customItemSection.getString("pickaxe-core"));
                ((BasePickaxeItem) baseCustomItem).setPickaxeHandleId(customItemSection.getString("pickaxe-handle"));
            }else if(baseCustomItem instanceof BasePickaxeComponent){
                ((BasePickaxeComponent) baseCustomItem).setBreakingPower(customItemSection.getInt("breaking-power"));
                ((BasePickaxeComponent) baseCustomItem).setMiningSpeed((float) customItemSection.getDouble("mining-speed"));
                ((BasePickaxeComponent) baseCustomItem).setMiningFortune((float) customItemSection.getDouble("mining-fortune"));
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
