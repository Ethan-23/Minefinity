package org.evasive.me.minefinity.towns.structures.mines.miner.gui.selection;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.framework.ItemPickupService;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.mines.miner.service.AutoMinerService;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.TextConversions.intToRoman;
import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.backPage;

public class MinerBlockSelectionGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    static final List<Integer> TRACK = List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34, 38, 39, 40, 41, 42);
    public static final int BACK_SLOT = 45;
    public static final int NONE_SLOT = TRACK.getFirst();

    private final MinerBlockSelectionHandler minerBlockSelectionHandler;
    private final AutoMinerService autoMinerService;
    private final BlockTierService blockTierService;

    public MinerBlockSelectionGui(Player player, CustomItemRegistryService customItemRegistryService, ItemPickupService itemPickupService, AutoMinerService autoMinerService, BlockTierService blockTierService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Block Selection"));
        this.autoMinerService = autoMinerService;
        this.blockTierService = blockTierService;
        this.minerBlockSelectionHandler = new MinerBlockSelectionHandler(this.blockTierService, itemPickupService, customItemRegistryService,  this.autoMinerService);
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        setupButtons();
        buildBlocks(player);
    }

    private void buildBlocks(Player player){
        ItemBuilder noneBuilder = new ItemBuilder(Material.BARRIER, TextConversions.parse("<red>NONE"));
        if(autoMinerService.getAutoMinerBlockType(player) == null)
            setSelected(noneBuilder);
        inventory.setItem(NONE_SLOT, noneBuilder.build());
        List<String> blockList = blockTierService.getBlockTypeRegistryService().getBlockList(player.getWorld().getName());
        for(int blockTier = 0; blockTier < TRACK.size(); blockTier++){

            if(blockTier >= blockList.size()) break;

            String blockId = blockList.get(blockTier);
            BaseBlock baseBlock = blockTierService.getBlockTypeRegistryService().getBaseBlock(blockId);
            boolean unlocked = autoMinerService.getAutoMinerUnlockedBlockTier(player) >= blockTier;

            Component name = TextConversions.parse("<gray>(<" + (unlocked ? "white" : "red") + ">" + intToRoman(blockTier+1) +"<gray>) <white>" + baseBlock.name());
            ItemBuilder blockBuilder = new ItemBuilder(baseBlock.material(), name);
            blockBuilder.addLore(unlocked ? getBlockLore(baseBlock) : getLockedBlockLore());

            String autoMinerBlockId = autoMinerService.getAutoMinerBlockType(player);
            int tier = blockList.indexOf(autoMinerBlockId);
            BaseBlock selectedBaseBlock = blockTierService.getBlockTypeRegistryService().getBaseBlock(autoMinerBlockId);
            if(selectedBaseBlock != null && blockTier == tier)
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

    private List<String> getBlockLore(BaseBlock baseBlock){
        String blockHealth = String.valueOf(baseBlock.health());

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

        if(blockTier == -1 || blockTier >= blockTierService.getBlockTypeRegistryService().getBlockList(player.getWorld().getName()).size()) return;

        minerBlockSelectionHandler.handleBlockSelection(player, blockTier);
        rebuildInventory();
    }
}
