package org.evasive.me.minevolutionCore.customItems.items.pickaxes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.customItems.PickaxeItemBuilder;


import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;

public class CopperPickaxe implements PickaxeItemBuilder {

    @Override
    public Component getName(int tier) {
        return makeText("<", NamedTextColor.WHITE, true).append(makeText("Tier " + tier, NamedTextColor.GREEN, false).append(makeText("> ", NamedTextColor.WHITE, true)).append(makeText("Copper Pickaxe", NamedTextColor.GRAY, false)));
    }

    @Override
    public String getID() {
        return "COPPER_PICKAXE";
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
        return (int) ((40 * tier) * (1.5));
    }

    @Override
    public float getBaseSpeed() {
        return 10f;
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
        return ItemMaker.copperPickaxe;
    }

}
