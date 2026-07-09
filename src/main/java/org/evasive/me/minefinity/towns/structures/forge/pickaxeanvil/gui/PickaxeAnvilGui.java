package org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.PickaxeAnvilHandler;

import java.util.List;

public class PickaxeAnvilGui extends BaseGui {

    public static final int INVENTORY_SIZE = 27;
    public static final int PICKAXE_SLOT = 11;
    public static final int HEAD_SLOT = 13;
    public static final int CORE_SLOT = 14;
    public static final int HANDLE_SLOT = 15;
    private final ItemStack EMPTY_HEAD_SLOT;
    private final ItemStack EMPTY_CORE_SLOT;
    private final ItemStack EMPTY_HANDLE_SLOT;
    private final ItemStack EMPTY_SLOT;
    private final CustomItemRegistryService customItemRegistryService;
    PickaxeAnvilHandler pickaxeAnvilHandler;

    public PickaxeAnvilGui(Player player, CustomItemRegistryService customItemRegistryService, ItemStack pickaxe) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Pickaxe Anvil"));
        this.customItemRegistryService = customItemRegistryService;
        EMPTY_HEAD_SLOT = new CustomItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Head Slot").build();
        EMPTY_CORE_SLOT = new CustomItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Core Slot").build();
        EMPTY_HANDLE_SLOT = new CustomItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Handle Slot").build();
        EMPTY_SLOT = new CustomItemBuilder(Material.RED_STAINED_GLASS_PANE, "<red>Please insert an item").build();
        this.pickaxeAnvilHandler = new PickaxeAnvilHandler(customItemRegistryService);
        if(pickaxe != null){
            inventory.setItem(PICKAXE_SLOT, pickaxe);
        }

        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory, List.of(PICKAXE_SLOT));
        ItemStack pickaxe = inventory.getItem(PICKAXE_SLOT);
        if(pickaxe == null || pickaxe.getType() == Material.AIR) {
            resetSlots();
        }else {
            addPickaxeParts(pickaxe);
        }
    }

    public void addPickaxeParts(ItemStack itemStack){
        BasePickaxeItem pickaxe = new BasePickaxeItem(itemStack);
        String headId = pickaxe.getPart(PartSlots.HEAD);
        String coreId = pickaxe.getPart(PartSlots.CORE);
        String handleId = pickaxe.getPart(PartSlots.HANDLE);

        inventory.setItem(HEAD_SLOT, headId == null || !customItemRegistryService.isRegistered(headId) ? EMPTY_HEAD_SLOT : customItemRegistryService.getRegisteredItemStack(headId).clone());
        inventory.setItem(CORE_SLOT, coreId == null || !customItemRegistryService.isRegistered(coreId) ? EMPTY_CORE_SLOT : customItemRegistryService.getRegisteredItemStack(coreId).clone());
        inventory.setItem(HANDLE_SLOT, handleId == null || !customItemRegistryService.isRegistered(handleId) ? EMPTY_HANDLE_SLOT : customItemRegistryService.getRegisteredItemStack(handleId).clone());
    }

    public void resetSlots(){
        inventory.setItem(HEAD_SLOT, EMPTY_SLOT.clone());
        inventory.setItem(CORE_SLOT, EMPTY_SLOT.clone());
        inventory.setItem(HANDLE_SLOT, EMPTY_SLOT.clone());
    }

    public void updatePickaxeParts(){
        ItemStack head = inventory.getItem(HEAD_SLOT);
        ItemStack core = inventory.getItem(CORE_SLOT);
        ItemStack handle = inventory.getItem(HANDLE_SLOT);
        ItemStack pickaxe = inventory.getItem(PICKAXE_SLOT);

        inventory.setItem(PICKAXE_SLOT,  pickaxeAnvilHandler.updatePickaxeItem(pickaxe, head, core, handle).clone());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        handleClickEvent(e);
    }

    private void handleClickEvent(InventoryClickEvent e){
        int slot = e.getSlot();
        PartSlots toolSlot = null;

        switch (slot) {
            case HEAD_SLOT -> toolSlot = PartSlots.HEAD;
            case CORE_SLOT -> toolSlot = PartSlots.CORE;
            case HANDLE_SLOT -> toolSlot = PartSlots.HANDLE;
            case PICKAXE_SLOT -> pickaxeAnvilHandler.handlePickaxeSlot(e);
            default -> e.setCancelled(true);
        }

        if(toolSlot == null) return;


        if(!pickaxeAnvilHandler.verifyPartChange(inventory.getItem(PICKAXE_SLOT), e.getCursor(), e.getCurrentItem(), toolSlot)){
            e.setCancelled(true);
            return;
        }

        if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.YELLOW_STAINED_GLASS_PANE))
            e.getCurrentItem().setAmount(0);


        new BukkitRunnable() {
            @Override
            public void run() {
                updatePickaxeParts();
                build();
            }
        }.runTaskLater(Minefinity.getCore(), 1);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        ItemStack checkedItem = e.getInventory().getItem(PickaxeAnvilGui.PICKAXE_SLOT);

        if(checkedItem == null || checkedItem.isEmpty() || !checkedItem.hasItemMeta() || !(customItemRegistryService.getRegisteredBaseItem(checkedItem) instanceof BasePickaxeItem))
            return;

        Player player = (Player) e.getPlayer();

        player.getInventory().addItem(checkedItem);
    }
}
