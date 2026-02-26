package org.evasive.me.minefinity.workshop.tools;

public class EngineerTools {
    private WorkshopToolsTiers resource;
    int resourceAmount;
    private WorkshopToolsTiers currentToolType;
    int currentToolDurability;

    public EngineerTools(){
        this.resource = null;
        this.resourceAmount = 0;
        this.currentToolType = null;
        this.currentToolDurability = 0;
    }

    public EngineerTools(WorkshopToolsTiers resource, int resourceAmount, WorkshopToolsTiers currentToolType, int currentToolDurability) {
        this.resource = resource;
        this.resourceAmount = resourceAmount;
        this.currentToolType = currentToolType;
        this.currentToolDurability = currentToolDurability;
    }

    public WorkshopToolsTiers getResource() {
        return resource;
    }

    public void setResource(WorkshopToolsTiers resource) {
        this.resource = resource;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    public WorkshopToolsTiers getCurrentToolType() {
        return currentToolType;
    }

    public void setCurrentToolType(WorkshopToolsTiers currentToolType) {
        this.currentToolType = currentToolType;
    }

    public int getCurrentToolDurability() {
        return currentToolDurability;
    }

    public void setCurrentToolDurability(int currentToolDurability) {
        this.currentToolDurability = currentToolDurability;
    }
}
