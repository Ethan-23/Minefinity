package org.evasive.me.minevolutionCore.resourceblock.block.gui;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.core.gui.BaseGui;
import org.evasive.me.minevolutionCore.resourceblock.BaseBlock;
import org.evasive.me.minevolutionCore.resourceblock.BlockType;
import org.evasive.me.minevolutionCore.resourceblock.milestones.MilestoneFunctions;
import org.evasive.me.minevolutionCore.utils.ItemBuilder;
import org.evasive.me.minevolutionCore.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minevolutionCore.MinevolutionCore.playerManager;
import static org.evasive.me.minevolutionCore.utils.EnchantUtils.intToRoman;
import static org.evasive.me.minevolutionCore.utils.GenericGuiItems.fillerPane;

public class BlockGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    static final List<Integer> TRACK = List.of(0, 9, 18, 27, 36, 45, 46, 47, 38, 29, 20, 11, 2, 3, 4, 13, 22, 31, 40, 49, 50, 51, 42, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53);
    private BlockGuiHandler blockGuiHandler = new  BlockGuiHandler();

    public BlockGui(Player player) {
        super(player, INVENTORY_SIZE, Messages.parse("Blocks"));
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
            boolean unlocked = MinevolutionCore.playerManager.getBlockTier(player) >= blockTier;

            Component name = Messages.parse("<gray>(<" + (unlocked ? "white" : "red") + ">" + intToRoman(blockTier+1) +"<gray>) <white>" + block.name());
            ItemBuilder blockBuilder = new ItemBuilder(block.material(), name);
            blockBuilder.addLore(unlocked ? getBlockLore(block, player) : getLockedBlockLore(block));
            if(blockTier == MinevolutionCore.playerManager.getSelectedBlockTier(player))
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
     * @param block BaseBlock data
     * @param player Player being sent the inventory
     * @return Unlocked block lore
     */
    private List<String> getBlockLore(BaseBlock block, Player player){
        String blockHealth = String.valueOf(block.health());
        int mTier = MilestoneFunctions.getTier(player, block.material());
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

        if(blockTier > playerManager.getBlockTier(player)){
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
