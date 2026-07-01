package org.evasive.me.minefinity.mining.abilities.structuredforce.data;

import org.evasive.me.minefinity.core.data.BaseBlock;

public class BlockStreak {

    private BaseBlock baseBlock;
    private int amount;

    public BlockStreak(BaseBlock baseBlock, int amount) {
        this.baseBlock = baseBlock;
        this.amount = amount;
    }

    public BaseBlock getBaseBlock() {
        return baseBlock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }
}
