package org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.evasive.me.minevolutionCore.enchanting.enchantments.enchants.EnchantType;

public class EnchantOrb {
    private EnchantType enchantType;
    private int successRate;
    private ItemDisplay itemDisplay;
    private TextDisplay textDisplay;
    private Interaction interaction;
    private int runnable;

    public EnchantOrb(EnchantType enchantType, int successRate, ItemDisplay itemDisplay, TextDisplay textDisplay, Interaction interaction, int runnable) {
        this.enchantType = enchantType;
        this.successRate = successRate;
        this.itemDisplay = itemDisplay;
        this.textDisplay = textDisplay;
        this.interaction = interaction;
        this.runnable = runnable;
    }

    public EnchantType getEnchantType() {
        return enchantType;
    }

    public void setEnchantType(EnchantType enchantType) {
        this.enchantType = enchantType;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public int getRunnable() {
        return runnable;
    }

    public void setRunnable(int runnable) {
        this.runnable = runnable;
    }

    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    public void setTextDisplay(TextDisplay textDisplay) {
        this.textDisplay = textDisplay;
    }
}
