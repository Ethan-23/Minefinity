package org.evasive.me.minefinity.miner;

import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AutoMiner {

    private BasePickaxeItem pickaxe;
    private BlockType blockType;
    private Map<String, Integer> itemStorage;
    private int level;
    private int offlineProgress;
    private int storageCap;
    private float storedProgress;
    private long lastOpened;

    public AutoMiner() {
        this.pickaxe = null;
        this.blockType = null;
        this.storedProgress = 0;
        setLevel(1);
        this.itemStorage = new HashMap<>();
        this.lastOpened = System.currentTimeMillis();
    }

    public BasePickaxeItem getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(BasePickaxeItem pickaxe) {
        this.pickaxe = pickaxe;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.storedProgress = 0;
        this.blockType = blockType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        if(level == 1){
            this.storageCap = 128;
            this.offlineProgress = 30;
        }
    }

    public void setItemStorage(Map<String, Integer> itemStorage) {
        this.itemStorage = itemStorage;
    }

    public Map<String, Integer> getItemStorage() {
        return Collections.unmodifiableMap(itemStorage);
    }

    public void addItemStorage(String itemId, int amount) {
        if(!itemStorage.containsKey(itemId)){
            itemStorage.put(itemId, 0);
        }

        int amountStored = itemStorage.get(itemId);
        int totalStorage = getStoredBlockAmount();

        if(totalStorage + amount > storageCap)
            amount = storageCap - totalStorage;

        itemStorage.put(itemId, amount + amountStored);
    }

    public void removeItemStorage(String itemId, int amount) {
        if(!itemStorage.containsKey(itemId))
            return;
        itemStorage.put(itemId, itemStorage.get(itemId) - amount);
    }

    public int getOfflineProgress() {
        return offlineProgress;
    }

    public void setOfflineProgress(int offlineProgress) {
        this.offlineProgress = offlineProgress;
    }

    public int getStorageCap() {
        return storageCap;
    }

    public void setStorageCap(int storageCap) {
        this.storageCap = storageCap;
    }

    public float getStoredProgress() {
        return storedProgress;
    }

    public void setStoredProgress(float storedProgress) {
        this.storedProgress = storedProgress;
    }

    public long getLastUpdated() {
        return lastOpened;
    }

    public void setLastUpdated(long lastOpened) {
        this.lastOpened = lastOpened;
    }

    public int getStoredBlockAmount(){
        int storedItems = 0;
        for (Map.Entry<String, Integer> entry : itemStorage.entrySet()) {
            storedItems += entry.getValue();
        }
        return storedItems;
    }

    public boolean isFull() {
        return getStoredBlockAmount() >= storageCap;
    }

}
