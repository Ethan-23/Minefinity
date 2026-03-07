package org.evasive.me.minefinity.resourceblock.framework;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;

import java.util.List;

public enum BlockType {
    OAK_LOG("Log", Material.OAK_LOG, 40, null, 0),
    GRAVEL("Gravel", Material.GRAVEL, 80, null, 50),
    CLAY("Clay", Material.CLAY, 120, null, 250),
    COBBLESTONE("Cobblestone", Material.COBBLESTONE, 160, null, 1500),
    ANDESITE("Andesite", Material.ANDESITE, 200, null, 3000),
    COPPER_ORE("Copper Ore", Material.COPPER_ORE, 300, null, 5000),
    COAL_ORE("Coal Ore", Material.COAL_ORE, 400, null, 15000),
    TUFF("Tuff", Material.TUFF, 500, null, 0),
    MOSSY_COBBLESTONE("Mossy Cobblestone", Material.MOSSY_COBBLESTONE, 600, null, 0),
    GRANITE("Granite", Material.GRANITE, 600, null, 0),
    DIORITE("Diorite", Material.DIORITE, 600, null, 0),
    IRON_ORE("Iron Ore", Material.IRON_ORE, 600, null, 0),
    DEEPSLATE_COAL_ORE("Enriched Coal", Material.DEEPSLATE_COAL_ORE, 600, null, 0), // Change to tier 2 fuel when made
    CALCITE("Calcite", Material.CALCITE, 600, null, 0),
    LAPIS_ORE("Lapis Ore", Material.LAPIS_ORE, 600, null, 0),
    REDSTONE_ORE("Redstone Ore", Material.REDSTONE_ORE, 600, null, 0),
    GOLD_ORE("Gold Ore", Material.GOLD_ORE, 600, null, 0),
    COAL_BLOCK("Coal Block", Material.COAL_BLOCK, 600, null, 0),
    AMETHYST("Amethyst", Material.AMETHYST_BLOCK, 600, null, 0),
    DEEPSLATE("Deepslate", Material.DEEPSLATE, 600, null, 0),
    DEEPSLATE_DIAMOND_ORE("Diamond Ore", Material.DEEPSLATE_DIAMOND_ORE, 600, null, 0),
    OBSIDIAN("Obsidian", Material.OBSIDIAN, 600, null, 0),
    REINFORCED_DEEPSLATE("Reinforced Deepslate", Material.REINFORCED_DEEPSLATE, 600, null, 0),
    DEEPSLATE_EMERALD_ORE("Emerald Ore", Material.DEEPSLATE_EMERALD_ORE, 600, null, 0)
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
