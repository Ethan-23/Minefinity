package org.evasive.me.minevolutionCore.customItems.items.storage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minevolutionCore.customItems.ItemBuilder;
import org.evasive.me.minevolutionCore.customItems.ItemMaker;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import java.util.List;

public class Backpack implements ItemBuilder {

    @Override
    public Component getName() {
        return ComponentUtils.makeText("Backpack", NamedTextColor.WHITE, false);
    }

    @Override
    public String getID() {
        return "BACKPACK";
    }

    @Override
    public List<Component> getLore() {
        return List.of();
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public ItemStack getItem() {
        return ItemMaker.backpack;
    }
}
