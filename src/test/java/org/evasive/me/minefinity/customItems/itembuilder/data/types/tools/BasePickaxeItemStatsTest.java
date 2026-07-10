package org.evasive.me.minefinity.customItems.itembuilder.data.types.tools;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.data.Stats;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StatsComponent;
import org.evasive.me.minefinity.customItems.types.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.types.tools.BasePickaxeItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasePickaxeItemStatsTest {

    private BasePickaxeItem pickaxeWithStats(Map<Stats, Integer> stats) {
        BasePickaxeItem pickaxe = new BasePickaxeItem("PICK", Material.WOODEN_PICKAXE, "Pick", Rarity.MINOR);
        StatsComponent component = pickaxe.getComponent(StatsComponent.class);
        stats.forEach(component::setStat);
        return pickaxe;
    }

    private BasePartItem partWithStats(Map<Stats, Integer> stats) {
        BasePartItem part = new BasePartItem("PART", Material.STICK, "Part", Rarity.MINOR);
        StatsComponent component = part.getComponent(StatsComponent.class);
        stats.forEach(component::setStat);
        return part;
    }

    @Test
    void totalStatsAreJustThePickaxesOwnWhenThereAreNoParts() {
        BasePickaxeItem pickaxe = pickaxeWithStats(Map.of(Stats.BREAKING_POWER, 1, Stats.MINING_SPEED, 30));

        Map<String, Integer> total = pickaxe.getTotalStats(List.of());

        assertEquals(1, total.get(Stats.BREAKING_POWER.name()));
        assertEquals(30, total.get(Stats.MINING_SPEED.name()));
    }

    @Test
    void partStatsAreSummedOntoThePickaxeStats() {
        BasePickaxeItem pickaxe = pickaxeWithStats(Map.of(Stats.MINING_SPEED, 30));
        BasePartItem head = partWithStats(Map.of(Stats.MINING_SPEED, 10));
        BasePartItem core = partWithStats(Map.of(Stats.MINING_FORTUNE, 5));

        Map<String, Integer> total = pickaxe.getTotalStats(List.of(head, core));

        assertEquals(40, total.get(Stats.MINING_SPEED.name()), "30 own + 10 from the head");
        assertEquals(5, total.get(Stats.MINING_FORTUNE.name()), "0 own + 5 from the core");
    }

    @Test
    void aNullPartInTheListIsSkippedWithoutThrowing() {
        BasePickaxeItem pickaxe = pickaxeWithStats(Map.of(Stats.MINING_SPEED, 30));
        BasePartItem head = partWithStats(Map.of(Stats.MINING_SPEED, 10));

        List<BasePartItem> parts = new ArrayList<>(Arrays.asList(head, null));
        Map<String, Integer> total = pickaxe.getTotalStats(parts);

        assertEquals(40, total.get(Stats.MINING_SPEED.name()), "the null part contributes nothing and does not NPE");
    }

    @Test
    void aPartWithNoStatsContributesNothing() {
        BasePickaxeItem pickaxe = pickaxeWithStats(Map.of(Stats.MINING_SPEED, 30));
        BasePartItem blank = new BasePartItem("BLANK", Material.STICK, "Blank", Rarity.MINOR);

        Map<String, Integer> total = pickaxe.getTotalStats(List.of(blank));

        assertEquals(1, total.size());
        assertEquals(30, total.get(Stats.MINING_SPEED.name()));
    }
}
