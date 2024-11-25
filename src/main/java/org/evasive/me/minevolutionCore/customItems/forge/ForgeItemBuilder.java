package org.evasive.me.minevolutionCore.customItems.forge;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemBuilder;

import java.util.List;

public interface ForgeItemBuilder extends ItemBuilder {
    int getCraftAmount();
    List<ItemStack> getRecipe();
    int getCraftTime();
}
