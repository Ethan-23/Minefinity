package org.evasive.me.minefinity.mining.data;

public class MiningBlockData {

    private float progress;
    private int animationID;

    public MiningBlockData(int animationID, float progress) {
        this.animationID = animationID;
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getAnimationID() {
        return animationID;
    }

    public void setAnimationID(int animationID) {
        this.animationID = animationID;
    }
}
