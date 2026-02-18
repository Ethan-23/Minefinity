package org.evasive.me.minefinity.resourceblock.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.player.sevices.MilestoneService;
import org.evasive.me.minefinity.resourceblock.BlockType;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.utils.GenericGuiItems.backPage;
import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;
import static org.evasive.me.minefinity.utils.TextConversions.buildRarityColor;
import static org.evasive.me.minefinity.utils.TextConversions.intToRoman;

public class MilestoneGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public final static int BLOCK_DISPLAY = 13;
    public final static int BACK_BUTTON = 45;
    public final static List<Integer> TRACK = List.of(28,29,30,31,32,33,34);
    public final int blockTier;

    String COMPLETED = "<bold><green>COMPLETED";
    String IN_PROGRESS = "<bold><yellow>IN PROGRESS";

    MilestoneService milestoneService =  Minefinity.getCore().getMilestoneService();

    public MilestoneGui(Player player, int blockTier) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Milestones"));
        this.blockTier = blockTier;
        build();
    }

    @Override
    protected void build() {
        BlockType blockType = BlockType.values()[blockTier];

        int milestoneAmount = milestoneService.getTierProgress(player, blockType);


        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (!TRACK.contains(i)) this.inventory.setItem(i, fillerPane);
        }

        inventory.setItem(BLOCK_DISPLAY, new ItemBuilder(blockType.getBlock().material(), TextConversions.parse(buildRarityColor(blockType.name(), blockType.getBlock().blockDrop().getBuilder().getRarity()))).build());

        inventory.setItem(BACK_BUTTON, backPage);

        List<Integer> milestoneUnlocks = blockType.getBlock().milestoneUnlocks();

        for(int milestoneTier = 0; milestoneTier < milestoneUnlocks.size(); milestoneTier++){

            int slot = TRACK.get(milestoneTier);

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
            new BlockGui(player).open();
        }

    }
}
