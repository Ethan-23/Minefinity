package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.CustomItemType;

public enum ItemOptions {

    MINEFINITY_ID("Minefinity ID", Material.NAME_TAG, String.class),
    MATERIAL("Material", Material.CRAFTING_TABLE, Material.class),
    DISPLAY_NAME("Display Name", Material.WRITABLE_BOOK, String.class),
    CUSTOM_ITEM_TYPE("Item Type", Material.KNOWLEDGE_BOOK, CustomItemType.class),
    RARITY("Rarity", Material.GRAY_DYE, Rarity.class),

    STATS("Stats", Material.GRINDSTONE, StatsComponent.class),
    EQUIPMENT_SLOT("Equipment Slots", Material.NETHERITE_HELMET, EquipmentSlotComponent.class),
    TOOL_PARTS("Tool Parts", Material.IRON_INGOT, ToolPartComponent.class),
    PART_SLOT("Part Slots", Material.ACACIA_BOAT, PartSlotComponent.class),
    PART_ABILITY("Part Abilities", Material.WIND_CHARGE, PartAbilityComponent.class),
    ACCEPTABLE_TOOLS("Acceptable Tools", Material.SMITHING_TABLE, AcceptableToolsComponent.class),

    FUEL_AMOUNT("Fuel Amount", Material.CHARCOAL, FuelAmountComponent.class),
    STORAGE_AMOUNT("Storage Amount", Material.CHEST, StorageAmountComponent.class),
    STORAGE_LIST("Storage List", Material.PAPER, StorageListComponent.class),

    SELL_VALUE("Sell Value", Material.GOLD_INGOT, ValueComponent.class),
    VISUAL_MATERIAL("Visual Material", Material.SPYGLASS, VisualMaterialComponent.class),
    FLAVOR_LORE("Flavor Lore", Material.ITEM_FRAME, FlavorTextComponent.class),
    GLOWING("Glowing", Material.ENCHANTING_TABLE, GlowComponent.class),
    STACK_SIZE("Stack Size", Material.HOPPER, StackSizeComponent.class),
    SOULBOUND("Soulbound", Material.SOUL_CAMPFIRE, SoulboundComponent.class);

    private final String displayName;
    private final Material icon;
    private final Class<?> optionClass;

    ItemOptions(String displayName, Material icon, Class<?> optionClass) {
        this.displayName = displayName;
        this.icon = icon;
        this.optionClass = optionClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public boolean isComponent() {
        return ItemComponent.class.isAssignableFrom(optionClass);
    }

    @SuppressWarnings("unchecked")
    public Class<? extends ItemComponent> asComponentClass() {
        return (Class<? extends ItemComponent>) optionClass;
    }

    public boolean isString() {
        return optionClass == String.class;
    }

    public boolean isMaterial() {
        return optionClass == Material.class;
    }

    public boolean isEnum() {
        return optionClass.isEnum();
    }
}