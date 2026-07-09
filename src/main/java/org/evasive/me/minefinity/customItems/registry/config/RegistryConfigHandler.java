package org.evasive.me.minefinity.customItems.registry.config;

import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseCustomItem;

import java.util.Collection;

public class RegistryConfigHandler {

    private final ItemRegistryConfigManager itemRegistryConfigManager;
    private static final String MATERIAL = "material";
    private static final String DISPLAY_NAME = "display-name";
    private static final String TYPE = "custom-item-type";
    private static final String RARITY = "rarity";

    public RegistryConfigHandler(ItemRegistryConfigManager itemRegistryConfigManager) {
        this.itemRegistryConfigManager = itemRegistryConfigManager;
    }

    public void saveEntireRegistry(Collection<BaseCustomItem> customItems) {
        customItems.forEach(this::addSingleEntry);
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void addSingleEntry(BaseCustomItem item) {
        ConfigurationSection section = itemRegistryConfigManager.getItemConfigSection().createSection(item.getID());

        section.set(MATERIAL, item.getMaterial().name());
        section.set(DISPLAY_NAME, item.getDisplayName());
        section.set(TYPE, item.getCustomItemType().name());
        section.set(RARITY, item.getRarity().name());

        for (ItemComponent component : item.getComponents()) {
            component.saveToConfig(section);
        }

        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void removeSingleEntry(BaseCustomItem item){
        if(!itemRegistryConfigManager.getItemConfigSection().isConfigurationSection(item.getID()))
            return;
        itemRegistryConfigManager.getItemConfigSection().set(item.getID(), null);
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

}
