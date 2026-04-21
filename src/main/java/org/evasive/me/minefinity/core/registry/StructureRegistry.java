package org.evasive.me.minefinity.core.registry;

import org.evasive.me.minefinity.towns.structures.data.Structure;

import java.util.*;

public class StructureRegistry {

    private final Map<String, Structure> STRUCTURE_MAP = new LinkedHashMap<>();

    public void registerStructure(Structure structure){
        if(STRUCTURE_MAP.containsKey(structure.id()))
            return;
        STRUCTURE_MAP.put(structure.id(), structure);
    }

    public Structure getStructure(String id){
        return STRUCTURE_MAP.get(id);
    }

    public Map<String, Structure> getStructureMapView(){
        return Collections.unmodifiableMap(STRUCTURE_MAP);
    }

    public Collection<Structure> getStructureList() {
        return STRUCTURE_MAP.values();
    }

    public boolean hasId(String structureName) {
        return STRUCTURE_MAP.containsKey(structureName);
    }
}
