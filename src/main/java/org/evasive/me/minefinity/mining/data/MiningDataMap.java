package org.evasive.me.minefinity.mining.data;

import org.bukkit.Location;
import org.evasive.me.minefinity.mining.utils.AnimationIDs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningDataMap {

    private final Map<Location, Map<UUID, MiningBlockData>> miningMap = new HashMap<>();
    private final AnimationIDs animationIDs;

    public MiningDataMap(AnimationIDs animationIDs){
        this.animationIDs = animationIDs;
    }

    public void addMiningData(Location location, UUID uuid, MiningBlockData miningBlockData){
        Map<UUID, MiningBlockData> blockDataMap = containsBlockLocation(location) ? miningMap.get(location) : new HashMap<>();

        if(blockDataMap.containsKey(uuid))
            return;

        blockDataMap.put(uuid, miningBlockData);
        miningMap.put(location, blockDataMap);
    }

    public void removeBlockPos(Location location, UUID uuid){
        animationIDs.releaseAnimationId(miningMap.get(location).get(uuid).getAnimationID());
        miningMap.get(location).remove(uuid);
    }

    public boolean containsBlockLocation(Location location){
        return miningMap.containsKey(location);
    }

    public boolean containsPlayerAtLocation(Location location, UUID uuid){
        return containsBlockLocation(location) && miningMap.get(location).containsKey(uuid);
    }

    public void increaseBlockProgress(Location location, UUID uuid, float progress){
        miningMap.get(location).get(uuid).setProgress(miningMap.get(location).get(uuid).getProgress() + progress);
    }

    public float getBlockProgress(Location location, UUID uuid){
        return miningMap.get(location).get(uuid).getProgress();
    }

    public int getBlockAnimationID(Location location, UUID uuid){
        return miningMap.get(location).get(uuid).getAnimationID();
    }

}
