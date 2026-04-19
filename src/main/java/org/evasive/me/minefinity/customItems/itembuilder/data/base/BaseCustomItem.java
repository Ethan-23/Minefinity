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
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.core.utils.TextConversions.*;

public class BaseCustomItem implements CustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.STATS,
            ItemOptions.EQUIPMENT_SLOT
    );

    private static final List<ItemOptions> optionalOptions = List.of(
            ItemOptions.SELL_VALUE,
            ItemOptions.VISUAL_MATERIAL,
            ItemOptions.FLAVOR_LORE,
            ItemOptions.GLOWING,
            ItemOptions.STACK_SIZE,
            ItemOptions.SOULBOUND
    );

    private String id;
    private Material material;
    private String displayName;
    private Rarity rarity;
    private CustomItemType itemType;
    private Map<String, Integer> statsMap;
    private Set<EquipmentSlot> equipmentSlots;

    // Optionals
    private Float value;
    private Material visualMaterial;
    private String flavorText;
    private Boolean glowing;
    private Integer stackSize;
    private Boolean soulbound;


    public BaseCustomItem(ItemStack itemStack) {

        this.material = itemStack.getType();

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.id = getOrDefault(pdc, ITEM_ID_KEY, PersistentDataType.STRING, material.name());
        this.displayName = getOrDefault(pdc, DISPLAY_NAME_KEY, PersistentDataType.STRING, TextConversions.formatItemName(this.id));
        this.rarity = Rarity.valueOf(getOrDefault(pdc, ITEM_RARITY_KEY, PersistentDataType.STRING, Rarity.MINOR.name()));
        this.itemType = CustomItemType.valueOf(getOrDefault(pdc, ITEM_TYPE_KEY, PersistentDataType.STRING, CustomItemType.CUSTOM_ITEM.name()));
        this.glowing = hasOrDefault(pdc, GLOWING_KEY) ? true : null;
        this.stackSize = getOrDefault(pdc, STACK_SIZE_KEY, PersistentDataType.INTEGER, null);
        this.soulbound = hasOrDefault(pdc, SOULBOUND_KEY) ? true : null;
        this.flavorText = getOrDefault(pdc, FLAVOR_TEXT_KEY, PersistentDataType.STRING, null);
        this.value = getOrDefault(pdc, VALUE_KEY, PersistentDataType.FLOAT, null);

        if (pdc.has(EQUIPMENT_SLOT_KEY)) {

            String joined = pdc.get(EQUIPMENT_SLOT_KEY, PersistentDataType.STRING);

            this.equipmentSlots = new HashSet<>();

            if (joined != null && !joined.isEmpty()) {
                for (String slot : joined.split(";;")) {
                    try {
                        equipmentSlots.add(EquipmentSlot.valueOf(slot));
                    } catch (IllegalArgumentException e) {
                        Bukkit.getConsoleSender().sendMessage("Invalid equipment slot: " + slot);
                    }
                }
            }

        } else {
            this.equipmentSlots = new HashSet<>();
        }

        this.statsMap = new HashMap<>();

        if (pdc.has(STATS_KEY)) {

            String mapJson = pdc.get(STATS_KEY, PersistentDataType.STRING);

            if (mapJson != null && !mapJson.isEmpty()) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Integer>>() {}.getType();

                Map<String, Integer> parsed = gson.fromJson(mapJson, type);
                if (parsed != null) {
                    this.statsMap.putAll(parsed);
                }
            }
        }


        //
        String materialName = getOrDefault(pdc, VISUAL_ID_KEY, PersistentDataType.STRING, null);
        this.visualMaterial = materialName == null ? null : Material.getMaterial(materialName);

    }

    public <T> T getOrDefault(PersistentDataContainer pdc, NamespacedKey key, PersistentDataType<?,T> type, T def){
        T value = pdc.get(key, type);
        return value == null ? def : value;
    }

    private static boolean hasOrDefault(PersistentDataContainer pdc, NamespacedKey key){
        return pdc.has(key);
    }

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.rarity = rarity;
        this.itemType = CustomItemType.CUSTOM_ITEM;
        this.equipmentSlots = new HashSet<>();
        this.statsMap = new HashMap<>();
    }

    public BaseCustomItem(String id, Material material, String displayName, Rarity rarity, CustomItemType itemType) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.rarity = rarity;
        this.itemType = itemType;
        this.equipmentSlots = new HashSet<>();
        this.statsMap = new HashMap<>();
    }

    public Material getMaterial() {
        return material;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public CustomItemType getCustomItemType() {
        return itemType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setItemType(CustomItemType itemType) {
        this.itemType = itemType;
    }

    protected String getName() {
        return buildRarityColor(this.id, this.rarity);
    }

    public Set<EquipmentSlot> getEquipmentSlots() {
        return equipmentSlots;
    }

    public void changeEquipmentList(EquipmentSlot slot) {
        if(equipmentSlots.contains(slot)){
            equipmentSlots.remove(slot);
        } else {
            equipmentSlots.add(slot);
        }
    }

    public void addEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlots.add(equipmentSlot);
    }

    public ItemStack buildItem() {

        Gson gson = new Gson();
        String json = gson.toJson(statsMap);

        List<String> lore = getLore();
        lore.add("");
        lore.add(buildItemRarity(this.rarity, this.itemType));

        return new ItemBuilder(this.id, this.material, this.getDisplayName())
                .addLore(lore)
                .setItemId(this.id)
                .setItemRarity(this.rarity)
                .setDisplayName(this.displayName)
                .setItemType(this.itemType)
                .addPersistentDataContainer(STATS_KEY, PersistentDataType.STRING, json)
                .addPersistentDataContainer(EQUIPMENT_SLOT_KEY, PersistentDataType.STRING, equipmentSlots.stream().map(Enum::name).collect(Collectors.joining(";;")))
                .setGlow(this.glowing)
                .setStackSize(this.stackSize)
                .setFlavorText(this.flavorText)
                .setSoulbound(this.soulbound)
                .setValue(this.value)
                .setVisualMaterial(this.visualMaterial)
                .build();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    protected void getStatsLore(List<String> lore) {
        if(statsMap == null || statsMap.isEmpty())
            return;

        for(Stats stats : Stats.values()){
            if(!statsMap.containsKey(stats.name()))
                continue;
            int value = statsMap.get(stats.name());
            lore.add(stats.getDisplay() + ": " + (value < 0 ? "<red>" : "") + value);
        }
    }

    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();

        if(this.flavorText != null) {
            lore.add(this.flavorText);
            lore.add("");
        }

        getStatsLore(lore);
        if(!lore.isEmpty())
            lore.add("");

        if(this.getCustomItemType() == CustomItemType.CUSTOM_ITEM){
            lore.add("<red>This is an unregistered CustomItem");
            lore.add("<red>Please report where you got this item");
            lore.add("");
        }

        return lore;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public BaseCustomItem getBaseItem() {
        return this;
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

    public static List<ItemOptions> getOptionalOptions() {
        return optionalOptions;
    }


    public Optional<Float> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<Material> getVisualMaterial() {
        return Optional.ofNullable(visualMaterial);
    }

    public Optional<String> getFlavorText() {
        return Optional.ofNullable(flavorText);
    }

    public Optional<Boolean> isGlowing() {
        return Optional.ofNullable(glowing);
    }

    public Optional<Integer> getStackSize() {
        return Optional.ofNullable(stackSize);
    }

    public Optional<Boolean> isSoulbound() {
        return Optional.ofNullable(soulbound);
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setVisualMaterial(Material visualMaterial) {
        this.visualMaterial = visualMaterial;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public void setGlowing(Boolean glowing) {
        this.glowing = glowing;
    }

    public void setStackSize(Integer stackSize) {
        if(stackSize != null && stackSize > 99) stackSize = 99;
        else if(stackSize != null && stackSize < 1) stackSize = 1;
        this.stackSize = stackSize;
    }

    public void setSoulbound(Boolean soulbound) {
        this.soulbound = soulbound;
    }

    public int getStatAmount(Stats stats){
        if(!statsMap.containsKey(stats.name()))
            return 0;
        return this.statsMap.get(stats.name());
    }

    public Map<String, Integer> getStatsMap() {
        return statsMap;
    }

    public void setStatsMap(Map<String, Integer> statsMap) {
        this.statsMap = statsMap;
    }

    public void addStatsMap(Stats stats, Integer value) {
        String statId = stats.name();
        if(!statsMap.containsKey(statId) || value != 0){
            statsMap.put(statId, value);
        }else {
            statsMap.remove(statId);
        }

    }

    public void setEquipmentSlots(Set<EquipmentSlot> equipmentSlots) {
        this.equipmentSlots = equipmentSlots;
    }

    public BaseCustomItem copy() {
        return new BaseCustomItem(this.buildItem());
    }
}
