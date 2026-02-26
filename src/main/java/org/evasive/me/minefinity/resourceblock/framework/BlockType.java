package org.evasive.me.minefinity.resourceblock.framework;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.types.FuelItem;
import org.evasive.me.minefinity.customItems.types.ResourceItem;

import java.util.List;

public enum BlockType {
    OAK_LOG("Log", Material.OAK_LOG, 40, ResourceItem.OAK_PLANK, 0),
    GRAVEL("Gravel", Material.GRAVEL, 80, ResourceItem.GRAVEL, 50),
    CLAY("Clay", Material.CLAY, 120, ResourceItem.CLAY_BALL, 250),
    COBBLESTONE("Cobblestone", Material.COBBLESTONE, 160, ResourceItem.COBBLESTONE, 1500),
    ANDESITE("Andesite", Material.ANDESITE, 200, ResourceItem.ANDESITE, 3000),
    COPPER_ORE("Copper Ore", Material.COPPER_ORE, 300, ResourceItem.RAW_COPPER, 5000),
    COAL_ORE("Coal Ore", Material.COAL_ORE, 400, FuelItem.COAL, 15000),
    TUFF("Tuff", Material.TUFF, 500, ResourceItem.TUFF, 0),
    MOSSY_COBBLESTONE("Mossy Cobblestone", Material.MOSSY_COBBLESTONE, 600, ResourceItem.MOSSY_COBBLESTONE, 0),
    GRANITE("Granite", Material.GRANITE, 600, ResourceItem.GRANITE, 0),
    DIORITE("Diorite", Material.DIORITE, 600, ResourceItem.DIORITE, 0),
    IRON_ORE("Iron Ore", Material.IRON_ORE, 600, ResourceItem.RAW_IRON, 0),
    DEEPSLATE_COAL_ORE("Enriched Coal", Material.DEEPSLATE_COAL_ORE, 600, FuelItem.ENRICHED_COAL, 0), // Change to tier 2 fuel when made
    CALCITE("Calcite", Material.CALCITE, 600, ResourceItem.CALCITE, 0),
    LAPIS_ORE("Lapis Ore", Material.LAPIS_ORE, 600, ResourceItem.LAPIS, 0),
    REDSTONE_ORE("Redstone Ore", Material.REDSTONE_ORE, 600, ResourceItem.REDSTONE, 0),
    GOLD_ORE("Gold Ore", Material.GOLD_ORE, 600, ResourceItem.RAW_GOLD, 0),
    COAL_BLOCK("Coal Block", Material.COAL_BLOCK, 600, FuelItem.COAL_BLOCK, 0),
    AMETHYST("Amethyst", Material.AMETHYST_BLOCK, 600, ResourceItem.AMETHYST, 0),
    DEEPSLATE("Deepslate", Material.DEEPSLATE, 600, ResourceItem.DEEPSLATE, 0),
    DEEPSLATE_DIAMOND_ORE("Diamond Ore", Material.DEEPSLATE_DIAMOND_ORE, 600, ResourceItem.DIAMOND, 0),
    OBSIDIAN("Obsidian", Material.OBSIDIAN, 600, ResourceItem.OBSIDIAN, 0),
    REINFORCED_DEEPSLATE("Reinforced Deepslate", Material.REINFORCED_DEEPSLATE, 600, ResourceItem.REINFORCED_DEEPSLATE, 0),
    DEEPSLATE_EMERALD_ORE("Emerald Ore", Material.DEEPSLATE_EMERALD_ORE, 600, ResourceItem.EMERALD, 0)
    ;

    private final BaseBlock block;
    BlockType(String name, Material material, int health, CustomItem blockDrop, int unlockCost) {
        // Use the enum constant name as the ID automatically
        this.block = new BaseBlock(name, material, health, blockDrop, unlockCost, List.of(50, 250, 750, 2500, 7500, 20000, 50000));
    }

    public BaseBlock getBlock() {
        return block;
    }

}
