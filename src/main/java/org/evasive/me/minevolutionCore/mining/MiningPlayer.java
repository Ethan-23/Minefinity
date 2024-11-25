package org.evasive.me.minevolutionCore.mining;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MiningPlayer {
    private BukkitTask miningTask;
    private BukkitRunnable swingCooldown;
    private float progress;
    private boolean isMining;
    private boolean isSwining;

    public MiningPlayer (boolean isMining, boolean isSwining, float progress, BukkitTask miningTask, BukkitRunnable swingCooldown){
        this.isMining = isMining;
        this.isSwining = isSwining;
        this.progress = progress;
        this.miningTask = miningTask;
        this.swingCooldown = swingCooldown;
    }

    public BukkitTask getMiningTask() {
        return miningTask;
    }

    public void setMiningTask(BukkitTask bukkitTask) {
        this.miningTask = bukkitTask;
    }

    public BukkitRunnable getSwingCooldown() {
        return swingCooldown;
    }

    public void setSwingCooldown(BukkitRunnable bukkitTask) {
        this.swingCooldown = bukkitTask;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isMining() {
        return isMining;
    }

    public void setMining(boolean mining) {
        isMining = mining;
    }

    public boolean isSwinging() {
        return isSwining;
    }

    public void setSwinging(boolean swinging) {
        isSwining = swinging;
    }
}
