package org.evasive.me.minefinity.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GenericGuiItems {

    public static ItemStack fillerPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, TextConversions.parse("")).build();
    public static ItemStack lockedPane = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, TextConversions.parse("<bold><red>Locked")).build();
    public static ItemStack emptyPane = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, TextConversions.parse("<bold><yellow>EMPTY")).build();
    public static ItemStack progressPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, TextConversions.parse("<bold><gold>IN PROGRESS")).build();
    public static ItemStack donePane = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE, TextConversions.parse("<bold><green>DONE")).build();
    public static ItemStack blankOrange = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, TextConversions.parse("")).build();
    public static ItemStack mysteryPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, TextConversions.parse("<gold>???")).build();
    public static ItemStack noneBarrier = new ItemBuilder(Material.BARRIER, TextConversions.parse("<red>None")).build();
    public static ItemStack backPage = new ItemBuilder(Material.ARROW, TextConversions.parse("<white>Back")).build();
    public static ItemStack exit = new ItemBuilder(Material.BARRIER, TextConversions.parse("<bold><red>Close Menu")).build();
    public static ItemStack start = new ItemBuilder(Material.FURNACE, TextConversions.parse("<bold><green>Start")).build();

}
