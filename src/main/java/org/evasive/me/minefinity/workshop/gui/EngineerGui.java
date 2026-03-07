package org.evasive.me.minefinity.workshop.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.utils.TextConversions;
import org.evasive.me.minefinity.workshop.*;
import org.evasive.me.minefinity.workshop.service.EngineerClickHandler;
import org.evasive.me.minefinity.workshop.service.EngineerService;

import java.util.List;

import static org.evasive.me.minefinity.utils.TextConversions.formatItemName;

public class EngineerGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public static final int HEADER_SLOT = 13;
    public static final int TOOL_SLOT = 40;
    public static final int RESOURCE_SLOT = 41;
    public static final int INFORMATION_SLOT = 49;
    public static final int SWAP_SLOT = 39;
    public static final List<Integer> SHOP_SLOTS = List.of(
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    );

    private final EngineerGuiRenderer renderer;
    EngineerClickHandler engineerClickHandler;


    public EngineerGui(Player player, WorkshopMode workshopMode, EngineerService engineerService) {
        super(player, INVENTORY_SIZE, TextConversions.parse(formatItemName(workshopMode.name())));
        renderer = new EngineerGuiRenderer(player, workshopMode, engineerService);
        engineerClickHandler = new EngineerClickHandler(player, workshopMode, engineerService);
        build();
    }

    @Override
    protected void build() {
        renderer.render(inventory, INVENTORY_SIZE);
    }

    public void rebuildDynamic(){
        renderer.renderDynamic(inventory);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
        engineerClickHandler.handleEngineerClick(e);
        rebuildDynamic();
    }
}