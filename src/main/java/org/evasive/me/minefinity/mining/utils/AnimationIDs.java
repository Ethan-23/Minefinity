package org.evasive.me.minefinity.mining.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AnimationIDs {

    private int nextId = 2147483647;
    private final Set<Integer> trackQueue = new HashSet<>();
    private final Queue<Integer> recycledIds = new LinkedList<>();

    public int getUniqueAnimationId() {
        Integer id = recycledIds.poll();
        if (id != null) {
            trackQueue.remove(id);
            return id;
        }
        return nextId--;
    }

    public void releaseAnimationId(int id) {
        if(trackQueue.contains(id))
            return;
        trackQueue.add(id);
        recycledIds.offer(id);
    }


}
