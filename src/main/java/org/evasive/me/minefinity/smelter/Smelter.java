package org.evasive.me.minefinity.smelter;

import org.evasive.me.minefinity.customItems.framework.CustomItemStack;
import org.evasive.me.minefinity.customItems.types.FuelItem;

import java.util.HashMap;
import java.util.Map;

public class Smelter {
    private CustomItemStack[] inventoryItems; // set size to 9
    private FuelItem fuelTier;
    private int totalFuel;
    private int remainingFuelEfficiency;
    private String currentlySmelting;
    private int currentSmeltProgress;
    private Map<String, Integer> output;
    private long lastOpened;
    public final int OFFLINE_TIME_CAP = 30;

    public Smelter(){
        this.inventoryItems = new CustomItemStack[9];
        this.fuelTier = null;
        this.totalFuel = 0;
        this.remainingFuelEfficiency = 0;
        this.currentlySmelting = null;
        this.currentSmeltProgress = 0;
        this.lastOpened = System.currentTimeMillis();
        this.output = new HashMap<>();
    }

    public CustomItemStack[] getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(CustomItemStack[] inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public FuelItem getFuelTier() {
        return fuelTier;
    }

    public void setFuelTier(FuelItem fuelTier) {
        this.fuelTier = fuelTier;
    }

    public int getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(int totalFuel) {
        this.totalFuel = totalFuel;
    }

    public String getCurrentlySmelting() {
        return currentlySmelting;
    }

    public void setCurrentlySmelting(String currentlySmelting) {
        this.currentlySmelting = currentlySmelting;
    }

    public Map<String, Integer> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Integer> output) {
        this.output = output;
    }

    public int getRemainingFuelEfficiency() {
        return remainingFuelEfficiency;
    }

    public void setRemainingFuelEfficiency(int remainingFuelEfficiency) {
        this.remainingFuelEfficiency = remainingFuelEfficiency;
    }

    public int getCurrentSmeltProgress() {
        return currentSmeltProgress;
    }

    public void setCurrentSmeltProgress(int currentSmeltProgress) {
        this.currentSmeltProgress = currentSmeltProgress;
    }

    public boolean isEmpty() {
        for (CustomItemStack item : inventoryItems) {
            if(item != null){
                return false;
            }
        }
        return true;
    }

    public void removeItemStorage(String itemId, int amount) {
        if(!output.containsKey(itemId))
            return;
        output.put(itemId, output.get(itemId) - amount);
    }

    public long getLastUpdated() {
        return lastOpened;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastOpened = lastUpdated;
    }
}
