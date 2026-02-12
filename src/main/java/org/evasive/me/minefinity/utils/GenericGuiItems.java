package org.evasive.me.minefinity.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GenericGuiItems {

    public static ItemStack fillerPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, Messages.parse("")).build();
    public static ItemStack lockedPane = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, Messages.parse("<bold><red>Locked")).build();
    public static ItemStack emptyPane = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, Messages.parse("<bold><yellow>EMPTY")).build();
    public static ItemStack progressPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, Messages.parse("<bold><gold>IN PROGRESS")).build();
    public static ItemStack donePane = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE, Messages.parse("<bold><green>DONE")).build();
    public static ItemStack blankOrange = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, Messages.parse("")).build();
    public static ItemStack mysteryPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, Messages.parse("<gold>???")).build();
    public static ItemStack noneBarrier = new ItemBuilder(Material.BARRIER, Messages.parse("<red>None")).build();
    public static ItemStack backPage = new ItemBuilder(Material.ARROW, Messages.parse("<white>Back")).build();
    public static ItemStack exit = new ItemBuilder(Material.BARRIER, Messages.parse("<bold><red>Close Menu")).build();
    public static ItemStack start = new ItemBuilder(Material.FURNACE, Messages.parse("<bold><green>Start")).build();

}
