package org.evasive.me.minefinity.core.utils.guis;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.lib.item.ItemBuilder;

public class GenericGuiItems {

    public static ItemStack fillerPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "").build();
    public static ItemStack lockedPane = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, "<bold><red>Locked").build();
    public static ItemStack emptyPane = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, "<bold><yellow>EMPTY").build();
    public static ItemStack progressPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "<bold><gold>IN PROGRESS").build();
    public static ItemStack donePane = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE, "<bold><green>DONE").build();
    public static ItemStack blankOrange = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "").build();
    public static ItemStack mysteryPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "<gold>???").build();
    public static ItemStack noneBarrier = new ItemBuilder(Material.BARRIER, "<red>None").build();
    public static ItemStack backPage = new ItemBuilder(Material.ARROW, "<white>Back").build();
    public static ItemStack exit = new ItemBuilder(Material.BARRIER, "<bold><red>Close Menu").build();
    public static ItemStack start = new ItemBuilder(Material.FURNACE, "<bold><green>Start").build();

}
