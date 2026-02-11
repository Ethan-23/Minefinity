package org.evasive.me.minevolutionCore.customItems.items;

import org.bukkit.Material;
import org.evasive.me.minevolutionCore.core.items.BaseCustomItem;
import org.evasive.me.minevolutionCore.core.items.CustomItem;
import org.evasive.me.minevolutionCore.customItems.backpack.Backpacks;
import org.evasive.me.minevolutionCore.rarity.Rarity;

public enum ResourceItem implements CustomItem {
    OAK_PLANK(Material.OAK_PLANKS, Rarity.MINOR, 0.5f),
    OAK_BARK(Material.OAK_LOG, Rarity.MINOR, 1f),
    COBBLESTONE(Material.COBBLESTONE, Rarity.MINOR, 1f),
    CLAY_BALL(Material.CLAY_BALL, Rarity.MINOR, 2f),
    GRAVEL(Material.GRAVEL, Rarity.MINOR, 3f),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, Rarity.UNIQUE, 4f),
    STONE(Material.STONE, Rarity.MINOR, 5f),
    ROCKS(Material.COBBLESTONE, Rarity.MINOR, 6f),
    COMPRESSED_STONE(Material.SMOOTH_STONE_SLAB, Rarity.MINOR, 7f),
    REINFORCED_STONE(Material.SMOOTH_STONE, Rarity.MINOR, 8f),
    DIORITE(Material.DIORITE, Rarity.MINOR, 10f),
    RAW_COPPER(Material.RAW_COPPER, Rarity.UNIQUE, 12f),
    COPPER_INGOT(Material.COPPER_INGOT, Rarity.UNIQUE, 15f),
    ANDESITE(Material.ANDESITE, Rarity.UNIQUE, 17f),
    RAW_IRON(Material.RAW_IRON, Rarity.RADIANT, 25f),
    IRON_INGOT(Material.IRON_INGOT, Rarity.RADIANT, 40f),
    GRANITE(Material.GRANITE, Rarity.UNIQUE, 30f),
    LAPIS(Material.LAPIS_LAZULI, Rarity.RADIANT, 40f),
    TUFF(Material.TUFF, Rarity.UNIQUE, 45f),
    DEEPSLATE(Material.DEEPSLATE, Rarity.RADIANT, 50f),
    REDSTONE(Material.REDSTONE, Rarity.EXQUISITE, 55f),
    CALCITE(Material.CALCITE, Rarity.RADIANT, 60f),
    AMETHYST(Material.AMETHYST_SHARD, Rarity.EXQUISITE, 70f),
    RAW_GOLD(Material.RAW_GOLD, Rarity.EXQUISITE, 75f),
    GOLD_INGOT(Material.GOLD_INGOT, Rarity.EXQUISITE, 100f),
    DIAMOND_ORE(Material.DIAMOND_ORE, Rarity.PRISTINE, 125f),
    OBSIDIAN(Material.OBSIDIAN, Rarity.PRISTINE, 200f),
    DIAMOND(Material.DIAMOND, Rarity.PRISTINE, 200f),
    REINFORCED_DEEPSLATE(Material.REINFORCED_DEEPSLATE, Rarity.PRISTINE, 190f),
    EMERALD_ORE(Material.EMERALD_ORE, Rarity.PRISTINE, 300f),
    EMERALD(Material.EMERALD, Rarity.PRISTINE, 400f)
    ;

    private final BaseCustomItem builder;
    ResourceItem(Material material, Rarity rarity, float value) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseCustomItem(this.name(), material, rarity, CustomItemType.RESOURCE, value);
    }

    public static boolean contains(String value) {
        for (ResourceItem resourceItem : ResourceItem.values()) {
            if (resourceItem.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    @Override
    public BaseCustomItem getBuilder() {
        return builder;
    }

}
