package org.evasive.me.minevolutionCore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.MinevolutionCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ItemBuilder {

    private final int MAX_LORE_CHARACTERS = 40;
    private static final Pattern COLOR_TAG = Pattern.compile("<(#?[a-zA-Z0-9]+)>");

    private final ItemStack itemStack;
    private final ItemMeta meta;
    private final List<Component> lore;

    // Constructor starts with a material and display name
    public ItemBuilder(Material material, Component displayName) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
        this.meta.displayName(displayName);
        this.lore = new ArrayList<>();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
        this.meta.displayName(itemStack.getItemMeta().displayName());
        if(itemStack.lore() == null)
            this.lore = new ArrayList<>();
        else
            this.lore = itemStack.lore();
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

        // Track the currently active color (or last formatting tag)
        String activeColor = "";

        for (String word : words) {
            // Check for color tags in this word


            // Calculate visible length of the word (ignoring color tags)
            int wordVisibleLength = PlainTextComponentSerializer.plainText()
                    .serialize(Messages.parse(word))
                    .length();

            // Wrap line if adding this word would exceed MAX_LORE_CHARACTERS
            if (visibleLength > 0 && visibleLength + 1 + wordVisibleLength > MAX_LORE_CHARACTERS) {
                lore.add(Messages.parse(currentLine.toString()));
                currentLine.setLength(0);
                visibleLength = 0;

                // Start the new line with the last active color
                if (!activeColor.isEmpty()) {
                    currentLine.append(activeColor);
                }
            }

            // Add a space before the word if not the first word in the line
            if (visibleLength > 0) {
                currentLine.append(" ");
                visibleLength += 1;
            }

            // Append the word
            currentLine.append(word);
            Matcher matcher = COLOR_TAG.matcher(word);
            while (matcher.find()) {
                activeColor = matcher.group(); // update active color when a tag is found
            }
            visibleLength += wordVisibleLength;
        }

        // Add any remaining text
        if (!currentLine.isEmpty()) {
            lore.add(Messages.parse(currentLine.toString()));
        }

        return this;
    }


    // Optional: add multiple lines at once
    public ItemBuilder addLore(List<String> lines) {
        for(String line : lines){
            addLore(line);
        }
        return this;
    }

    public ItemBuilder setLore(List<Component> lines) {
        lore.clear();
        lore.addAll(lines);
        return this;
    }

    public ItemBuilder setDisplayName(Component displayName) {
        this.meta.displayName(displayName);
        return this;
    }

    public ItemBuilder setItemModel(NamespacedKey itemModel) {
        this.meta.setItemModel(itemModel);
        return this;
    }

    public ItemBuilder addBlank() {
        lore.add(Messages.parse(""));
        return this; // allow chaining
    }

    public ItemBuilder addGlow() {
        meta.addEnchant(Enchantment.LOYALTY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addUniqueTag(){
        meta.getPersistentDataContainer().set(new NamespacedKey(MinevolutionCore.getCore(), "UniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
        return this;
    }

    public ItemBuilder addPersistentDataContainer(NamespacedKey namespacedKey, String value) {
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        return this;
    }

    // Build the final ItemStack
    public ItemStack build() {
        if (!lore.isEmpty()) meta.lore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


}