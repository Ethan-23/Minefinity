package org.evasive.me.minefinity.miner.gui.main;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.miner.service.AutoMinerService;
import org.evasive.me.minefinity.player.sevices.BlockTierService;
import org.evasive.me.minefinity.resourceblock.framework.BlockType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.GenericGuiItems.noneBarrier;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.utils.TextConversions.intToRoman;


public class MinerMainGui extends BaseGui {

    private final AutoMinerService autoMinerService;
    private MinerMainHandler minerMainEvents;

    private static final int INVENTORY_SIZE = 54;
    public static final int PICKAXE_SLOT = 20;
    public static final int BLOCK_SLOT = 22;
    public static final int UPGRADE_SLOT = 24;
    public static final int COLLECT_SLOT = 31;
    public static final int INFORMATION_SLOT = 49;

    public MinerMainGui(Player player, AutoMinerService autoMinerService, BlockTierService blockTierService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Miner"));
        this.autoMinerService = autoMinerService;
        minerMainEvents = new MinerMainHandler(autoMinerService, blockTierService);
        build();
    }

    @Override
    protected void build() {
        setupBackground();
        addButtons(player);
        createRefresh(player);
        buildInformation();
    }

    private void addButtons(Player player){
        ItemStack pickaxe = autoMinerService.getAutoMinerPickaxe(player);
        BlockType blockType = autoMinerService.getAutoMinerBlockType(player);
        inventory.setItem(PICKAXE_SLOT, pickaxe != null ? pickaxe : new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, TextConversions.parse("Pickaxe Slot")).build()); // Stored pickaxe
        inventory.setItem(BLOCK_SLOT, blockType != null ? createBlockButton(blockType) : noneBarrier); // Stored selected block
        inventory.setItem(UPGRADE_SLOT, new ItemBuilder(Material.INK_SAC, TextConversions.parse("INK")).build()); // Stored upgrade
        inventory.setItem(COLLECT_SLOT, createCollectButton(player)); // Stored upgrade
    }

    private ItemStack createBlockButton(BlockType blockType){
        Component name = TextConversions.parse("<gray>(<white>" + intToRoman(blockType.ordinal()+1) +"<gray>) <white>" + blockType.getBlock().name());

        return new ItemBuilder(blockType.getBlock().material(), name).addLore(
                List.of(
                        "<gray>Block Health: <white>" + blockType.getBlock().health(),
                        "",
                        "<#a1a1a1>Left-Click <gray>to Change"
                )
        ).build();
    }


    private ItemStack createCollectButton(Player player){

        String storageAmountString = "<gray>(" + autoMinerService.getAutoMinerStoredBlockAmount(player) + "/" + autoMinerService.getAutoMinerStorageCap(player) + ")";

        ItemBuilder collectBuilder = new ItemBuilder(Material.CHEST_MINECART, TextConversions.parse("<gold><bold>Block Storage</bold>"));

        collectBuilder.addLore(storageAmountString).addBlank();

        Map<String, Integer> itemStorage = autoMinerService.getAutoMinerStorage(player);

        for (Map.Entry<String, Integer> entry : itemStorage.entrySet()) {
            if(entry.getValue() == 0) continue;

            String itemId = entry.getKey();

            BaseCustomItem baseCustomItem = (BaseCustomItem) CustomItemRegistry.getByID(itemId).getBaseItem();
            collectBuilder.addLore("<white>" + buildRarityColor(itemId, baseCustomItem.getRarity()) + "<gray> ×<blue>" + entry.getValue());
        }

        collectBuilder.addBlank().addLore("<green>Click to collect");

        return collectBuilder.build();
    }

    private void buildInformation(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.KNOWLEDGE_BOOK, TextConversions.parse("<bold><gold>Miner"));
        itemBuilder.addLore("<gray>This is the miner. He be mining the blocks and stuff. He be mining all night and day. Might be a 'worker' if you catch my drift. 3 Netherite Ingots!");
        inventory.setItem(INFORMATION_SLOT, itemBuilder.build());
    }

    private void setupBackground(){
        for (int slot = 0; slot < INVENTORY_SIZE; slot++) {
            if(List.of(PICKAXE_SLOT, BLOCK_SLOT, UPGRADE_SLOT).contains(slot)) continue;
            inventory.setItem(slot, fillerPane);
        }
    }

    private void createRefresh(Player player){
        Bukkit.getScheduler().runTaskLater(Minefinity.getCore(), () -> {
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof MinerMainGui)
                build();
        }, 20).getTaskId();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);

        Player player = (Player) e.getWhoClicked();

        int clickedSlot = e.getSlot();

        if(clickedSlot == MinerMainGui.PICKAXE_SLOT){
            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.YELLOW_STAINED_GLASS_PANE))
                e.getCurrentItem().setAmount(0);
            rebuildInventory();
            if(!minerMainEvents.handlePickaxeSlot(player, e.getCursor()))
                e.setCancelled(true);
            return;
        }

        e.setCancelled(true);

        if(clickedSlot == MinerMainGui.BLOCK_SLOT){
            e.setCancelled(true);
            minerMainEvents.handleBlockSlot(player);
            return;
        }

        if(clickedSlot == MinerMainGui.COLLECT_SLOT){
            minerMainEvents.handleCollectSlot(e, player);
            build();
            return;
        }
    }

}
