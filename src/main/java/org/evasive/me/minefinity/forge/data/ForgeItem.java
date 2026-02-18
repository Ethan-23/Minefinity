package org.evasive.me.minefinity.forge.data;

import org.bukkit.inventory.ItemStack;

public class ForgeItem {
    long timeFinished;
    ItemStack itemStack;

    public ForgeItem(long timeFinished, ItemStack itemStack) {
        this.timeFinished = timeFinished;
        this.itemStack = itemStack;
    }

    public long getTimeFinished() {
        return timeFinished;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
