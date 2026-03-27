package org.evasive.me.minefinity.towns.worldPackets;

import org.bukkit.Material;

import java.util.Map;

public enum StructurePallets {
    TIER0(Map.of(
            Material.OAK_PLANKS,Material.AIR,
            Material.OAK_WOOD,Material.AIR,
            Material.OAK_LOG,Material.AIR,
            Material.OAK_STAIRS,Material.AIR,
            Material.OAK_FENCE,Material.AIR,
            Material.OAK_SLAB,Material.AIR
    )),
    TIER1(Map.of(
            Material.OAK_PLANKS,Material.OAK_PLANKS,
            Material.OAK_WOOD,Material.OAK_WOOD,
            Material.OAK_LOG,Material.OAK_LOG,
            Material.OAK_STAIRS,Material.OAK_STAIRS,
            Material.OAK_FENCE,Material.OAK_FENCE,
            Material.OAK_SLAB,Material.OAK_SLAB
    )),
    TIER2(Map.of(
            Material.OAK_PLANKS,Material.COBBLESTONE,
            Material.OAK_WOOD,Material.STONE_BRICKS,
            Material.OAK_LOG,Material.STONE_BRICKS,
            Material.OAK_STAIRS,Material.COBBLESTONE_STAIRS,
            Material.OAK_FENCE,Material.COBBLESTONE_WALL,
            Material.OAK_SLAB,Material.COBBLESTONE_SLAB
    )),
    TIER3(Map.of(
            Material.OAK_PLANKS,Material.CUT_COPPER,
            Material.OAK_WOOD,Material.COPPER_BULB,
            Material.OAK_LOG,Material.COPPER_BULB,
            Material.OAK_STAIRS,Material.CUT_COPPER_STAIRS,
            Material.OAK_FENCE,Material.COPPER_BARS,
            Material.OAK_SLAB,Material.CUT_COPPER_SLAB
    )),
    TIER4(Map.of(
            Material.OAK_PLANKS,Material.COBBLESTONE,
            Material.OAK_WOOD,Material.STONE_BRICKS,
            Material.OAK_LOG,Material.STONE_BRICKS,
            Material.OAK_STAIRS,Material.COBBLESTONE_STAIRS,
            Material.OAK_FENCE,Material.COBBLESTONE_WALL,
            Material.OAK_SLAB,Material.COBBLESTONE_SLAB
    )),
    TIER5(Map.of(
            Material.OAK_PLANKS,Material.COBBLESTONE,
            Material.OAK_WOOD,Material.STONE_BRICKS,
            Material.OAK_LOG,Material.STONE_BRICKS,
            Material.OAK_STAIRS,Material.COBBLESTONE_STAIRS,
            Material.OAK_FENCE,Material.COBBLESTONE_WALL,
            Material.OAK_SLAB,Material.COBBLESTONE_SLAB
    )),
    ;

    public final Map<Material, Material> replacementMap;

    StructurePallets(Map<Material, Material> map) {
        this.replacementMap = map;
    }
}
