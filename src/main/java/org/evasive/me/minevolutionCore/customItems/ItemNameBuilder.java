package org.evasive.me.minevolutionCore.customItems;

import org.evasive.me.minevolutionCore.rarity.Rarity;

public class ItemNameBuilder {


    public static String buildRarityColor(String name, Rarity rarity){
        return "<"+rarity.getRarityBuilder().getTextColor().asHexString()+">" + formatItemName(name);
    }

    public static String buildItemType(String type){
        return "<#555555>" + formatItemName(type);
    }

    public static String buildItemRarity(Rarity rarity){
        return "<bold><"+rarity.getRarityBuilder().getTextColor().asHexString()+">" + rarity.name();
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
