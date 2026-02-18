package org.evasive.me.minefinity.automation.miner.gui.selection;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.player.sevices.AutoMinerService;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.resourceblock.BaseBlock;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.utils.GenericGuiItems.*;
import static org.evasive.me.minefinity.utils.TextConversions.intToRoman;

public class MinerBlockSelectionGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    static final List<Integer> TRACK = List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34, 38, 39, 40, 41, 42);
    public static final int BACK_SLOT = 45;
    public static final int NONE_SLOT = TRACK.getFirst();

    MinerBlockSelectionHandler minerBlockSelectionHandler = new MinerBlockSelectionHandler();
    private final AutoMinerService autoMinerService;
    private final BlockTierService blockTierService;

    public MinerBlockSelectionGui(Player player) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Block Selection"));
        autoMinerService = Minefinity.getCore().getAutoMinerService();
        blockTierService = Minefinity.getCore().getBlockTierService();
        build();
    }

    @Override
    protected void build() {
        buildFrame();
        setupButtons();
        buildBlocks(player);
    }

    private void buildBlocks(Player player){
        ItemBuilder noneBuilder = new ItemBuilder(Material.BARRIER, TextConversions.parse("<red>NONE"));
        if(autoMinerService.getAutoMinerBlockType(player) == null)
            setSelected(noneBuilder);
        inventory.setItem(NONE_SLOT, noneBuilder.build());
        BlockType[] blockList = BlockType.values();
        for(int blockTier = 0; blockTier < TRACK.size(); blockTier++){

            if(blockTier >= blockList.length) break;

            BaseBlock block = blockList[blockTier].getBlock();
            boolean unlocked = blockTierService.getBlockTier(player).ordinal() >= blockTier;

            Component name = TextConversions.parse("<gray>(<" + (unlocked ? "white" : "red") + ">" + intToRoman(blockTier+1) +"<gray>) <white>" + block.name());
            ItemBuilder blockBuilder = new ItemBuilder(block.material(), name);
            blockBuilder.addLore(unlocked ? getBlockLore(BlockType.values()[blockTier]) : getLockedBlockLore());
            BlockType blockType = autoMinerService.getAutoMinerBlockType(player);
            if(blockType != null && blockTier == blockType.ordinal())
                setSelected(blockBuilder);
            this.inventory.setItem(TRACK.get(blockTier + 1), blockBuilder.build());
        }
    }

    private void setSelected(ItemBuilder blockBuilder){
        blockBuilder.addLore("<bold><green>Selected").addGlow();
    }

    private void setupButtons() {
        inventory.setItem(BACK_SLOT, backPage);
    }

    private void buildFrame(){
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (!TRACK.contains(i)) this.inventory.setItem(i, fillerPane);
        }
    }

    private List<String> getBlockLore(BlockType blockType){
        String blockHealth = String.valueOf(blockType.getBlock().health());

        return List.of(
                "<gray>Block Health: <white>" + blockHealth,
                "",
                "<#a1a1a1>Left-Click <gray>to Select"
        );
    }

    private List<String> getLockedBlockLore(){
        return List.of(
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

        int blockTier = TRACK.indexOf(clickedSlot);

        if(blockTier == -1 || blockTier >= BlockType.values().length) return;

        minerBlockSelectionHandler.handleBlockSelection(player, blockTier);
        rebuildInventory();
    }
}
