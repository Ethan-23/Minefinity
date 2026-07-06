package org.evasive.me.minefinity.lib.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A dependency-free fluent builder for Bukkit {@link ItemStack}s.
 * <p>
 * It depends only on Bukkit and Adventure, so it can be copied into any Paper/Spigot project
 * unchanged. Project-specific behaviour (custom metadata, rarity, persistent tags) belongs in a
 * subclass &mdash; see {@code CustomItemBuilder} in the Minefinity {@code customItems} module.
 * <p>
 * Text setters accept <a href="https://docs.advntr.dev/minimessage/format.html">MiniMessage</a>
 * strings and render with italics disabled, the usual look for item names and lore.
 * <p>
 * Every setter returns {@code this}, so calls chain:
 * <pre>{@code
 * ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD, "<red>Doom Blade")
 *         .addLore("<gray>Forged in shadow")
 *         .addGlow()
 *         .build();
 * }</pre>
 * Builders are not thread-safe and are intended to be used and discarded on a single thread.
 */
public class ItemBuilder {

    /** Maximum visible characters per wrapped lore line. */
    protected static final int MAX_LORE_CHARACTERS = 40;
    /** Matches a MiniMessage colour/tag token such as {@code <red>} or {@code <#ff0000>}. */
    protected static final Pattern COLOR_TAG = Pattern.compile("<(#?[a-zA-Z0-9]+)>");

    protected ItemStack itemStack;
    protected final List<Component> lore;

    /**
     * Parses a MiniMessage string with italics disabled &mdash; the default look for item text.
     *
     * @param text MiniMessage-formatted string
     * @return the parsed component, non-italic
     */
    protected static Component mm(String text) {
        return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Creates a builder for a fresh item of the given material with a MiniMessage display name.
     *
     * @param material    item material
     * @param displayName MiniMessage display name
     */
    public ItemBuilder(Material material, String displayName) {
        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(mm(displayName));
        this.itemStack.setItemMeta(meta);
    }

    /**
     * Creates a builder for a fresh item of the given material with an Adventure display name.
     *
     * @param material    item material
     * @param displayName display name component (used as-is)
     */
    public ItemBuilder(Material material, Component displayName) {
        this.itemStack = new ItemStack(material);
        this.lore = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(displayName);
        this.itemStack.setItemMeta(meta);
    }

    /**
     * Creates a builder that edits a copy of an existing item, preserving its current lore.
     *
     * @param itemStack the item to copy; must be non-null and not {@link Material#AIR}
     * @throws IllegalArgumentException if the item is null, AIR, or has no meta
     */
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

    /**
     * @return the current item meta
     * @throws IllegalStateException if the backing item has no meta
     */
    protected ItemMeta getMeta() {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) throw new IllegalStateException("ItemStack has no ItemMeta");
        return meta;
    }

    /**
     * Appends a line of lore, word-wrapping at {@link #MAX_LORE_CHARACTERS} and carrying the last
     * active color tag onto each wrapped continuation line. An empty string adds a blank line.
     *
     * @param line MiniMessage lore line (null is ignored)
     * @return this builder
     */
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
                    .serialize(mm(word))
                    .length();

            if (visibleLength > 0 && visibleLength + 1 + wordVisibleLength > MAX_LORE_CHARACTERS) {
                lore.add(mm(currentLine.toString()));
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
            lore.add(mm(currentLine.toString()));
        }

        return this;
    }

    /**
     * Appends several lore lines, each word-wrapped like {@link #addLore(String)}.
     *
     * @param lines MiniMessage lore lines
     * @return this builder
     */
    public ItemBuilder addLore(List<String> lines) {
        for (String line : lines) {
            addLore(line);
        }
        return this;
    }

    /**
     * Clears any existing lore and sets it to the given lines.
     *
     * @param lines MiniMessage lore lines
     * @return this builder
     */
    public ItemBuilder setLore(List<String> lines) {
        lore.clear();
        addLore(lines);
        return this;
    }

    /**
     * Sets the display name from a MiniMessage string.
     *
     * @param displayName MiniMessage display name
     * @return this builder
     */
    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = getMeta();
        meta.displayName(mm(displayName));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item model override (a resource-pack model key).
     *
     * @param itemModel the model key
     * @return this builder
     */
    public ItemBuilder setItemModel(NamespacedKey itemModel) {
        ItemMeta meta = getMeta();
        meta.setItemModel(itemModel);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Appends a blank lore line.
     *
     * @return this builder
     */
    public ItemBuilder addBlank() {
        lore.add(Component.empty());
        return this;
    }

    /**
     * Makes the item unbreakable and hides the unbreakable flag from the tooltip.
     *
     * @return this builder
     */
    public ItemBuilder addUnbreakable() {
        ItemMeta meta = getMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Applies the enchantment-glint override so the item glows without an enchantment.
     *
     * @return this builder
     */
    public ItemBuilder addGlow() {
        ItemMeta meta = getMeta();
        meta.setEnchantmentGlintOverride(true);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Changes the item's material, migrating the existing meta onto the new type where possible.
     *
     * @param material the new material
     * @return this builder
     */
    public ItemBuilder setMaterial(Material material) {
        ItemMeta meta = getMeta();
        ItemStack replacement = new ItemStack(material, this.itemStack.getAmount());
        ItemMeta migrated = Bukkit.getItemFactory().asMetaFor(meta, material);
        if (migrated != null) {
            replacement.setItemMeta(migrated);
        }
        this.itemStack = replacement;
        return this;
    }

    /**
     * Writes a value into the item's persistent data container.
     *
     * @param key   the key to store under
     * @param type  the persistent data type
     * @param value the value to store
     * @param <T>   primitive persistent data type
     * @param <Z>   complex (runtime) type
     * @return this builder
     */
    public <T, Z> ItemBuilder addPersistentDataContainer(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        ItemMeta meta = getMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the owning player of a player-head item. No-op if the item is not a skull.
     *
     * @param player the head owner
     * @return this builder
     */
    public ItemBuilder addSkullMeta(Player player) {
        ItemMeta meta = getMeta();
        if (!(meta instanceof SkullMeta skullMeta)) return this;
        skullMeta.setOwningPlayer(player);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the stack amount.
     *
     * @param amount the amount
     * @return this builder
     */
    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Applies the accumulated lore and returns a finished, independent copy of the item.
     *
     * @return a clone of the built item
     */
    public ItemStack build() {
        ItemMeta meta = getMeta();
        meta.lore(lore.isEmpty() ? null : lore);
        itemStack.setItemMeta(meta);
        return itemStack.clone();
    }
}
