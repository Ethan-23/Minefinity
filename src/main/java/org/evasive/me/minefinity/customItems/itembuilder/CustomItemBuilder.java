package org.evasive.me.minefinity.customItems.itembuilder;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.lib.item.ItemBuilder;

import java.util.List;
import java.util.UUID;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

/**
 * The Minefinity item builder: extends the portable {@link ItemBuilder} with custom-item metadata
 * (id, type, rarity, soulbound, value, ...) written into the item's persistent data container.
 * <p>
 * The inherited generic setters are overridden to return {@code CustomItemBuilder}, so a chain can
 * freely mix generic calls (lore, glow) with custom ones (rarity, value) in any order.
 */
public class CustomItemBuilder extends ItemBuilder {

    /**
     * Creates a custom-item builder for a fresh item.
     * @param material item material
     * @param displayName MiniMessage display name
     */
    public CustomItemBuilder(Material material, String displayName) {
        super(material, displayName);
    }

    /**
     * Creates a custom-item builder for a fresh item.
     * @param material item material
     * @param displayName display name component
     */
    public CustomItemBuilder(Material material, Component displayName) {
        super(material, displayName);
    }

    /**
     * Creates a custom-item builder that edits a copy of an existing item.
     * @param itemStack the item to copy
     */
    public CustomItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Creates a custom-item builder for a fresh item and stamps its item id.
     * @param itemId custom item id
     * @param material item material
     * @param displayName MiniMessage display name
     */
    public CustomItemBuilder(String itemId, Material material, String displayName) {
        super(material, displayName);
        setItemId(itemId);
    }

    /**
     * Adds a unique UUID
     * @return CustomItemBuilder
     */
    public CustomItemBuilder addUniqueTag() {
        addPersistentDataContainer(UNIQUE_ID_KEY, PersistentDataType.STRING, UUID.randomUUID().toString());
        return this;
    }

    /**
     * Sets the items Type
     * @param customItemType New type for custom item
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setItemType(CustomItemType customItemType) {
        addPersistentDataContainer(ITEM_TYPE_KEY, PersistentDataType.STRING, customItemType.name());
        return this;
    }

    /**
     * Sets the items Rarity
     * @param rarity New Rarity for custom item
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setItemRarity(Rarity rarity) {
        addPersistentDataContainer(ITEM_RARITY_KEY, PersistentDataType.STRING, rarity.name());
        return this;
    }

    /**
     * Sets the items ID
     * @param itemId New String ID
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setItemId(String itemId) {
        addPersistentDataContainer(ITEM_ID_KEY, PersistentDataType.STRING, itemId);
        return this;
    }

    /**
     * Sets the item to glow
     * @param glow Boolean
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setGlow(Boolean glow) {
        if (glow == null || !glow) return this;
        addGlow();
        addPersistentDataContainer(GLOWING_KEY, PersistentDataType.BYTE, (byte) 1);
        return this;
    }

    /**
     * Sets the max stack size
     * @param size Max stack size (null is ignored)
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setStackSize(Integer size) {
        if (size == null) return this;
        ItemMeta meta = getMeta();
        meta.setMaxStackSize(size);
        itemStack.setItemMeta(meta);
        addPersistentDataContainer(STACK_SIZE_KEY, PersistentDataType.INTEGER, size);
        return this;
    }

    /**
     * Marks the item soulbound
     * @param soulbound Boolean (null/false is ignored)
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setSoulbound(Boolean soulbound) {
        if (soulbound == null || !soulbound) return this;
        addPersistentDataContainer(SOULBOUND_KEY, PersistentDataType.BYTE, (byte) 1);
        return this;
    }

    /**
     * Sets the item's flavor text
     * @param flavorText Flavor text (null is ignored)
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setFlavorText(String flavorText) {
        if (flavorText == null) return this;
        addPersistentDataContainer(FLAVOR_TEXT_KEY, PersistentDataType.STRING, flavorText);
        return this;
    }

    /**
     * Sets the item's value
     * @param amount Value (null is ignored)
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setValue(Float amount) {
        if (amount == null) return this;
        addPersistentDataContainer(VALUE_KEY, PersistentDataType.FLOAT, amount);
        return this;
    }

    /**
     * Sets a visual-only material (item model) while keeping the real type
     * @param material Visual material (null is ignored)
     * @return CustomItemBuilder
     */
    public CustomItemBuilder setVisualMaterial(Material material) {
        if (material == null) return this;
        ItemMeta meta = getMeta();
        meta.setItemModel(material.getKey());
        itemStack.setItemMeta(meta);
        addPersistentDataContainer(VISUAL_MATERIAL_KEY, PersistentDataType.STRING, material.name());
        return this;
    }

    // ---- Display name: adds rarity colour + records the raw name in the PDC ----

    /**
     * Sets the display name, colouring it by the item's rarity (if one is set) and recording the
     * raw name in the persistent data container so it can be recovered later.
     * @param displayName MiniMessage display name
     * @return CustomItemBuilder
     */
    @Override
    public CustomItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = getMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String displayNameText = displayName;
        if (pdc.has(ITEM_RARITY_KEY))
            displayNameText = TextConversions.setRarityColor(displayName, Rarity.valueOf(pdc.get(ITEM_RARITY_KEY, PersistentDataType.STRING)));
        meta.displayName(TextConversions.parse(displayNameText));
        itemStack.setItemMeta(meta);
        addPersistentDataContainer(DISPLAY_NAME_KEY, PersistentDataType.STRING, displayName);
        return this;
    }

    // ---- Covariant overrides so chaining keeps returning CustomItemBuilder ----

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addLore(String line) {
        super.addLore(line);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addLore(List<String> lines) {
        super.addLore(lines);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder setLore(List<String> lines) {
        super.setLore(lines);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder setItemModel(NamespacedKey itemModel) {
        super.setItemModel(itemModel);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addBlank() {
        super.addBlank();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addUnbreakable() {
        super.addUnbreakable();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addGlow() {
        super.addGlow();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder setMaterial(Material material) {
        super.setMaterial(material);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public <T, Z> CustomItemBuilder addPersistentDataContainer(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        super.addPersistentDataContainer(key, type, value);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder addSkullMeta(Player player) {
        super.addSkullMeta(player);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CustomItemBuilder setAmount(int amount) {
        super.setAmount(amount);
        return this;
    }
}
