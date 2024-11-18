package org.evasive.me.minevolutionCore.mining.customItems.pickaxes.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.mining.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.mining.customItems.pickaxes.PickaxeItemBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;

public class CopperPickaxe implements PickaxeItemBuilder {

    @Override
    public Component getName() {
        return makeText("<", NamedTextColor.WHITE, true).append(makeText("Tier 1", NamedTextColor.GREEN, false).append(makeText("> ", NamedTextColor.WHITE, true)).append(makeText("Copper Pickaxe", NamedTextColor.GRAY, false)));
    }

    @Override
    public String getID() {
        return "COPPER_PICKAXE";
    }

    @Override
    public List<Component> getLore(int tier) {
        List<Component> lore = new ArrayList<>();
        lore.add(makeText("Mining Speed: ", NamedTextColor.GRAY, false).append(makeText(""+getBaseSpeed(1), NamedTextColor.WHITE, true)));
        lore.add(Component.text(""));
        lore.add(makeText("Enchants:", NamedTextColor.GOLD, true));
        lore.add(Component.text(""));
        lore.add(makeText("Progress:", NamedTextColor.GOLD, true));
        lore.add(makeText("|||||||||||||||||||||||||||||", NamedTextColor.RED, false));
        lore.add(makeText("Mine ", NamedTextColor.GRAY, false).append(makeText("0/" + getTierRequirement(1) + " ", NamedTextColor.GOLD, false).append(makeText(getMaterialRequirement(1).toString(), NamedTextColor.GRAY, false))));
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_PICKAXE;
    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public Material getMaterialRequirement(int tier) {
        return Material.COPPER_ORE;
    }

    @Override
    public int getTierRequirement(int tier) {
        return 50;
    }

    @Override
    public int getMaxTier() {
        return 5;
    }

    @Override
    public float getBaseSpeed(int tier) {
        return 10f + (0.2f * (tier - 1));
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.copperPickaxe;
    }

}
