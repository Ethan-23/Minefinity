package org.evasive.me.minefinity.town;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Town {

    private final Map<Structure, Integer> structureLevelMap;

    public Town() {
        structureLevelMap = new HashMap<>();
        structureLevelMap.put(Structure.TOWNHALL, 1);
        structureLevelMap.put(Structure.MERCHANT, 0);
        structureLevelMap.put(Structure.FORGE, 0);
        structureLevelMap.put(Structure.WORKSHOP, 0);
    }

    public Map<Structure, Integer> getStructureLevelMap() {
        return Collections.unmodifiableMap(structureLevelMap);
    }

    public void setStructureLevel(Structure structure, int level) {
        structureLevelMap.put(structure, level);
    }

    public int getStructureLevel(Structure structure) {
        return structureLevelMap.get(structure);
    }

}
