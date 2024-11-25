package org.evasive.me.minevolutionCore.customItems.pickaxes.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.customItems.pickaxes.PickaxeItemBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;

public class WoodenPickaxe implements PickaxeItemBuilder {

    @Override
    public Component getName(int tier) {
        return makeText("<", NamedTextColor.WHITE, true).append(makeText("Tier " + tier, NamedTextColor.GREEN, false).append(makeText("> ", NamedTextColor.WHITE, true)).append(makeText("Wooden Pickaxe", NamedTextColor.GRAY, false)));
    }

    @Override
    public String getID() {
        return "WOODEN_PICKAXE";
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
        return Material.COBBLESTONE;
    }

    @Override
    public int getTierRequirement(int tier) {
        return (int) ((10 * tier) * (1.5));
    }

    @Override
    public float getBaseSpeed() {
        return 1f;
    }

    @Override
    public float getTierSpeed(int tier){
        return (tier-1) * 0.01f;
    }

    @Override
    public float getSpeed(int tier){
        return getBaseSpeed() + getTierSpeed(tier);
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.woodenPickaxe;
    }

}
