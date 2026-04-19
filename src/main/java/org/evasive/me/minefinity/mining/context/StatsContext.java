package org.evasive.me.minefinity.mining.context;

import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.Map;

public class StatsContext {

    private int breakingPower = 0;
    private float speed = 0f;
    private float fortune = 0f;
    private int specialChance = 1;
    private boolean specialDrop = false;

    public void addBreakingPower(int amount){
        breakingPower += amount;
    }
    public void addSpeed(float amount) {
        this.speed += amount;
    }
    public void multiplySpeed(float amount) {
        this.speed *= amount;
    }

    public void addSpecialChance(int amount) {
        this.specialChance += amount;
    }

    public void addFortune(float amount) {
        this.fortune += amount;
    }
    public void multiplyFortune(float amount) {
        this.fortune *= amount;
    }

    public void setSpecialDrop(boolean specialDrop) {
        this.specialDrop = specialDrop;
    }

    public boolean isSpecialDrop() {
        return specialDrop;
    }

    public int getSpecialChance() {
        return specialChance;
    }

    public float getSpeed() {
        return speed;
    }
    public float getFortune() {
        return fortune;
    }
    public int getBreakingPower() {
        return breakingPower;
    }

    public void addStats(Map<String, Integer> playerStats) {
        speed += playerStats.getOrDefault(Stats.MINING_SPEED.name(), 0);
        fortune += playerStats.getOrDefault(Stats.MINING_FORTUNE.name(), 0);
        breakingPower += playerStats.getOrDefault(Stats.BREAKING_POWER.name(), 0);
    }


}
