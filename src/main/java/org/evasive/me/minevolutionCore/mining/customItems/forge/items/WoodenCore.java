package org.evasive.me.minevolutionCore.mining.customItems.forge.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.mining.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.mining.customItems.forge.ForgeItemBuilder;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenCore implements ForgeItemBuilder {

    @Override
    public Component getName() {
        return ComponentUtils.makeText("Wooden Core", NamedTextColor.WHITE, false);
    }

    @Override
    public String getID() {
        return "";
    }

    @Override
    public @Nullable List<Component> getLore() {
        return List.of(ComponentUtils.makeText("Tier 1", NamedTextColor.WHITE, false));
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_SAPLING;
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.woodenCore;
    }

    @Override
    public int getCraftAmount() {
        return 1;
    }

    @Override
    public List<ItemStack> getRecipe() {
        return List.of(new ItemStack(Material.OAK_PLANKS, 10));
    }

    @Override
    public int getCraftTime() {
        return 10;
    }
}
