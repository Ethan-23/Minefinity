package org.evasive.me.minefinity.resourceblock;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;

public record BaseBlock(String name, Material material, int health, CustomItem blockDrop, int unlockCost) {

}
