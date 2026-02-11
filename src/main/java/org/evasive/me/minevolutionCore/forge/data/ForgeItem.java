package org.evasive.me.minevolutionCore.forge.data;

import org.bukkit.inventory.ItemStack;

public class ForgeItem {
    long timeFinished;
    ItemStack itemStack;

    public ForgeItem(long timeFinished, ItemStack itemStack) {
        this.timeFinished = timeFinished;
        this.itemStack = itemStack;
    }
}
