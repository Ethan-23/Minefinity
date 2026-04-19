package org.evasive.me.minefinity.towns.structures.resourceblock.framework;

import org.bukkit.Material;

import java.util.List;

public record BaseBlock(String name, Material material, int breakingPower, int health, String blockDropId, String specialBlockDropId, float unlockCost, List<Integer> milestoneUnlocks) {

}
