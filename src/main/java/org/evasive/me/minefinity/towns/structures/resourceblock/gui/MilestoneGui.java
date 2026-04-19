package org.evasive.me.minefinity.towns.structures.resourceblock.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.resourceblock.framework.BaseBlock;
import org.evasive.me.minefinity.towns.structures.resourceblock.service.BlockTierService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.backPage;
import static org.evasive.me.minefinity.core.utils.guis.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.core.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.core.utils.TextConversions.intToRoman;

public class MilestoneGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public final static int BLOCK_DISPLAY = 13;
    public final static int BACK_BUTTON = 45;
    public final static List<Integer> TRACK = List.of(28,29,30,31,32,33,34);
    public final String blockId;
    private final String SELECTED_WORLD;

    String COMPLETED = "<bold><green>COMPLETED";
    String IN_PROGRESS = "<bold><yellow>IN PROGRESS";

    private final BlockTierService blockTierService;
    private final MilestoneService milestoneService;
    private final EconomyService economyService;
    private final CustomItemRegistryService customItemRegistryService;

    public MilestoneGui(Player player, String blockId, CustomItemRegistryService customItemRegistryService, BlockTierService blockTierService, MilestoneService milestoneService, EconomyService economyService, String worldId) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Milestones"));
        this.blockId = blockId;
        this.blockTierService = blockTierService;
        this.milestoneService = milestoneService;
        this.economyService = economyService;
        this.customItemRegistryService = customItemRegistryService;
        SELECTED_WORLD = worldId;
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        buildMilestoneTiers();
        inventory.setItem(BACK_BUTTON, backPage);
    }

    public void buildMilestoneTiers(){
        BaseBlock baseBlock = blockTierService.getBlockTypeRegistryService().getBaseBlock(blockId);
        BaseCustomItem baseCustomItem = customItemRegistryService.getBaseItemById(baseBlock.blockDropId());

        inventory.setItem(BLOCK_DISPLAY, new ItemBuilder(baseBlock.material(), TextConversions.parse(buildRarityColor(baseBlock.name(), baseCustomItem.getRarity()))).build());
        int milestoneAmount = milestoneService.getTierProgress(player, blockId);
        List<Integer> milestoneUnlocks = baseBlock.milestoneUnlocks();

        int milestoneSize = TRACK.size() - milestoneUnlocks.size();

        for(int milestoneTier = 0; milestoneTier < milestoneUnlocks.size(); milestoneTier++){

            if(milestoneTier >= TRACK.size())
                continue;

            int slot = TRACK.get(milestoneTier + milestoneSize/2);

            boolean completed = milestoneAmount >= milestoneUnlocks.get(milestoneTier);
            boolean started = (milestoneTier == 0 && milestoneUnlocks.getFirst() > milestoneTier || (milestoneUnlocks.getFirst() <= milestoneAmount && milestoneAmount >= milestoneUnlocks.get(milestoneTier-1)));

            Material milestoneMaterial = completed ? Material.GREEN_STAINED_GLASS_PANE : started ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            String color = completed ? "green" : started ? "yellow" : "red";
            Component milestoneName = TextConversions.parse("<"+color+">Milestone <tier></"+color+">", Placeholder.parsed("tier", intToRoman(milestoneTier + 1)));
            int cap = milestoneUnlocks.get(milestoneTier);

            ItemBuilder milestoneItem = new ItemBuilder(milestoneMaterial, milestoneName);
            milestoneItem.addLore("<white>"+Math.min(milestoneAmount, cap)+"/"+cap+"</white>");
            milestoneItem.addLore(completed ? COMPLETED : (started ? IN_PROGRESS : null));

            inventory.setItem(slot, milestoneItem.build());
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

        int slot = e.getSlot();

        if(slot == BACK_BUTTON){
            new BlockGui(player, blockTierService, customItemRegistryService, milestoneService, economyService, SELECTED_WORLD).open();
        }

    }
}
