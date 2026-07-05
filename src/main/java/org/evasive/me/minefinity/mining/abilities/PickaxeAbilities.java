package org.evasive.me.minefinity.mining.abilities;

import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;

public enum PickaxeAbilities {
    //Minor
    CRITICAL_FRACTURE(
            "Adds critical points to blocks that give you increased mining speed if you hit them.",
            Rarity.MINOR
    ),
    STRUCTURED_FORCE(
            "Mining the same block without stopping grows ramping speed.",
            Rarity.MINOR
    ),
    MOLDABLE_GRIP(
            "Gives a 10% increase to your mining speed",
            Rarity.MINOR
    ),

    //Unique
    SEISMIC_TAP(
            "Mining a block has a chance to erupt the ground causing more blocks to be broken",
            Rarity.UNIQUE
    ),
    EARLY_BIRD(
            "Increased mining speed on Breaking Power 1 resources.",
            Rarity.UNIQUE
    ),
    METAL_DETECT(
            "Increases the chance of finding secondary resources.",
            Rarity.UNIQUE
    ),

    //Radiant?
    FRACTURED_VEIN(
            "Blocks under your breaking power will spawn partially broken",
            Rarity.RADIANT
    ),
    ITCHY(
            "Your hands will start to itch after breaking blocks causing you to gain a burst of mining speed",
            Rarity.RADIANT
    )
    ;

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

    public static PickaxeAbilities getPickaxeAbilities(String abilityId){
        try {
            return PickaxeAbilities.valueOf(abilityId);
        }catch (IllegalArgumentException e){
            Minefinity.SendLogMessage("Invalid Pickaxe Ability: " + abilityId);
            return null;
        }
    }
}
