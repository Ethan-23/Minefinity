package org.evasive.me.minefinity.forge.data;

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
