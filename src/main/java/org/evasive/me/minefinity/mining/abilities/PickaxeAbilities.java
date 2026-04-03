package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;

public enum PickaxeAbilities {
    CRITICAL_FRACTURE(
            "Adds critical points to blocks that give you increased mining speed if you hit them.",
            Rarity.UNIQUE
    ),
    FORTUNE(
            "Gives you a chance of gaining double resources.",
            Rarity.MINOR
    ),
    METAL_DETECT(
            "Increases the chance of finding secondary resources.",
            Rarity.UNIQUE
    ),
    STRUCTURED_FORCE(
            "Mining the same block without stopping grows ramping speed.",
            Rarity.RADIANT
    ),
    EARLY_BIRD(
            "Increased mining speed on Breaking Power 1 resources.",
            Rarity.MINOR
    ),
    FRACTURED_VEIN(
            "The next block will spawn partially broken.",
            Rarity.UNIQUE
    ),
    MOLDABLE_GRIP(
            "Small increase of stats of all other parts.",
            Rarity.UNIQUE
    ),
    SEISMIC_TAP(
            "Gives a chance to erupt the ground brining you more resources.",
            Rarity.MINOR
    ),
    ITCHY(
            "Has a chance to give you bursts of mining speed.",
            Rarity.UNIQUE
    ),
    FILLER(
            "replace later",
            Rarity.UNIQUE
    );

    private final String description;
    private final Rarity rarity;

    PickaxeAbilities(String description, Rarity rarity) {
        this.description = description;
        this.rarity = rarity;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getAbilityDisplay(){
        return TextConversions.buildRarityColor(this.name(), this.getRarity()) + " <dark_gray>- <gray>" + this.getDescription();
    }
}
