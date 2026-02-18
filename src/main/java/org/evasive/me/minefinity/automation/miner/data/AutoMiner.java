package org.evasive.me.minefinity.automation.miner.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.evasive.me.minefinity.mining.BlockProgress;
import org.evasive.me.minefinity.resourceblock.BlockType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemFunctions.getPickaxeItem;

public class AutoMiner {

    private ItemStack pickaxe;
    private BlockType blockType;
    private Map<String, Integer> itemStorage;
    private int level;
    private int offlineProgress;
    private int storageCap;
    private float storedProgress;

    public AutoMiner() {
        this.pickaxe = null;
        this.blockType = null;
        this.storedProgress = 0;
        setLevel(1);
        this.itemStorage = new HashMap<>();
    }

    public ItemStack getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(ItemStack pickaxe) {
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

    public void addProgress() {

        float blockHardness = blockType.getBlock().health();
        float progressPerTick = getPickaxeItem(pickaxe).getBaseMiningSpeed();

        if(progressPerTick > blockHardness / BlockProgress.MAX_SPEED_DENOMINATION)
            progressPerTick = blockHardness / BlockProgress.MAX_SPEED_DENOMINATION;

        storedProgress += progressPerTick * 20;

        int blocksMined = (int) (storedProgress / blockHardness);

        if (blocksMined > 0) {
            storedProgress -= blocksMined * blockHardness;
            addItemStorage(blockType.getBlock().blockDrop().getID(), blocksMined);
        }
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

    public String serializePickaxe() {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(byteOut);
            out.writeObject(pickaxe);
            out.close();
            return Base64.getEncoder().encodeToString(byteOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public ItemStack deserializePickaxe(String data) {

        if(data == null) return null;

        try {
            byte[] bytes = Base64.getDecoder().decode(data);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream in = new BukkitObjectInputStream(byteIn);
            ItemStack item = (ItemStack) in.readObject();
            in.close();
            return item;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
