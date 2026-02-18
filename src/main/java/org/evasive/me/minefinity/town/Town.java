package org.evasive.me.minefinity.town;

import java.util.Collections;
import java.util.Map;

public class Town {

    private Map<Structure, Integer> structureLevelMap;

    public Town() {
        structureLevelMap = Map.of(
                Structure.TOWNHALL, 1,
                Structure.MERCHANT, 0,
                Structure.FORGE, 0,
                Structure.WORKSHOP, 0
        );
    }

    public Map<Structure, Integer> getStructureLevelMap() {
        return structureLevelMap;
    }

    public void setStructureLevelMap(Map<Structure, Integer> structureLevelMap) {
        this.structureLevelMap = structureLevelMap;
    }
}
