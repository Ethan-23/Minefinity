package org.evasive.me.minefinity.towns.structures.resourceblock.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.core.data.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.TextConversions.intToRoman;

public class BlockGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    static final List<Integer> TRACK = List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25);
    static final List<Integer> WORLD_TRACK = List.of(38, 39, 40, 41, 42);
    private final BlockGuiHandler blockGuiHandler;
    private final BlockTierService blockTierService;
    private final MilestoneService milestoneService;
    private String SELECTED_WORLD;

    public BlockGui(Player player, BlockTierService blockTierService, CustomItemRegistryService customItemRegistryService, MilestoneService milestoneService, EconomyService economyService, String worldId) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Blocks"));
        this.blockTierService = blockTierService;
        this.milestoneService = milestoneService;
        SELECTED_WORLD = worldId;
        this.blockGuiHandler = new BlockGuiHandler(milestoneService, customItemRegistryService, blockTierService, economyService);
        build();
    }

    /**
     * Creates the inventory
     */
    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildBlocks(player);
        buildWorlds();
    }

    private void buildWorlds(){
        List<String> worldList = blockTierService.getBlockTrackWorlds();
        for (int i = 0; i < WORLD_TRACK.size(); i++) {
            if(worldList.size() <= i) return;
            String worldId = worldList.get(i);
            String blockId = blockTierService.getBlockTypeRegistryService().getBlockList(worldId).getFirst();
            BaseBlock baseBlock = blockTierService.getBlockTypeRegistryService().getBaseBlock(blockId);
            CustomItemBuilder itemBuilder = new CustomItemBuilder(baseBlock.material(), TextConversions.formatItemName(worldId));

            if(!blockTierService.hasWorldUnlocked(player, worldId))
                itemBuilder.addLore("<red>Locked");
            else if(SELECTED_WORLD.equals(worldId))
                itemBuilder.addLore("<bold><green>Selected").addGlow();
            this.inventory.setItem(WORLD_TRACK.get(i), itemBuilder.build());
        }
    }

    /**
     * Builds the block path in the gui.
     * @param player player whose data being used to build.
     */
    private void buildBlocks(Player player){
        List<String> blockList = blockTierService.getBlockTypeRegistryService().getBlockList(SELECTED_WORLD);
        for(int blockTier = 0; blockTier < TRACK.size(); blockTier++){

            if(blockTier >= blockList.size()){
                this.inventory.setItem(TRACK.get(blockTier), null);
                continue;
            }

            String blockId = blockList.get(blockTier);
            BaseBlock block = blockTierService.getBlockTypeRegistryService().getBaseBlock(blockId);

            boolean unlocked = blockTierService.getUnlockedMiningBlock(player, SELECTED_WORLD) >= blockTier;
            boolean selected = blockTier == blockTierService.getSelectedMiningTier(player, SELECTED_WORLD);

            Component name = TextConversions.parse(
                    "<gray>(<" + (unlocked ? "white" : "red") + ">" + intToRoman(blockTier+1) +"<gray>) <white>" +
                            TextConversions.buildRarityColor(block.name(), CustomItemRegistryService.get().getRegisteredBaseItem(block.blockDropId()).getRarity()) +
                            (selected ? "<gray> - <green><bold>Selected" : ""));
            CustomItemBuilder blockBuilder = new CustomItemBuilder(block.material(), name);
            if(selected)
                blockBuilder.setGlow(true);
            blockBuilder.addLore(unlocked ? getBlockLore(blockId, block, player) : getLockedBlockLore(block));
            this.inventory.setItem(TRACK.get(blockTier), blockBuilder.build());
        }
    }

    /**
     * @param baseBlock BlockType of lore being created
     * @param player Player being sent the inventory
     * @return Unlocked block lore
     */
    private List<String> getBlockLore(String blockId, BaseBlock baseBlock, Player player){
        String blockHealth =  "" + baseBlock.health();
        String breakingPower =  "" + baseBlock.breakingPower();
        int mTier = milestoneService.getTier(player, blockId);
        String milestoneTier = mTier == 0 ? "0" : intToRoman(mTier);

        return List.of(
                Stats.BREAKING_POWER.getDisplay() + ": <white>" + breakingPower,
                Stats.HEALTH.getDisplay() + ": <white>" + blockHealth,
                "",
                "<green>Milestone Tier: <white>" + milestoneTier,
                "",
                "<#a1a1a1>Left-Click <gray>to <white>Select",
                "<#a1a1a1>Right-Click <gray>to <white>Open Milestones"
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

        if(WORLD_TRACK.contains(slot)){

            List<String> worldList = blockTierService.getBlockTrackWorlds();

            if(WORLD_TRACK.indexOf(slot) > worldList.size()-1)
                return;

            String clickedWorldId = blockTierService.getBlockTrackWorlds().get(WORLD_TRACK.indexOf(slot));

            if(!blockTierService.hasWorldUnlocked(player, clickedWorldId)){
                player.sendMessage(TextConversions.parse("<red>You have not unlocked this world yet!"));
                return;
            }

            SELECTED_WORLD = clickedWorldId;
            rebuildInventory();
        }

        int blockTier = TRACK.indexOf(slot);

        if(blockTier == -1 || blockTier >= blockTierService.getBlockTypeRegistryService().getBlockList(SELECTED_WORLD).size()) return;

        Player player = (Player) e.getWhoClicked();

        if(blockTier > blockTierService.getUnlockedMiningBlock(player, SELECTED_WORLD)){
            if(blockGuiHandler.lockedBlockClicked(player, blockTier, SELECTED_WORLD))
                rebuildInventory();
            return;
        }

        if(e.getClick().isLeftClick()){
            blockGuiHandler.handleSelect(player, blockTier, SELECTED_WORLD);
            rebuildInventory();
        }

        if(e.getClick().isRightClick()){
            blockGuiHandler.handleMilestone(player, blockTier, SELECTED_WORLD);
        }
    }
}
