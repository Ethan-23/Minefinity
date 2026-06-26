package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.*;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

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

    public void addComponent(ItemComponent component) {
        components.add(component);
    }

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.rarity = rarity;
        this.itemType = CustomItemType.CUSTOM_ITEM;
    }

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity, CustomItemType itemType) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.rarity = rarity;
        this.itemType = itemType;
    }

    public BaseCustomItem(ItemStack itemStack) {

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.material = itemStack.getType();
        this.id = getOrDefault(pdc, ITEM_ID_KEY, PersistentDataType.STRING, material.name());
        this.displayName = getOrDefault(pdc, DISPLAY_NAME_KEY, PersistentDataType.STRING, TextConversions.formatItemName(this.id));
        this.rarity = Rarity.valueOf(getOrDefault(pdc, ITEM_RARITY_KEY, PersistentDataType.STRING, Rarity.MINOR.name()));
        this.itemType = CustomItemType.valueOf(getOrDefault(pdc, ITEM_TYPE_KEY, PersistentDataType.STRING, CustomItemType.CUSTOM_ITEM.name()));

        registerDefaultComponents();

        for(ItemComponent component : components) {
            component.load(pdc);
        }

    }

    private void registerDefaultComponents() {
        components.add(new FlavorTextComponent());
        components.add(new ValueComponent());
        components.add(new GlowComponent());
        components.add(new VisualMaterialComponent());
        components.add(new StackSizeComponent());
        components.add(new SoulboundComponent());
    }

    public <T> T getOrDefault(PersistentDataContainer pdc, NamespacedKey key, PersistentDataType<?,T> type, T def){
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

        ItemBuilder itemBuilder = new ItemBuilder(this.id, this.material, this.displayName);

        itemBuilder.setItemRarity(this.rarity);
        itemBuilder.setItemType(this.itemType);

        List<String> lore = getLore();

        for(ItemComponent component : components) {
            component.save(itemBuilder);
        }

        lore.add("");
        lore.add(buildItemRarity(this.rarity, this.itemType));

        return new ItemBuilder(this.id, this.material, this.displayName)
                .addLore(lore)
                .setItemId(this.id)
                .setItemRarity(this.rarity)
                .setDisplayName(this.displayName)
                .setItemType(this.itemType)
                .build();
    }



    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();

        if(!lore.isEmpty())
            lore.add("");

        //Make set order since lore comes in this way aswell
        for(ItemComponent component : components) {
            component.addLore(lore);
        }

        if(this.getCustomItemType() == CustomItemType.CUSTOM_ITEM){
            lore.add("<red>This is an unregistered CustomItem");
            lore.add("<red>Please report where you got this item");
            lore.add("");
        }

        return lore;
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(Class<T> clazz) {
        return (T) components.stream()
                .filter(clazz::isInstance)
                .findFirst()
                .orElse(null);
    }

    public List<ItemComponent> getComponents() {
        return components;
    }

    public BaseCustomItem getBaseItem() {
        return this;
    }

    public BaseCustomItem copy(){
        return new BaseCustomItem(this.buildItem());
    }
}
