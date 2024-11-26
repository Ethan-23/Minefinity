package org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects;

import java.util.List;

public class RunicMatrix {

    private final EnchantableItem enchantableItem;

    private List<EnchantOrb> enchants;

    public RunicMatrix(EnchantableItem enchantableItem, List<EnchantOrb> enchants) {
        this.enchantableItem = enchantableItem;
        this.enchants = enchants;
    }

    public EnchantableItem getEnchantableItem() {
        return enchantableItem;
    }

    public List<EnchantOrb> getEnchants() {
        return enchants;
    }

    public void setEnchants(List<EnchantOrb> enchants) {
        this.enchants = enchants;
    }

    public void addEnchant(EnchantOrb enchantOrb){
        this.enchants.add(enchantOrb);
    }

}
