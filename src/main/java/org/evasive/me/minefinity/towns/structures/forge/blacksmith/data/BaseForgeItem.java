package org.evasive.me.minefinity.towns.structures.forge.blacksmith.data;

public class BaseForgeItem {
    long timeFinished;
    String itemId;

    public BaseForgeItem(long timeFinished, String itemId) {
        this.timeFinished = timeFinished;
        this.itemId = itemId;
    }

    public long getTimeFinished() {
        return timeFinished;
    }

    public String getResultItemId() {
        return itemId;
    }
}
