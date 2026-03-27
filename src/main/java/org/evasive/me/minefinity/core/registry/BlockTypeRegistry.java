package org.evasive.me.minefinity.core.registry;

import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;

import java.util.*;

public class BlockTypeRegistry {

    //TrackId, List<BlockIds>
    private final Map<String, List<String>> blockTracks;
    //BlockId, BaseBlock
    private final Map<String, BaseBlock> blockTypes;

    public BlockTypeRegistry() {
        this.blockTracks = new HashMap<>();
        this.blockTypes = new HashMap<>();
    }

    public void registerBlockTrack(String trackName) {
        blockTracks.put(trackName, new ArrayList<>());
    }

    public void registerBlock(String trackName, BaseBlock block) {
        if(!blockTracks.containsKey(trackName))
            throw new IllegalArgumentException("Block track " + trackName + " is not registered.");
        blockTracks.get(trackName).add(block.name());
        blockTypes.put(block.name(), block);
    }

    public BaseBlock getBlock(String blockId) {
        return blockTypes.get(blockId);
    }

    public List<String> getBlockList(String trackName) {
        return Collections.unmodifiableList(
                blockTracks.getOrDefault(trackName, List.of())
        );
    }

    public Map<String, BaseBlock> getBlockTypes(){
        return Collections.unmodifiableMap(this.blockTypes);
    }
}
