package org.evasive.me.minefinity.towns.structures.resourceblock.framework;

import org.bukkit.Material;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItem;

import java.util.List;

public record BaseBlock(String name, Material material, int health, String blockDropId, float unlockCost, List<Integer> milestoneUnlocks) {

}
