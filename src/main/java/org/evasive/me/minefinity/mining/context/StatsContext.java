package org.evasive.me.minefinity.mining.context;

public class StatsContext {

    private float speed = 1f;
    private float fortune = 1f;

    public void addSpeed(float amount) {
        this.speed += amount;
    }
    public void multiplySpeed(float amount) {
        this.speed *= amount;
    }

    public void addFortune(float amount) {
        this.fortune += amount;
    }
    public void multiplyFortune(float amount) {
        this.fortune *= amount;
    }

    public float getSpeed() {
        return speed;
    }
    public float getFortune() {
        return fortune;
    }

}
