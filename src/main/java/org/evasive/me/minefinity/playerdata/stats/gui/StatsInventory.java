package org.evasive.me.minefinity.playerdata.stats.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;

import java.util.EnumMap;
import java.util.Map;

public class StatsInventory extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int COMBAT_STATS_SLOT = 10;
    private static final int MINING_STATS_SLOT = 11;
    private static final Stats[] COMBAT_STATS = new Stats[]{Stats.HEALTH, Stats.REGENERATION, Stats.DEFENSE, Stats.STRENGTH, Stats.CRITICAL_CHANCE, Stats.CRITICAL_DAMAGE, Stats.SPEED};
    private static final Stats[] MINING_STATS = new Stats[]{Stats.BREAKING_POWER, Stats.MINING_SPEED, Stats.MINING_FORTUNE};
    private static final Stats[] WOODCUTTING_STATS = new Stats[]{Stats.WOODCUTTING_POWER, Stats.WOODCUTTING_FORTUNE};
    private static final Stats[] FISHING_STATS = new Stats[]{Stats.FISHING_SPEED, Stats.FISHING_LUCK};
    private static final Stats[] ANIMAL_STATS = new Stats[]{Stats.ANIMAL_SPEED, Stats.ANIMAL_FORTUNE};
    private static final int SKILL_STATS_SLOT = 11;
    private final EnumMap<Stats, Integer> statsMap;

    public StatsInventory(Player player, StatsService statsService) {
        super(player, INVENTORY_SIZE, TextConversions.parse("Player Stats"));
        this.statsMap = statsService.getStats(player.getUniqueId());
        build();
    }

    @Override
    protected void build() {
        GuiUtils.fillGui(inventory);
        displayCombatStats();
        displayMiningStats();
    }

    private void displayCombatStats(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.IRON_SWORD, "<gold>Combat Stats");
        itemBuilder.setGlow(true);

        for (Stats stats : COMBAT_STATS) {
            itemBuilder.addLore(stats.getDisplay() + ": " + statsMap.get(stats));
        }

        inventory.setItem(COMBAT_STATS_SLOT, itemBuilder.build());
    }

    private void displayMiningStats(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.IRON_PICKAXE, "<gold>Mining Stats");
        itemBuilder.setGlow(true);

        for (Stats stats : MINING_STATS) {
            itemBuilder.addLore(stats.getDisplay() + ": " + statsMap.get(stats));
        }

        inventory.setItem(MINING_STATS_SLOT, itemBuilder.build());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
    }
}
