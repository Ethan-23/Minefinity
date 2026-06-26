package org.evasive.me.minefinity.customItems.registry.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.EditableComponent;

import java.util.*;

public class RegistryConfigHandler {

    private final ItemRegistryConfigManager itemRegistryConfigManager;

    public RegistryConfigHandler(ItemRegistryConfigManager itemRegistryConfigManager) {
        this.itemRegistryConfigManager = itemRegistryConfigManager;
    }

    public void saveEntireRegistry(Collection<BaseCustomItem> customItems) {
        customItems.forEach(item -> addSingleEntry((BaseCustomItem) item));
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void addSingleEntry(BaseCustomItem item){
        ConfigurationSection individualItemSection = itemRegistryConfigManager.getItemConfigSection().createSection(item.getID());

        CustomItemType customItemType = item.getCustomItemType();

        individualItemSection.set("material", item.getMaterial().name());
        individualItemSection.set("display-name", item.getDisplayName());
        individualItemSection.set("custom-item-type", customItemType.name());
        individualItemSection.set("rarity", item.getRarity().name());

        List<ItemComponent> itemComponentList = item.getComponents();

        for(ItemComponent itemComponent : itemComponentList){
            if(itemComponent instanceof EditableComponent<?> editableComponent)
                individualItemSection.set(itemComponent.getClass().getName().toLowerCase(Locale.ROOT), editableComponent.getValue());
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
