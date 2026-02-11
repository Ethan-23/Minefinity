package org.evasive.me.minevolutionCore.resourceblock;

import org.bukkit.Material;
import org.evasive.me.minevolutionCore.core.items.CustomItem;

public record BaseBlock(String name, Material material, int health, CustomItem blockDrop, int unlockCost) {

}
