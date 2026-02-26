package org.evasive.me.minefinity.resourceblock.framework;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;

import java.util.List;

public record BaseBlock(String name, Material material, int health, CustomItem blockDrop, int unlockCost, List<Integer> milestoneUnlocks) {

}
