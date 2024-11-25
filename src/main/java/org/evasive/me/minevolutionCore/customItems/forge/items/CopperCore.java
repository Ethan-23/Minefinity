package org.evasive.me.minevolutionCore.customItems.forge.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.customItems.forge.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.List;

public class CopperCore implements ForgeItemBuilder {
    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of();
    }

    @Override
    public int getCraftTime() {
        return 450;
    }

    @Override
    public Component getName() {
        return ComponentUtils.makeText("Copper Core", NamedTextColor.GRAY, false);
    }

    @Override
    public String getID() {
        return "COPPER_CORE";
    }

    @Override
    public List<Component> getLore() {
        return List.of();
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.copperCore;
    }
}
