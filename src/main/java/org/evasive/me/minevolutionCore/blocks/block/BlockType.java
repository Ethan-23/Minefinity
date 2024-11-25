package org.evasive.me.minevolutionCore.blocks.block;

public enum BlockType {
    OAK_LOG(new OakLog()),
    COBBLESTONE(new Cobblestone()),
    STONE(new Stone()),
    COAL_ORE(new CoalOre()),
    DIORITE(new Diorite()),
    POLISHED_DIORITE(new PolishedDiorite()),
    COPPER_ORE(new CopperOre()),
    RAW_COPPER_BLOCK(new RawCopperBlock()),
    COPPER_BLOCK(new CopperBlock()),
    ANDESITE(new Andesite()),
    POLISHED_ANDESITE(new PolishedAndesite()),
    IRON_ORE(new IronOre()),
    RAW_IRON_BLOCK(new RawIronBlock()),
    IRON_BLOCK(new IronBlock()),
    GRANITE(new Granite()),
    POLISHED_GRANITE(new PolishedGranite()),
    LAPIS_ORE(new LapisOre()),
    TUFF(new Tuff()),
    DEEPSLATE(new Deepslate()),
    REDSTONE_ORE(new RedstoneOre()),
    CALCITE(new Calcite()),
    AMETHYST(new Amethyst()),
    GOLD_ORE(new GoldOre()),
    RAW_GOLD_BLOCK(new RawGoldBlock()),
    GOLD_BLOCK(new GoldBlock()),
    COAL_BLOCK(new CoalBlock()),
    DIAMOND_ORE(new DiamondOre()),
    OBSIDIAN(new Obsidian()),
    DIAMOND_BLOCK(new DiamondBlock()),
    EMERALD_ORE(new EmeraldOre()),
    EMERALD_BLOCK(new EmeraldBlock());
    private final BlockBuilder blockBuilder;

    // Constructor for BlockType enum
    BlockType(BlockBuilder blockCreator) {
        this.blockBuilder = blockCreator;
    }

    public BlockBuilder getBlockCreator() {
        return blockBuilder;
    }

}
