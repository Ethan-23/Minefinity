package org.evasive.me.minefinity.resourceblock.milestones.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.resourceblock.milestones.MilestoneFunctions;
import org.evasive.me.minefinity.utils.ItemBuilder;
import org.evasive.me.minefinity.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.evasive.me.minefinity.utils.EnchantUtils.intToRoman;
import static org.evasive.me.minefinity.utils.GenericGuiItems.fillerPane;

public class MilestoneGui extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    public final static List<Integer> TRACK = List.of(0, 9, 18, 27, 36, 45, 46, 47, 38, 29, 20, 11, 2, 3, 4, 13, 22, 31, 40, 49, 50, 51, 42, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53);
    public final static List<Integer> TRACK_VALUES = List.of(50, 100, 250, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000, 12500, 15000, 17500, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 60000, 70000, 80000, 90000, 100000, 125000, 150000, 175000, 200000, 250000, 300000, 400000, 500000);
    public final Material material;

    String CLAIMED = "<bold><green>CLAIMED";
    String CLAIMABLE = "<bold><green>CLICK TO CLAIM";
    String IN_PROGRESS = "<bold><yellow>IN PROGRESS";

    public MilestoneGui(Player player, Material material) {
        super(player, INVENTORY_SIZE, Messages.parse("Milestones"));
        this.material = material;
        build();
    }

    @Override
    protected void build() {
        int milestoneAmount = MilestoneFunctions.getMaterialMilestone(player, material);

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (!TRACK.contains(i)) this.inventory.setItem(i, fillerPane);
        }

        for(int milestoneTier = 0; milestoneTier < TRACK.size(); milestoneTier++){

            if(milestoneTier >= TRACK_VALUES.size()) break;

            int slot = TRACK.get(milestoneTier);

            boolean completed = milestoneAmount >= TRACK_VALUES.get(milestoneTier);
            boolean started = (milestoneTier == 0 && TRACK_VALUES.getFirst() > milestoneTier || (TRACK_VALUES.getFirst() <= milestoneAmount && milestoneAmount >= TRACK_VALUES.get(milestoneTier-1)));
            boolean claimed = MilestoneFunctions.getClaimedTier(player, material) >= milestoneTier+1;

            Material milestoneMaterial = completed ? Material.GREEN_STAINED_GLASS_PANE : started ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            String color = completed ? "green" : started ? "yellow" : "red";
            Component milestoneName = Messages.parse("<"+color+">Milestone <tier></"+color+">", Placeholder.parsed("tier", intToRoman(milestoneTier + 1)));
            int cap = TRACK_VALUES.get(milestoneTier);

            ItemBuilder milestoneItem = new ItemBuilder(milestoneMaterial, milestoneName);
            milestoneItem.addLore("<white>"+Math.min(milestoneAmount, cap)+"/"+cap+"</white>");
            milestoneItem.addLore(completed ? (claimed ? CLAIMED : CLAIMABLE) : (started ? IN_PROGRESS : null));
            if (completed && !claimed) {
                milestoneItem.addGlow();
            }

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

        if(!MilestoneGui.TRACK.contains(slot))
            return;

        Player player = (Player) e.getWhoClicked();

        int trackIndex = MilestoneGui.TRACK.indexOf(slot);

        if(MilestoneGui.TRACK_VALUES.get(trackIndex) > MilestoneFunctions.getMaterialMilestone(player, MilestoneFunctions.getSelectedMaterial(player)))
            return;

        if(MilestoneFunctions.getClaimedTier(player, MilestoneFunctions.getSelectedMaterial(player)) < trackIndex + 1){
            MilestoneFunctions.increaseClaimedTier(player, MilestoneFunctions.getSelectedMaterial(player));
            rebuildInventory();
        }
    }
}
