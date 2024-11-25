package org.evasive.me.minevolutionCore.arcanecrafting;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RunicMatrix {

    private ItemStack pedestalMain;
    private List<Integer> rotationRunnables;
    private ItemDisplay itemDisplayMain;
    private List<ItemDisplay> enchants;

    public RunicMatrix(ItemStack pedestalMain, ItemDisplay itemDisplayMain, List<Integer> runnable) {
        this.pedestalMain = pedestalMain;
        this.itemDisplayMain = itemDisplayMain;
        this.rotationRunnables = runnable;
    }

    public ItemStack getPedestalMain() {
        return pedestalMain;
    }

    public ItemDisplay getItemDisplayMain(){
        return itemDisplayMain;
    }

    public void setItemDisplayMain(ItemDisplay itemDisplay){
        itemDisplayMain = itemDisplay;
    }

    public void setPedestalMain(ItemStack pedestalMain) {
        this.pedestalMain = pedestalMain;
    }

    public List<Integer> getRotationScheduler() {
        return rotationRunnables;
    }

    public void addScheduler(int runnable){
        rotationRunnables.add(runnable);
    }

    public List<ItemDisplay> getEnchants() {
        return enchants;
    }

    public void setEnchants(List<ItemDisplay> enchants) {
        this.enchants = enchants;
    }

    public void addEnchants(ItemDisplay itemDisplay){
        this.enchants.add(itemDisplay);
    }
}
