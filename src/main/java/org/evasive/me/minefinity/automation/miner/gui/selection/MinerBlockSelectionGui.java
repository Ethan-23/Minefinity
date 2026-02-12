package org.evasive.me.minefinity.automation.miner.gui.selection;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.resourceblock.BaseBlock;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;
import org.jetbrains.annotations.NotNull;

import static org.evasive.me.minefinity.utils.GenericGuiItems.*;

public class MinerBlockSelectionGui extends BaseGui {

    private static final int INVENTORY_SIZE = 36;
    public static final int BACK_SLOT = 27;
    public static final int NONE_SLOT = 0;
    MinerBlockSelectionHandler minerBlockSelectionHandler = new MinerBlockSelectionHandler();

    public MinerBlockSelectionGui(Player player) {
        super(player, INVENTORY_SIZE, Messages.parse("Block Selection"));
        build();
    }

    @Override
    protected void build() {
        setupBackground();
        setupButtons();
        setupBlocks(player);
    }

    private void setupBlocks(Player player) {

        BlockType[] blockList = BlockType.values();
        BlockType blockType = Minefinity.playerManager.getPlayerData(player).getAutoMiner().getBlockType();
        ItemBuilder none = new ItemBuilder(noneBarrier.clone());

        if(blockType == null)
            none.addGlow().addLore("<green>Selected");

        for(int blockTier = 0; blockTier < BlockType.values().length; blockTier++){

            BaseBlock block = blockList[blockTier].getBlock();
            boolean unlocked = Minefinity.playerManager.getBlockTier(player) >= blockTier;

            ItemBuilder blockBuilder = new ItemBuilder(block.material(), Messages.parse(block.name()));

            if(blockType != null && blockTier == blockType.ordinal())
                blockBuilder.addGlow().addLore("<green>Selected");

            this.inventory.setItem(blockTier + 1, unlocked ? blockBuilder.build() : lockedPane);
        }
        inventory.setItem(NONE_SLOT, none.build());
    }

    private void setupButtons() {
        inventory.setItem(BACK_SLOT, backPage);
    }

    private void setupBackground() {
        for (int slot = 0; slot < INVENTORY_SIZE; slot++) {
            inventory.setItem(slot, fillerPane);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        int clickedSlot = e.getSlot();

        if(clickedSlot == MinerBlockSelectionGui.BACK_SLOT){
            minerBlockSelectionHandler.handleBackButton(player);
            return;
        }

        if(clickedSlot == MinerBlockSelectionGui.NONE_SLOT){
            minerBlockSelectionHandler.handleNoneSelection(player);
            rebuildInventory();
            return;
        }

        if(BlockType.values().length >= clickedSlot){
            minerBlockSelectionHandler.handleBlockSelection(player, clickedSlot);
            rebuildInventory();
        }
    }
}
