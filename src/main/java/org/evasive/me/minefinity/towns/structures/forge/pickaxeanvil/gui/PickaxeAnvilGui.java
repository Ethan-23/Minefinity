package org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.pickaxeanvil.PickaxeAnvilHandler;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.core.utils.guis.GenericGuiItems;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.utils.TextConversions;

import java.util.List;
import java.util.Objects;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService.*;

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

    public PickaxeAnvilGui(Player player, CustomItemRegistryService customItemRegistryService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Pickaxe Anvil"));
        this.customItemRegistryService = customItemRegistryService;
        EMPTY_HEAD_SLOT = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Head Slot").build();
        EMPTY_CORE_SLOT = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Core Slot").build();
        EMPTY_HANDLE_SLOT = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Handle Slot").build();
        EMPTY_SLOT = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, "<red>Please insert an item").build();
        this.pickaxeAnvilHandler = new PickaxeAnvilHandler(customItemRegistryService);
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
        String headId = getStringPDC(itemStack, PICKAXE_HEAD_KEY);
        String coreId = getStringPDC(itemStack, PICKAXE_CORE_KEY);
        String handleId = getStringPDC(itemStack, PICKAXE_HANDLE_KEY);

        inventory.setItem(HEAD_SLOT, Objects.equals(headId, "NONE") || !customItemRegistryService.isRegistered(headId) ? EMPTY_HEAD_SLOT : customItemRegistryService.getRegisteredItemStack(headId).clone());
        inventory.setItem(CORE_SLOT, Objects.equals(coreId, "NONE") || !customItemRegistryService.isRegistered(coreId) ? EMPTY_CORE_SLOT : customItemRegistryService.getRegisteredItemStack(coreId).clone());
        inventory.setItem(HANDLE_SLOT, Objects.equals(handleId, "NONE") || !customItemRegistryService.isRegistered(handleId)? EMPTY_HANDLE_SLOT : customItemRegistryService.getRegisteredItemStack(handleId).clone());
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
        CustomItemType itemType = null;

        switch (slot) {
            case PickaxeAnvilGui.HEAD_SLOT -> itemType = CustomItemType.PICKAXE_HEAD;
            case PickaxeAnvilGui.CORE_SLOT -> itemType = CustomItemType.PICKAXE_CORE;
            case PickaxeAnvilGui.HANDLE_SLOT -> itemType = CustomItemType.PICKAXE_HANDLE;
            case PickaxeAnvilGui.PICKAXE_SLOT -> pickaxeAnvilHandler.handlePickaxeSlot(e);
            default -> e.setCancelled(true);
        }

        if(itemType == null) return;

        if(!pickaxeAnvilHandler.verifyPartChange(inventory.getItem(PICKAXE_SLOT), e.getCursor(), e.getCurrentItem(), itemType)){
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
