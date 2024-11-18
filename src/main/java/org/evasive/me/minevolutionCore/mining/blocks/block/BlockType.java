package org.evasive.me.minevolutionCore.mining.blocks.block;

public enum BlockType {
    OAK_LOG(new OakLog()),
    COBBLESTONE(new Cobblestone()),
    STONE(new Stone()),
    COAL_ORE(new CoalOre()),
    DIORITE(new Diorite()),
    POLISHED_DIORITE(new PolishedDiorite()),
    COPPER_ORE(new CopperOre()),
    RAW_COPPER_BLOCK(new RawCopperBlock()),
    COPPER_BLOCK(new CopperBlock())
//    ANDESITE,
//    POLISHED_ANDESITE,
//    IRON_ORE,
//    RAW_IRON,
//    IRON_BLOCK,
//    GRANITE,
//    POLISHED_GRANITE,
//    LAPIS_ORE,
//    TUFF,
//    DEEPSLATE,
//    REDSTONE_ORE,
//    CALCITE,
//    AMETHYST,
//    GOLD_ORE,
//    RAW_GOLD_BLOCK,
//    GOLD_BLOCK,
//    COAL_BLOCK,
//    DIAMOND_ORE,
//    OBSIDIAN,
//    DIAMOND_BLOCK,
//    EMERALD_ORE,
//    EMERALD_BLOCK
    ;
    private final BlockBuilder blockBuilder;

    // Constructor for BlockType enum
    BlockType(BlockBuilder blockCreator) {
        this.blockBuilder = blockCreator;
    }

    public BlockBuilder getBlockCreator() {
        return blockBuilder;
    }

}
