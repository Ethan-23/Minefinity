package org.evasive.me.minefinity.resourceblock.gui;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.player.sevices.MilestoneService;
import org.evasive.me.minefinity.resourceblock.BaseBlock;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.TextConversions.intToRoman;

public class BlockGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    static final List<Integer> TRACK = List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34, 38, 39, 40, 41, 42);
    private BlockGuiHandler blockGuiHandler = new  BlockGuiHandler();

    private BlockTierService blockTierService;
    private final MilestoneService milestoneService;

    public BlockGui(Player player) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Blocks"));
        this.blockTierService = Minefinity.core.getBlockTierService();
        this.milestoneService = Minefinity.core.getMilestoneService();
        build();
    }

    /**
     * Creates the inventory
     */
    @Override
    protected void build() {
        buildFrame();
        buildBlocks(player);
    }

    /**
     * Builds the frame of the gui
     */
    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (!TRACK.contains(i)) this.inventory.setItem(i, fillerPane);
        }
    }

    /**
     * Builds the block path in the gui.
     * @param player player whose data being used to build.
     */
    private void buildBlocks(Player player){
        BlockType[] blockList = BlockType.values();
        for(int blockTier = 0; blockTier < TRACK.size(); blockTier++){

            if(blockTier >= blockList.length) break;

            BaseBlock block = blockList[blockTier].getBlock();
            boolean unlocked = blockTierService.getBlockTier(player).ordinal() >= blockTier;

            Component name = TextConversions.parse("<gray>(<" + (unlocked ? "white" : "red") + ">" + intToRoman(blockTier+1) +"<gray>) <white>" + block.name());
            ItemBuilder blockBuilder = new ItemBuilder(block.material(), name);
            blockBuilder.addLore(unlocked ? getBlockLore(BlockType.values()[blockTier], player) : getLockedBlockLore(block));
            if(blockTier == blockTierService.getSelectedBlockTier(player))
                setSelected(blockBuilder);
            this.inventory.setItem(TRACK.get(blockTier), blockBuilder.build());
        }
    }

    /**
     * Adds a glow and lore to show the selected block.
     * @param blockBuilder ItemBuilder being added to.
     */
    private void setSelected(ItemBuilder blockBuilder){
        blockBuilder.addLore("<bold><green>Selected").addGlow();
    }


    /**
     * @param blockType BlockType of lore being created
     * @param player Player being sent the inventory
     * @return Unlocked block lore
     */
    private List<String> getBlockLore(BlockType blockType, Player player){
        String blockHealth = String.valueOf(blockType.getBlock().health());
        int mTier = milestoneService.getTier(player, blockType);
        String milestoneTier = mTier == 0 ? "0" : intToRoman(mTier);

        return List.of(
                "<gray>Block Health: <white>" + blockHealth,
                "<gray>Milestone Tier: <aqua>" + milestoneTier,
                "",
                "<#a1a1a1>Left-Click <gray>to Select",
                "<#a1a1a1>Right-Click <gray>to view Milestones"
        );
    }

    /**
     * @param block BaseBlock data
     * @return Locked block lore
     */
    private List<String> getLockedBlockLore(BaseBlock block){
        String unlockCost = String.valueOf(block.unlockCost());

        return List.of(
                "<gray>Cost: $" + unlockCost,
                "",
                "<gray>Left-Click to purchase",
                "<bold><red>Locked"
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        e.setCancelled(true);

        int slot = e.getSlot();

        int blockTier = TRACK.indexOf(slot);

        if(blockTier == -1 || blockTier >= BlockType.values().length) return;

        Player player = (Player) e.getWhoClicked();

        if(blockTier > blockTierService.getBlockTier(player).ordinal()){
            if(blockGuiHandler.lockedBlockClicked(player, blockTier))
                rebuildInventory();
            return;
        }

        if(e.getClick().isLeftClick()){
            blockGuiHandler.handleSelect(player, blockTier);
            rebuildInventory();
        }

        if(e.getClick().isRightClick()){
            blockGuiHandler.handleMilestone(player, blockTier);
        }
    }
}
