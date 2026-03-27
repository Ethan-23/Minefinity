package org.evasive.me.minefinity.core.npcs;

import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NpcBehaviorRegistry {

    Map<String, Supplier<NpcBehavior>> behaviors;

    public NpcBehaviorRegistry() {
        behaviors = new HashMap<>();
    }

    public void addBehavior(String npcName, Supplier<NpcBehavior> behavior) {
        behaviors.put(npcName, behavior);
    }

    public Supplier<NpcBehavior> getBehavior(String id) {
        return behaviors.get(id);
    }

}
