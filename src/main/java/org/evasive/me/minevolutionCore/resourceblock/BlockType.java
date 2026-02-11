package org.evasive.me.minevolutionCore.resourceblock;

import org.bukkit.Material;
import org.evasive.me.minevolutionCore.core.items.CustomItem;
import org.evasive.me.minevolutionCore.customItems.items.FuelItem;
import org.evasive.me.minevolutionCore.customItems.items.ResourceItem;

public enum BlockType {
    OAK_LOG("Log", Material.OAK_LOG, 40, ResourceItem.OAK_PLANK, 0),
    GRAVEL("Gravel", Material.GRAVEL, 80, ResourceItem.GRAVEL, 50),
    CLAY("Clay", Material.CLAY, 120, ResourceItem.CLAY_BALL, 250),
    COBBLESTONE("Cobblestone", Material.COBBLESTONE, 160, ResourceItem.COBBLESTONE, 1500),
    COAL_ORE("Coal Ore", Material.COAL_ORE, 200, FuelItem.COAL, 3000),
    ANDESITE("Andesite", Material.ANDESITE, 300, ResourceItem.ANDESITE, 5000),
    COPPER_ORE("Copper Ore", Material.COPPER_ORE, 450, ResourceItem.RAW_COPPER, 15000),
    GRANITE("Granite", Material.GRANITE, 170, ResourceItem.GRANITE, 0),
    MOSSY_COBBLESTONE("Mossy Cobblestone", Material.MOSSY_COBBLESTONE, 190, ResourceItem.MOSSY_COBBLESTONE, 0),
    DIORITE("Diorite", Material.DIORITE, 190, ResourceItem.DIORITE, 0),
    TUFF("Tuff", Material.TUFF, 200, ResourceItem.TUFF, 0),
    IRON_ORE("Iron Ore", Material.IRON_ORE, 220, ResourceItem.RAW_IRON, 0),
    DEEPSLATE("Deepslate", Material.DEEPSLATE, 240, ResourceItem.DEEPSLATE, 0),
    DEEPSLATE_COAL_ORE("Coal Ore 2", Material.DEEPSLATE_COAL_ORE, 260, FuelItem.COAL, 0), // Change to tier 2 fuel when made
    DEEPSLATE_LAPIS_ORE("Lapis Ore", Material.DEEPSLATE_LAPIS_ORE, 280, ResourceItem.LAPIS, 0),
    CALCITE("Calcite", Material.CALCITE, 300, ResourceItem.CALCITE, 0),
    DEEPSLATE_GOLD_ORE("Gold Ore", Material.DEEPSLATE_GOLD_ORE, 320, ResourceItem.RAW_GOLD, 0),
    AMETHYST("Amethyst", Material.AMETHYST_BLOCK, 340, ResourceItem.AMETHYST, 0),
    DEEPSLATE_REDSTONE_ORE("Redstone Ore", Material.REDSTONE_ORE, 360, ResourceItem.REDSTONE, 0),
    COAL_BLOCK("Coal Block", Material.COAL_BLOCK, 400, FuelItem.COAL_BLOCK, 0),
    DEEPSLATE_DIAMOND_ORE("Diamond Ore", Material.DEEPSLATE_DIAMOND_ORE, 450, ResourceItem.DIAMOND, 0),
    OBSIDIAN("Obsidian", Material.OBSIDIAN, 500, ResourceItem.OBSIDIAN, 0),
    REINFORCED_DEEPSLATE("Reinforced Deepslate", Material.REINFORCED_DEEPSLATE, 550, ResourceItem.REINFORCED_DEEPSLATE, 0),
    DEEPSLATE_EMERALD_ORE("Emerald Ore", Material.DEEPSLATE_EMERALD_ORE, 600, ResourceItem.EMERALD, 0)
    ;

    private final BaseBlock block;
    BlockType(String name, Material material, int health, CustomItem blockDrop, int unlockCost) {
        // Use the enum constant name as the ID automatically
        this.block = new BaseBlock(name, material, health, blockDrop, unlockCost);
    }

    public BaseBlock getBlock() {
        return block;
    }

}
