package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.core.utils.TextConversions.buildItemRarity;
import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BaseCustomItem {

    //Main
    private String id;
    private Material material;
    private String displayName;
    private Rarity rarity;
    private CustomItemType itemType;

    private final List<ItemComponent> components = new ArrayList<>();

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity) {
        this(id, material, displayName, rarity, CustomItemType.CUSTOM_ITEM);
    }

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity, CustomItemType itemType) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.rarity = rarity;
        this.itemType = itemType;
        registerComponents();
    }

    public BaseCustomItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.material = itemStack.getType();
        this.id = getOrDefault(pdc, ITEM_ID_KEY, PersistentDataType.STRING, material.name());
        this.displayName = getOrDefault(pdc, DISPLAY_NAME_KEY, PersistentDataType.STRING, TextConversions.formatItemName(this.id));
        this.rarity = parseEnum(Rarity.class, pdc.get(ITEM_RARITY_KEY, PersistentDataType.STRING), Rarity.MINOR);
        this.itemType = parseEnum(CustomItemType.class, pdc.get(ITEM_TYPE_KEY, PersistentDataType.STRING), CustomItemType.CUSTOM_ITEM);

        registerComponents();

        for (ItemComponent component : components) {
            component.load(pdc);
        }
    }

    protected void registerComponents() {
        addComponent(new FlavorTextComponent());
        addComponent(new ValueComponent());
        addComponent(new GlowComponent());
        addComponent(new VisualMaterialComponent());
        addComponent(new StackSizeComponent());
        addComponent(new SoulboundComponent());
        addComponent(new StatsComponent());
        addComponent(new EquipmentSlotComponent());
    }

    public void addComponent(ItemComponent component) {
        components.add(component);
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String name, E fallback) {
        if (name == null) return fallback;
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    public <T> T getOrDefault(PersistentDataContainer pdc, NamespacedKey key, PersistentDataType<?, T> type, T def) {
        T value = pdc.get(key, type);
        return value == null ? def : value;
    }

    public String getID() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public CustomItemType getCustomItemType() {
        return itemType;
    }

    public void setItemType(CustomItemType itemType) {
        this.itemType = itemType;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected String getName() {
        return buildRarityColor(this.id, this.rarity);
    }

    public ItemStack buildItem() {
        CustomItemBuilder builder = new CustomItemBuilder(this.id, this.material, this.displayName);
        builder.setItemId(this.id);
        builder.setItemRarity(this.rarity);
        builder.setItemType(this.itemType);
        builder.setDisplayName(this.displayName);

        for (ItemComponent component : components) {
            component.save(builder);
        }

        List<String> lore = getLore();
        lore.add("");
        lore.add(buildItemRarity(this.rarity, this.itemType.name()));
        builder.setLore(lore);

        return builder.build();
    }

    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();

        for (ItemComponent component : components) {
            component.addLore(lore);
        }

        if (this.itemType == CustomItemType.CUSTOM_ITEM) {
            lore.add("<red>This is an unregistered CustomItem");
            lore.add("<red>Please report where you got this item");
            lore.add("");
        }

        lore.removeIf(Objects::isNull);
        return lore;
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(Class<T> clazz) {
        return (T) components.stream()
                .filter(clazz::isInstance)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T, C extends ItemComponent> T getComponentValue(Class<C> clazz, T fallback) {
        C component = getComponent(clazz);
        if (component instanceof EditableComponent<?> editable) {
            Object value = editable.getValue();
            if (value != null) {
                return (T) value;
            }
        }
        return fallback;
    }

    public List<ItemComponent> getComponents() {
        return components;
    }

    public BaseCustomItem getBaseItem() {
        return this;
    }

    public BaseCustomItem copy() {
        return new BaseCustomItem(this.buildItem());
    }
}
