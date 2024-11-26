package org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public class EnchantableItem {
    private ItemStack itemStack;
    private ItemDisplay itemDisplay;
    private int runnable;

    public EnchantableItem(ItemStack itemStack, ItemDisplay itemDisplay, int runnable) {
        this.itemStack = itemStack;
        this.itemDisplay = itemDisplay;
        this.runnable = runnable;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay;
    }

    public int getRunnable() {
        return runnable;
    }

    public void setRunnable(int runnable) {
        this.runnable = runnable;
    }
}
