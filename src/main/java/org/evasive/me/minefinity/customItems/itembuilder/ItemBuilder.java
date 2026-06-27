package org.evasive.me.minefinity.customItems.itembuilder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;


public class ItemBuilder {

    private static final int MAX_LORE_CHARACTERS = 40;
    private static final Pattern COLOR_TAG = Pattern.compile("<(#?[a-zA-Z0-9]+)>");

    private ItemStack itemStack;
    private final List<Component> lore;

    public ItemBuilder(Material material, String displayName) {
        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(TextConversions.parse(displayName));
        this.itemStack.setItemMeta(meta);
    }

    //Temp to passify errors maybe
    public ItemBuilder(Material material, Component displayName) {
        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(displayName);
        this.itemStack.setItemMeta(meta);
    }

    public ItemBuilder(String itemId, Material material, String displayName) {

        if (material == null)
            throw new IllegalArgumentException("Material cannot be null");
        if (material == Material.AIR)
            throw new IllegalArgumentException("Cannot build from AIR");

        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();

        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null)
            throw new IllegalArgumentException("ItemStack has no ItemMeta");

        List<Component> existingLore = this.itemStack.lore();
        if (existingLore != null)
            this.lore.addAll(existingLore);

        meta.displayName(TextConversions.parse(displayName));
        meta.lore(this.lore);
        this.itemStack.setItemMeta(meta);
        setItemId(itemId);
    }

    public ItemBuilder(ItemStack itemStack) {
        if (itemStack == null)
            throw new IllegalArgumentException("ItemStack cannot be null");
        if (itemStack.getType() == Material.AIR)
            throw new IllegalArgumentException("Cannot build from AIR");

        this.itemStack = itemStack.clone();

        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null)
            throw new IllegalArgumentException("ItemStack has no ItemMeta");

        this.lore = new ArrayList<>();
        List<Component> existingLore = this.itemStack.lore();
        if (existingLore != null)
            this.lore.addAll(existingLore);

        meta.lore(this.lore);
        this.itemStack.setItemMeta(meta);
    }

    private ItemMeta getMeta() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) throw new IllegalStateException("ItemStack has no ItemMeta");
        return meta;
    }

    public ItemBuilder addLore(String line) {
        if (line == null) return this;

        if (line.isEmpty()) {
            lore.add(Component.empty());
            return this;
        }

        String[] words = line.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int visibleLength = 0;

        String activeColor = "";

        for (String word : words) {

            int wordVisibleLength = PlainTextComponentSerializer.plainText()
                    .serialize(TextConversions.parse(word))
                    .length();

            if (visibleLength > 0 && visibleLength + 1 + wordVisibleLength > MAX_LORE_CHARACTERS) {
                lore.add(TextConversions.parse(currentLine.toString()));
                currentLine.setLength(0);
                visibleLength = 0;

                if (!activeColor.isEmpty()) {
                    currentLine.append(activeColor);
                }
            }

            if (visibleLength > 0) {
                currentLine.append(" ");
                visibleLength += 1;
            }

            currentLine.append(word);
            Matcher matcher = COLOR_TAG.matcher(word);
            while (matcher.find()) {
                activeColor = matcher.group();
            }
            visibleLength += wordVisibleLength;
        }

        if (!currentLine.isEmpty()) {
            lore.add(TextConversions.parse(currentLine.toString()));
        }

        return this;
    }

    public ItemBuilder addLore(List<String> lines) {
        for(String line : lines){
            addLore(line);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lines) {
        lore.clear();
        addLore(lines);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = getMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String displayNameText = displayName;
        if(pdc.has(ITEM_RARITY_KEY))
            displayNameText = TextConversions.setRarityColor(displayName, Rarity.valueOf(pdc.get(ITEM_RARITY_KEY, PersistentDataType.STRING)));
        meta.displayName(TextConversions.parse(displayNameText));
        this.itemStack.setItemMeta(meta);
        addPersistentDataContainer(DISPLAY_NAME_KEY, PersistentDataType.STRING, displayName);
        return this;
    }

    public ItemBuilder setItemModel(NamespacedKey itemModel) {
        ItemMeta meta = getMeta();
        meta.setItemModel(itemModel);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addBlank() {
        lore.add(TextConversions.parse(""));
        return this;
    }

    public ItemBuilder addUnbreakable() {
        ItemMeta meta = getMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addGlow() {
        ItemMeta meta = getMeta();
        meta.setEnchantmentGlintOverride(true);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addUniqueTag(){
        return addPersistentDataContainer(UNIQUE_ID_KEY, PersistentDataType.STRING, UUID.randomUUID().toString());
    }

    public ItemBuilder setItemType(CustomItemType customItemType){
        return addPersistentDataContainer(ITEM_TYPE_KEY, PersistentDataType.STRING, customItemType.name());
    }

    public ItemBuilder setItemRarity(Rarity rarity){
        return addPersistentDataContainer(ITEM_RARITY_KEY, PersistentDataType.STRING, rarity.name());
    }

    public ItemBuilder setItemId(String itemId){
        return addPersistentDataContainer(ITEM_ID_KEY, PersistentDataType.STRING, itemId);
    }

    public ItemBuilder setGlow(Boolean glow){
        if (glow == null || !glow) return this;
        this.addGlow();
        return addPersistentDataContainer(GLOWING_KEY, PersistentDataType.BYTE, (byte)1);
    }

    public ItemBuilder setStackSize(Integer size){
        if(size == null) return this;

        ItemMeta meta = getMeta();
        meta.setMaxStackSize(size);
        itemStack.setItemMeta(meta);
        return addPersistentDataContainer(STACK_SIZE_KEY, PersistentDataType.INTEGER, size);
    }

    public ItemBuilder setSoulbound(Boolean soulbound){
        if (soulbound == null || !soulbound) return this;
        return addPersistentDataContainer(SOULBOUND_KEY, PersistentDataType.BYTE, (byte)1);
    }

    public ItemBuilder setFlavorText(String flavorText){
        if (flavorText == null) return this;
        return addPersistentDataContainer(FLAVOR_TEXT_KEY, PersistentDataType.STRING, flavorText);
    }

    public ItemBuilder setValue(Float amount){
        if (amount == null) return this;
        return addPersistentDataContainer(VALUE_KEY, PersistentDataType.FLOAT, amount);
    }

    public ItemBuilder setVisualMaterial(Material material){
        if (material == null) return this;
        ItemMeta meta = getMeta();
        meta.setItemModel(material.getKey());
        itemStack.setItemMeta(meta);
        return addPersistentDataContainer(VISUAL_MATERIAL_KEY, PersistentDataType.STRING, material.name());
    }

    public ItemBuilder setMaterial(Material material){
        ItemMeta meta = getMeta();
        ItemStack replacement = new ItemStack(material, this.itemStack.getAmount());
        // Only carry the old meta over if it is valid for the new material (e.g. don't force
        // SkullMeta onto a stone); otherwise keep the new material's default meta.
        if (Bukkit.getItemFactory().isApplicable(meta, material)) {
            replacement.setItemMeta(meta);
        }
        this.itemStack = replacement;
        return this;
    }

    public <T, Z> ItemBuilder addPersistentDataContainer(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
        ItemMeta meta = getMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addSkullMeta(Player player){
        ItemMeta meta = getMeta();
        if(!(meta instanceof SkullMeta skullMeta)) return this;
        skullMeta.setOwningPlayer(player);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        ItemMeta meta = getMeta();
        // Always apply lore (null clears it) so setLore([]) can actually remove lore.
        meta.lore(lore.isEmpty() ? null : lore);
        itemStack.setItemMeta(meta);
        return itemStack.clone();
    }


}