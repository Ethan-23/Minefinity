package org.evasive.me.minefinity.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;

public class TextConversions {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Component parse(String mm) {
        return MM.deserialize(mm).decoration(TextDecoration.ITALIC, false);
    }

    public static Component parse(String mm, TagResolver... tags) {
        return MM.deserialize(mm, tags).decoration(TextDecoration.ITALIC, false);
    }

    public static String intToRoman(int num) {
        int[] values =    {1000, 900, 500, 400, 100, 90,  50,  40,  10, 9,   5,  4,   1};
        String[] symbols = {"M",  "CM","D", "CD","C", "XC","L", "XL","X","IX","V","IV","I"};

        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(symbols[i]);
            }
        }
        return roman.toString();
    }

    public static String buildRarityColor(String name, Rarity rarity){
        return "<"+rarity.getRarityBuilder().textColor().asHexString()+">" + formatItemName(name);
    }

    public static String setRarityColor(String name, Rarity rarity){
        return "<"+rarity.getRarityBuilder().textColor().asHexString()+">" + name;
    }

    public static String buildColor(String name, String hexCode){
        return "<"+hexCode+">" + formatItemName(name);
    }

    public static String buildItemType(String type){
        return "<#555555>" + formatItemName(type);
    }

    public static String buildItemRarity(Rarity rarity, CustomItemType customItemType){
        return "<bold><"+rarity.getRarityBuilder().textColor().asHexString()+">" + rarity.name() + " " + customItemType.name().replace("_", " ");
    }

    public static String formatItemName(String rawName) {
        String[] parts = rawName.split("_");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1).toUpperCase() +
                    parts[i].substring(1).toLowerCase();
        }

        return String.join(" ", parts);
    }
}
