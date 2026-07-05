package org.evasive.me.minefinity.core.data;

public class MiningResourceData {
    private int tier;
    private int blockMined;
    private int progress;

    public MiningResourceData(int tier, int blockMined, int progress) {
        this.tier = tier;
        this.blockMined = blockMined;
        this.progress = progress;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getBlockMined() {
        return blockMined;
    }

    public void setBlockMined(int blockMined) {
        this.blockMined = blockMined;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void increaseBlocksMined(int amount){
        this.blockMined += amount;
    }

    public void increaseTier(int amount){
        this.tier += amount;
    }

    public void increaseProgress(int amount){
        this.progress += amount;
    }
}
