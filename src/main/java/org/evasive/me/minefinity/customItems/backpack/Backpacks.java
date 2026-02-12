package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.items.CustomItemType;
import org.evasive.me.minefinity.customItems.items.ResourceItem;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.Set;

public enum Backpacks implements CustomItem {
    RESOURCE_BACKPACK(Material.SUGAR,
            Material.GREEN_BUNDLE,
            Rarity.UNIQUE,
            640,
            Set.of(
                    ResourceItem.OAK_PLANK.getID(),
                    ResourceItem.GRAVEL.getID(),
                    ResourceItem.CLAY_BALL.getID(),
                    ResourceItem.COBBLESTONE.getID(),
                    ResourceItem.ANDESITE.getID(),
                    ResourceItem.GRANITE.getID(),
                    ResourceItem.MOSSY_COBBLESTONE.getID(),
                    ResourceItem.DIORITE.getID(),
                    ResourceItem.TUFF.getID(),
                    ResourceItem.DEEPSLATE.getID(),
                    ResourceItem.CALCITE.getID(),
                    ResourceItem.OBSIDIAN.getID(),
                    ResourceItem.REINFORCED_DEEPSLATE.getID()
            )
    ),
    ORE_BACKPACK(Material.SUGAR,
            Material.CYAN_BUNDLE,
            Rarity.RADIANT,
            640,
            Set.of(
                    ResourceItem.RAW_COPPER.getID(),
                    ResourceItem.COPPER_INGOT.getID(),
                    ResourceItem.RAW_IRON.getID(),
                    ResourceItem.IRON_INGOT.getID(),
                    ResourceItem.LAPIS.getID(),
                    ResourceItem.REDSTONE.getID(),
                    ResourceItem.RAW_GOLD.getID(),
                    ResourceItem.GOLD_INGOT.getID(),
                    ResourceItem.AMETHYST.getID(),
                    ResourceItem.DIAMOND.getID(),
                    ResourceItem.EMERALD.getID(),
                    ResourceItem.DIAMOND_ORE.getID(),
                    ResourceItem.EMERALD_ORE.getID()
            )
    ),


    ;

    private final BaseBackpackItem builder;
    Backpacks(Material material, Material visual, Rarity rarity, int storedItemAmount, Set<String> storedItemIdList) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseBackpackItem(this.name(), material, visual, rarity, CustomItemType.STORAGE, storedItemAmount, storedItemIdList);
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    public static boolean contains(String value) {
        for (Backpacks backpacks : Backpacks.values()) {
            if (backpacks.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BaseBackpackItem getBuilder() {
        return builder;
    }
}
