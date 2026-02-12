package org.evasive.me.minefinity.automation.miner.gui.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.automation.miner.data.AutoMiner;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;

import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.customItems.ItemNameBuilder.buildRarityColor;
import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.GenericGuiItems.noneBarrier;


public class MinerMainGui extends BaseGui {

    MinerMainHandler minerMainEvents = new MinerMainHandler();

    private static final int INVENTORY_SIZE = 27;
    public static final int PICKAXE_SLOT = 11;
    public static final int BLOCK_SLOT = 13;
    public static final int UPGRADE_SLOT = 15;
    public static final int COLLECT_SLOT = 22;

    public MinerMainGui(Player player) {
        super(player, INVENTORY_SIZE, Messages.parse("Miner"));
        build();
    }

    @Override
    protected void build() {
        setupBackground();
        addButtons(player);
        createRefresh(player);
    }

    private void addButtons(Player player){
        AutoMiner miner = Minefinity.playerManager.getPlayerData(player).getAutoMiner();

        ItemStack pickaxe = miner.getPickaxe();
        inventory.setItem(PICKAXE_SLOT, pickaxe != null ? pickaxe : new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, Messages.parse("Pickaxe Slot")).build()); // Stored pickaxe
        inventory.setItem(BLOCK_SLOT, miner.getBlockType() != null ? ItemStack.of(miner.getBlockType().getBlock().material()) : noneBarrier); // Stored selected block
        inventory.setItem(UPGRADE_SLOT, new ItemBuilder(Material.INK_SAC, Messages.parse("INK")).build()); // Stored upgrade
        inventory.setItem(COLLECT_SLOT, createCollectButton(player)); // Stored upgrade
    }

    private ItemStack createCollectButton(Player player){

        AutoMiner autoMiner = Minefinity.playerManager.getPlayerData(player).getAutoMiner();

        String storageAmountString = "<gray>(" + autoMiner.getStoredBlockAmount() + "/" + autoMiner.getStorageCap() + ")";

        ItemBuilder collectBuilder = new ItemBuilder(Material.CHEST_MINECART, Messages.parse("<gold><bold>Block Storage</bold>"));

        collectBuilder.addLore(storageAmountString).addBlank();

        Map<String, Integer> itemStorage = autoMiner.getItemStorage();

        for (Map.Entry<String, Integer> entry : itemStorage.entrySet()) {
            if(entry.getValue() == 0) continue;

            String itemId = entry.getKey();

            collectBuilder.addLore("<white>" + buildRarityColor(itemId, CustomItemRegistry.getByID(itemId).getBuilder().getRarity()) + "<gray> ×<blue>" + entry.getValue());
        }

        collectBuilder.addBlank().addLore("<green>Click to collect");

        return collectBuilder.build();
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
