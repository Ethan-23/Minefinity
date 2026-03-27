package org.evasive.me.minefinity.towns.structures.workshop.engineer.tools;

public class EngineerTools {
    private String resourceId;
    int resourceAmount;
    private String currentToolId;
    int currentToolDurability;

    public EngineerTools(){
        this.resourceId = null;
        this.resourceAmount = 0;
        this.currentToolId = null;
        this.currentToolDurability = 0;
    }

    public EngineerTools(String resourceId, int resourceAmount, String currentToolId, int currentToolDurability) {
        this.resourceId = resourceId;
        this.resourceAmount = resourceAmount;
        this.currentToolId = currentToolId;
        this.currentToolDurability = currentToolDurability;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    public String getCurrentToolId() {
        return currentToolId;
    }

    public void setCurrentToolId(String currentToolId) {
        this.currentToolId = currentToolId;
    }

    public int getCurrentToolDurability() {
        return currentToolDurability;
    }

    public void setCurrentToolDurability(int currentToolDurability) {
        this.currentToolDurability = currentToolDurability;
    }
}
