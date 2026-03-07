package org.evasive.me.minefinity.core.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.List;
import java.util.function.Consumer;

public class ConfirmationGui extends BaseGui {

    private static final int INVENTORY_SIZE = 27;
    private static final List<Integer> CONFIRM_LIST = List.of(0,1,2,3,9,10,11,12,18,19,20,21);
    private static final List<Integer> DENY_LIST = List.of(5,6,7,8,14,15,16,17,23,24,25,26);
    private static final List<Integer> MIDDLE_LIST = List.of(4,13,22);

    BaseGui prevInventory;
    private final Consumer<Player> onConfirm;

    public ConfirmationGui(Player player, BaseGui prevInventory, Consumer<Player> onConfirm) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Confirmation"));
        this.prevInventory = prevInventory;
        this.onConfirm = onConfirm;
        build();
    }

    @Override
    protected void build() {
        buildConfirmation();
        buildDeny();
        buildMiddle();
    }

    private void buildConfirmation(){
        for(int i :  CONFIRM_LIST){
            ItemBuilder itemBuilder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE,  TextConversions.parse("<bold><green>CONFIRM"));
            itemBuilder.addLore("<bold><red>NOTE! <reset><gray>This action is permanent!");
            itemBuilder.addLore("<yellow>Shift Left-click <gray>to <bold><green>CONFIRM!");
            inventory.setItem(i, itemBuilder.build());
        }
    }

    private void buildDeny(){
        for(int i :  DENY_LIST){
            ItemBuilder itemBuilder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE,  TextConversions.parse("<bold><red>DENY"));
            itemBuilder.addLore("<yellow>Shift Left-click <gray>to <bold><red>DENY!");
            inventory.setItem(i, itemBuilder.build());
        }
    }

    private void buildMiddle(){
        for(int i :  MIDDLE_LIST){
            ItemBuilder itemBuilder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE,  TextConversions.parse(""));
            inventory.setItem(i, itemBuilder.build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);

        int slot = e.getSlot();

        if(e.getClick() != ClickType.SHIFT_LEFT){
            player.sendMessage(TextConversions.parse("<yellow>Shift Left-click <gray>to <bold><green>CONFIRM<reset><gray>/<bold><red>DENY <reset><gray>decision"));
            return;
        }

        if(CONFIRM_LIST.contains(slot)){
            player.sendMessage(TextConversions.parse("<green>Confirmed"));
            if(onConfirm != null)
                onConfirm.accept(player);
            if(prevInventory != null)
                prevInventory.open();
        }else if(DENY_LIST.contains(slot)){
            player.sendMessage(TextConversions.parse("<red>Denied"));
            if(prevInventory != null)
                prevInventory.open();
            else
                player.closeInventory();
        }

    }
}
