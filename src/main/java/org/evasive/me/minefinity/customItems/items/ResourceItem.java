package org.evasive.me.minefinity.customItems.items;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.BaseCustomItem;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.rarity.Rarity;

public enum ResourceItem implements CustomItem {
    OAK_PLANK(Material.OAK_PLANKS, Rarity.MINOR, 0.5f),
    OAK_BARK(Material.OAK_LOG, Rarity.MINOR, 1f),
    TIMBER(Material.STRIPPED_OAK_LOG, Rarity.MINOR, 10f),
    GRAVEL(Material.GRAVEL, Rarity.MINOR, 2f),
    FLINT(Material.FLINT, Rarity.MINOR, 3f),
    CLAY_BALL(Material.CLAY_BALL, Rarity.MINOR, 3f),
    BRICK(Material.BRICK, Rarity.MINOR, 4f),
    BRICKS(Material.BRICKS, Rarity.MINOR, 20f),
    COBBLESTONE(Material.COBBLESTONE, Rarity.MINOR, 5f),
    ROCK(Material.COBBLESTONE_SLAB, Rarity.MINOR, 7f),
    STONE(Material.STONE, Rarity.MINOR, 6f),
    ROCKY_STONE(Material.STONE_BRICKS, Rarity.MINOR, 10f),
    ANDESITE(Material.ANDESITE, Rarity.UNIQUE, 17f),
    RAW_COPPER(Material.RAW_COPPER, Rarity.UNIQUE, 12f),
    COPPER_INGOT(Material.COPPER_INGOT, Rarity.UNIQUE, 15f),
    TUFF(Material.TUFF, Rarity.UNIQUE, 12),
    TUFF_BRICK(Material.MUD, Rarity.UNIQUE, 10f),
    TOUGH_STONE(Material.CHISELED_STONE_BRICKS, Rarity.UNIQUE, 10f),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, Rarity.UNIQUE, 4f),
    MOSS(Material.MOSS_CARPET, Rarity.UNIQUE, 4f),
    MOSSY_TIMBER(Material.JUNGLE_WOOD, Rarity.UNIQUE, 10f),
    GRANITE(Material.GRANITE, Rarity.UNIQUE, 30f),
    RAW_TIN(Material.RAW_IRON, Rarity.UNIQUE, 10f),
    TIN_INGOT(Material.IRON_INGOT, Rarity.UNIQUE, 10f),
    BRONZE_INGOT(Material.COPPER_INGOT, Rarity.UNIQUE, 10f),
    DIORITE(Material.DIORITE, Rarity.MINOR, 10f),
    RAW_IRON(Material.RAW_IRON, Rarity.RADIANT, 25f),
    IRON_INGOT(Material.IRON_INGOT, Rarity.RADIANT, 40f),
    CALCITE(Material.CALCITE, Rarity.RADIANT, 60f),
    STURDY_STONE(Material.SMOOTH_STONE, Rarity.RADIANT, 10f),
    LAPIS(Material.LAPIS_LAZULI, Rarity.RADIANT, 40f),
    //LEFT OFF ADDING NEW HERE
    REDSTONE(Material.REDSTONE, Rarity.EXQUISITE, 55f),
    RAW_GOLD(Material.RAW_GOLD, Rarity.EXQUISITE, 75f),
    GOLD_INGOT(Material.GOLD_INGOT, Rarity.EXQUISITE, 100f),
    DEEPSLATE(Material.DEEPSLATE, Rarity.RADIANT, 50f),
    AMETHYST(Material.AMETHYST_SHARD, Rarity.EXQUISITE, 70f),
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
