package org.evasive.me.minefinity.customItems.framework;

public class CustomItemStack {
    private final String customItemId;
    int amount;

    public CustomItemStack(String customItemId, int amount) {
        this.customItemId = customItemId;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomItem() {
        return customItemId;
    }
}
