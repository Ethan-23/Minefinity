package org.evasive.me.minefinity.playerdata.stats.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.gui.BaseGui;
import org.evasive.me.minefinity.core.gui.GuiUtils;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.CustomItemBuilder;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;
import org.evasive.me.minefinity.playerdata.stats.service.StatsService;

import java.util.EnumMap;

import static org.evasive.me.minefinity.playerdata.stats.data.Stats.MINING_FORTUNE;
import static org.evasive.me.minefinity.playerdata.stats.data.Stats.MINING_SPEED;

public class StatsInventory extends BaseGui {

    private static final int INVENTORY_SIZE = 54;
    private static final int HELMET_SLOT = 10;
    private static final int CHESTPLATE_SLOT = 19;
    private static final int LEGGINGS_SLOT = 28;
    private static final int BOOTS_SLOT = 37;
    private static final int HAND_SLOT = 20;
    private static final int COMBAT_STATS_SLOT = 15;
    private static final int MINING_STATS_SLOT = 16;
    private static final int SELECTED_BLOCK_STATS_SLOT = 22;
    private static final Stats[] COMBAT_STATS = new Stats[]{Stats.HEALTH, Stats.REGENERATION, Stats.DEFENSE, Stats.STRENGTH, Stats.CRITICAL_CHANCE, Stats.CRITICAL_DAMAGE, Stats.SPEED};
    private static final Stats[] MINING_STATS = new Stats[]{Stats.BREAKING_POWER, MINING_SPEED, Stats.MINING_FORTUNE};
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
        displaySelectedBlockStats();
        displayGear();
        displayCombatStats();
        displayMiningStats();
    }

    private void displayGear() {
        CustomItemBuilder blank = new CustomItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<yellow>Empty");
        ItemStack helmetItem = player.getInventory().getHelmet();
        ItemStack chestplateItem = player.getInventory().getChestplate();
        ItemStack leggingsItem = player.getInventory().getLeggings();
        ItemStack bootsItem = player.getInventory().getBoots();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        inventory.setItem(HELMET_SLOT, helmetItem != null && !helmetItem.getType().isAir() ? helmetItem : blank.setDisplayName("<yellow>Empty Helmet").build());
        inventory.setItem(CHESTPLATE_SLOT, chestplateItem != null && !chestplateItem.getType().isAir() ? chestplateItem : blank.setDisplayName("<yellow>Empty Chestplate").build());
        inventory.setItem(LEGGINGS_SLOT, leggingsItem != null && !leggingsItem.getType().isAir() ? leggingsItem : blank.setDisplayName("<yellow>Empty Leggings").build());
        inventory.setItem(BOOTS_SLOT, bootsItem != null && !bootsItem.getType().isAir() ? bootsItem : blank.setDisplayName("<yellow>Empty Boots").build());
        inventory.setItem(HAND_SLOT, !handItem.getType().isAir() ? handItem : blank.setDisplayName("<yellow>Empty Hand").build());
    }

    private void displayCombatStats(){
        CustomItemBuilder itemBuilder = new CustomItemBuilder(Material.IRON_SWORD, "<gold>Combat Stats");
        itemBuilder.setGlow(true);

        for (Stats stats : COMBAT_STATS) {
            itemBuilder.addLore(stats.getDisplay() + ": " + statsMap.get(stats));
        }

        inventory.setItem(COMBAT_STATS_SLOT, itemBuilder.build());
    }

    private void displaySelectedBlockStats(){
        CustomItemBuilder itemBuilder = new CustomItemBuilder(Material.BEDROCK, "Selected Block Stats");
        itemBuilder.addLore(MINING_SPEED.getDisplay() + ": " + statsMap.get(MINING_SPEED));
        itemBuilder.addLore(MINING_FORTUNE.getDisplay() + ": " + statsMap.get(MINING_FORTUNE));
        inventory.setItem(SELECTED_BLOCK_STATS_SLOT, itemBuilder.build());
    }

    private void displayMiningStats(){
        CustomItemBuilder itemBuilder = new CustomItemBuilder(Material.IRON_PICKAXE, "<gold>Mining Stats");
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
