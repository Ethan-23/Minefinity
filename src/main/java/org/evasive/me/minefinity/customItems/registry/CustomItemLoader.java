package org.evasive.me.minefinity.customItems.registry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemComponent;
import org.evasive.me.minefinity.customItems.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.registry.config.ItemRegistryConfigManager;

public class CustomItemLoader {

    private final CustomItemRegistry customItemRegistry;

    public CustomItemLoader(CustomItemRegistry customItemRegistry) {
        this.customItemRegistry = customItemRegistry;
    }

    public void registerConfigItems(ItemRegistryConfigManager itemRegistryConfigManager) {

        ConfigurationSection itemsSection = itemRegistryConfigManager
                .getItemRegistryConfig()
                .getConfigurationSection("items");

        if (itemsSection == null)
            return;

        for (String itemID : itemsSection.getKeys(false)) {
            ConfigurationSection customItemSection = itemsSection.getConfigurationSection(itemID);
            if (customItemSection == null)
                continue;

            Material material = Material.valueOf(customItemSection.getString("material"));
            String displayName = customItemSection.getString("display-name");
            Rarity rarity = Rarity.valueOf(customItemSection.getString("rarity"));
            CustomItemType customItemType = CustomItemType.valueOf(customItemSection.getString("custom-item-type"));

            // The factory picks the correct subclass and registers that type's components.
            BaseCustomItem baseCustomItem = customItemType.create(itemID, material, displayName, rarity);

            // Each component reads its own keys out of the item's section — no type switch needed.
            for (ItemComponent component : baseCustomItem.getComponents()) {
                component.loadFromConfig(customItemSection);
            }

            customItemRegistry.registerCustomItem(baseCustomItem);
        }
    }
}
