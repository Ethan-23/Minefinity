package org.evasive.me.minefinity.customItems.registry.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.*;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.*;

import java.util.*;

public class RegistryConfigHandler {

    private final ItemRegistryConfigManager itemRegistryConfigManager;

    public RegistryConfigHandler(ItemRegistryConfigManager itemRegistryConfigManager) {
        this.itemRegistryConfigManager = itemRegistryConfigManager;
    }

    public void saveEntireRegistry(Collection<BaseCustomItem> customItems) {
        customItems.forEach(this::addSingleEntry);
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    public void addSingleEntry(BaseCustomItem item){
        ConfigurationSection section = itemRegistryConfigManager.getItemConfigSection().createSection(item.getID());

        section.set("material", item.getMaterial().name());
        section.set("display-name", item.getDisplayName());
        section.set("custom-item-type", item.getCustomItemType().name());
        section.set("rarity", item.getRarity().name());

        writeUniversalComponents(section, item);
        writeTypeSpecificComponents(section, item);

        itemRegistryConfigManager.saveItemRegistryConfig();
    }

    private void writeUniversalComponents(ConfigurationSection section, BaseCustomItem item) {
        Map<String, Integer> stats = item.getComponent(StatsComponent.class).getValue();
        if (!stats.isEmpty()) {
            ConfigurationSection statsSection = section.createSection("stats");
            stats.forEach(statsSection::set);
        }

        Set<EquipmentSlot> equipmentSlots = item.getComponent(EquipmentSlotComponent.class).getValue();
        if (!equipmentSlots.isEmpty()) {
            section.set("equipment-slot", equipmentSlots.stream().map(Enum::name).toList());
        }

        Float sellValue = item.getComponent(ValueComponent.class).getValue();
        if (sellValue != null) section.set("sell-value", sellValue);

        Material visualMaterial = item.getComponent(VisualMaterialComponent.class).getValue();
        if (visualMaterial != null) section.set("visual-material", visualMaterial.name());

        String flavorText = item.getComponent(FlavorTextComponent.class).getValue();
        if (flavorText != null) section.set("flavor-text", flavorText);

        if (Boolean.TRUE.equals(item.getComponent(GlowComponent.class).getValue())) section.set("glowing", true);
        if (Boolean.TRUE.equals(item.getComponent(SoulboundComponent.class).getValue())) section.set("soulbound", true);

        Integer stackSize = item.getComponent(StackSizeComponent.class).getValue();
        if (stackSize != null) section.set("stack-size", stackSize);
    }

    private void writeTypeSpecificComponents(ConfigurationSection section, BaseCustomItem item) {
        if (item instanceof BaseToolItem tool) {
            Map<PartSlots, String> parts = tool.partComponent().getValue();
            if (!parts.isEmpty()) {
                ConfigurationSection partsSection = section.createSection("components");
                parts.forEach((slot, partId) -> partsSection.set(slot.name(), partId));
            }
        } else if (item instanceof BasePartItem part) {
            List<String> abilities = part.abilityComponent().getValue();
            if (!abilities.isEmpty()) section.set("pickaxe-abilities", abilities);

            Set<PartSlots> partSlots = part.slotComponent().getValue();
            if (!partSlots.isEmpty()) section.set("component-slot", partSlots.stream().map(Enum::name).toList());
        } else if (item instanceof BaseFuelItem fuel) {
            section.set("fuel-amount", fuel.getComponent(FuelAmountComponent.class).getValue());
        } else if (item instanceof BaseBackpackItem backpack) {
            section.set("storage-amount", backpack.storageAmountComponent().getValue());

            List<String> storageList = backpack.storageListComponent().getValue();
            if (!storageList.isEmpty()) section.set("storage-list", storageList);
        }
    }

    public void removeSingleEntry(BaseCustomItem item){
        if(!itemRegistryConfigManager.getItemConfigSection().isConfigurationSection(item.getID()))
            return;
        itemRegistryConfigManager.getItemConfigSection().set(item.getID(), null);
        itemRegistryConfigManager.saveItemRegistryConfig();
    }

}
