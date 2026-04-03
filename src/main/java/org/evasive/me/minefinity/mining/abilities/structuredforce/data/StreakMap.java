package org.evasive.me.minefinity.mining.abilities.structuredforce.data;

import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StreakMap {

    Map<UUID, BlockStreak> streakMap;

    public StreakMap() {
        this.streakMap = new HashMap<>();
    }

    public int getStreak(UUID uuid) {
        if(!streakMap.containsKey(uuid))
            return 0;
        return streakMap.get(uuid).getAmount();
    }

    public BaseBlock getBaseBlock(UUID uuid) {
        if(!streakMap.containsKey(uuid))
            return null;
        return streakMap.get(uuid).getBaseBlock();
    }

    public void addStreak(UUID uuid, BaseBlock baseBlock) {
        if(streakMap.containsKey(uuid) && streakMap.get(uuid).getBaseBlock().equals(baseBlock)) {
            streakMap.get(uuid).increaseAmount(1);
        }else {
            streakMap.put(uuid, new BlockStreak(baseBlock, 1));
        }
    }
}
