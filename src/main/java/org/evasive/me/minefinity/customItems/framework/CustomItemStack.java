package org.evasive.me.minefinity.customItems.framework;

import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.types.ResourceItem;

public class CustomItemStack {
    private final ResourceItem customItem;
    int amount;

    public CustomItemStack(ResourceItem customItem, int amount) {
        this.customItem = customItem;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }
}
