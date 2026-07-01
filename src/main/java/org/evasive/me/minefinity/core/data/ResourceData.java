package org.evasive.me.minefinity.core.data;

public class ResourceData {
    private int tier;
    private int blockMined;
    private int progress;

    public ResourceData(int tier, int blockMined, int progress) {
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
}
