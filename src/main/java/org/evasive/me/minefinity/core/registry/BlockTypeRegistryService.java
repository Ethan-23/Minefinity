package org.evasive.me.minefinity.core.registry;

import org.evasive.me.minefinity.core.registry.BlockTypeRegistry;
import org.evasive.me.minefinity.core.data.BaseBlock;

import java.util.List;

public class BlockTypeRegistryService {

    private final BlockTypeRegistry blockTypeRegistry;
    private static BlockTypeRegistryService blockTypeRegistryService;

    public static BlockTypeRegistryService getInstance() {
        return blockTypeRegistryService;
    }

    public BlockTypeRegistryService(BlockTypeRegistry blockTypeRegistry) {
        this.blockTypeRegistry = blockTypeRegistry;
        blockTypeRegistryService = this;
    }

    public BaseBlock getBaseBlock(String blockId){
        return blockTypeRegistry.getBlock(blockId);
    }

    public String getBlockIdByTier(String worldName, int tier){
        return blockTypeRegistry.getBlockList(worldName).get(tier);
    }

    public boolean hasWorldUnlocked(String worldName){
        return blockTypeRegistry.hasWorld(worldName);
    }

    public List<String> getBlockList(String worldName){
        return blockTypeRegistry.getBlockList(worldName);
    }

    public int getBlockTier(String worldName, String blockId){
        return blockTypeRegistry.getBlockList(worldName).indexOf(blockId);
    }

    public float getNextUnlockCost(int currentTier, String worldName){
        List<String> blockList = blockTypeRegistry.getBlockList(worldName);
        if(blockList.size() < currentTier + 2) // current tier starts at 0
            return 0f;
        return blockTypeRegistry.getBlockTypes().get(blockList.get(currentTier + 1)).unlockCost();
    }

    public List<BaseBlock> getAllBlocks(){
        return blockTypeRegistry.getBlockTypes().values().stream().toList();
    }

    public List<String> getWorldList() {
        return blockTypeRegistry.getWorldList();
    }
}
