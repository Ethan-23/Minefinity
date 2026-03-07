package org.evasive.me.minefinity.customItems.framework;

import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;

public class CustomItemStack {
    private final BaseCustomItem customItem;
    int amount;

    public CustomItemStack(BaseCustomItem customItem, int amount) {
        this.customItem = customItem;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BaseCustomItem getCustomItem() {
        return customItem;
    }
}
