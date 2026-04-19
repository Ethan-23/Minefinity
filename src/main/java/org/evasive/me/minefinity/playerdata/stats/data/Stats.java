package org.evasive.me.minefinity.playerdata.stats.data;

import org.bukkit.Material;

public enum Stats {

    HEALTH("Health", "❤", "#ff6161", Material.REDSTONE),
    REGENERATION("Regeneration", "❣", "#ff3d3d", Material.CAMPFIRE),
    DEFENSE("Defense", "\uD83D\uDEE1", "#47c44b", Material.SHIELD),
    STRENGTH("Strength", "⚔", "#de5500", Material.IRON_SWORD),
    CRITICAL_CHANCE("Critical Chance", "☣", "#3e71de", Material.TUBE_CORAL),
    CRITICAL_DAMAGE("Critical Damage", "♆", "#0a6680", Material.END_CRYSTAL),
    SPEED("Speed", "♢", "#eff74d", Material.SUGAR),

    BREAKING_POWER("Breaking Power", "♭", "#ff8400", Material.BEDROCK),
    MINING_SPEED("Mining Speed", "⛏", "#ff8400", Material.GOLDEN_PICKAXE),
    MINING_FORTUNE("Mining Fortune", "☘", "#ff8400", Material.GOLD_INGOT),
    WOODCUTTING_POWER("Woodcutting Power", "\uD83E\uDE93", "#ff8400", Material.GOLDEN_AXE),
    WOODCUTTING_FORTUNE("Woodcutting Fortune", "\uD83E\uDEB5", "#ff8400", Material.GOLD_INGOT),
    FISHING_SPEED("Fishing Speed", "\uD83D\uDC1F", "#52fff9", Material.FISHING_ROD),
    FISHING_LUCK("Fishing Luck", "\uD83E\uDE99", "#52fff9", Material.HEART_OF_THE_SEA),
    ANIMAL_SPEED("Animal Speed", "\uD83D\uDC04", "#ffd633", Material.WHEAT),
    ANIMAL_FORTUNE("Animal Fortune", "\uD83C\uDF3E", "#ffd633", Material.EGG),
    ;

    private final String name;
    private final String symbol;
    private final String display;
    private final String shortDisplay;
    private final String color;
    private final Material material;

    Stats(String name, String symbol, String color, Material material){
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.display = "<" + color + ">" + symbol + " " + name;
        this.shortDisplay = "<" + color + ">" + symbol;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplay() {
        return display;
    }

    public String getShortDisplay() {
        return shortDisplay;
    }

    public String getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }
}
