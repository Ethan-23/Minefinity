package org.evasive.me.minefinity.core.data;

import org.bukkit.Material;

import java.util.List;

public record BaseBlock(String name, Material material, int breakingPower, int health, String blockDropId, String specialBlockDropId, float unlockCost, List<MilestoneTier> milestoneUnlocks) {

}
