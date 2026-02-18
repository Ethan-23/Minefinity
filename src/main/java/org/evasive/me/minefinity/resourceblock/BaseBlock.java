package org.evasive.me.minefinity.resourceblock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.evasive.me.minefinity.core.items.CustomItem;

import java.util.List;

public record BaseBlock(String name, Material material, int health, CustomItem blockDrop, int unlockCost, List<Integer> milestoneUnlocks) {

}
