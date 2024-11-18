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

public class BrokenPickaxe implements PickaxeItemBuilder {

    public Component getName() {
        return makeText("<", NamedTextColor.WHITE, true).append(makeText("Tier 1", NamedTextColor.GREEN, false).append(makeText("> ", NamedTextColor.WHITE, true)).append(makeText("Broken Pickaxe", NamedTextColor.GRAY, false)));
    }

    @Override
    public String getID() {
        return "BROKEN_PICKAXE";
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
        return Material.WOODEN_PICKAXE;
    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public Material getMaterialRequirement(int tier) {
        return Material.OAK_LOG;
    }

    @Override
    public int getTierRequirement(int tier) {
        return 10;
    }

    @Override
    public int getMaxTier() {
        return 2;
    }

    @Override
    public float getBaseSpeed(int tier) {
        return 0.5f + ((tier-1) * 0.25f);
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.brokenPickaxe;
    }

}
