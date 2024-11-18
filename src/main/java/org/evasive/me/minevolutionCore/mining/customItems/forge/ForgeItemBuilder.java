package org.evasive.me.minevolutionCore.mining.customItems.forge;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.mining.customItems.ItemBuilder;

import java.util.List;

public interface ForgeItemBuilder extends ItemBuilder {
    public int getCraftAmount();
    public List<ItemStack> getRecipe();
    public int getCraftTime();
}
