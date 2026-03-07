package org.evasive.me.minefinity.mining.utils;

import java.util.LinkedList;
import java.util.Queue;

public class AnimationIDs {

    private int nextId = 2147483647;
    private final Queue<Integer> recycledIds = new LinkedList<>();

    public int getUniqueAnimationId() {
        Integer id = recycledIds.poll();
        if (id != null) {
            return id;
        }
        return nextId--;
    }

    public void releaseAnimationId(int id) {
        recycledIds.offer(id);
    }


}
