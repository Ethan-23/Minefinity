package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.FUEL_AMOUNT_KEY;
import static org.evasive.me.minefinity.utils.TextConversions.buildItemRarity;
import static org.evasive.me.minefinity.utils.TextConversions.buildItemType;

public class BaseFuelItem extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.FUEL_AMOUNT
    );

    private int fuelAmount;

    public BaseFuelItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity);
        this.fuelAmount = 1;
    }

    public BaseFuelItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        if(persistentDataContainer.has(FUEL_AMOUNT_KEY)){
            this.fuelAmount = persistentDataContainer.get(FUEL_AMOUNT_KEY, PersistentDataType.INTEGER);
        } else {
            this.fuelAmount = 1;
        }
    }

    public BaseFuelItem(String id, Material material, Rarity rarity, CustomItemType itemType, int fuelAmount) {
        super(id, material, id, rarity, itemType);
        this.fuelAmount = fuelAmount;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(int amount){
        this.fuelAmount = amount;
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();
        if(getFlavorText().isPresent()) lore.add(getFlavorText().get());
        lore.addAll(List.of(
                buildItemType(getCustomItemType().name()),
                "<gray> Efficiency: " + this.fuelAmount,
                "",
                buildItemRarity(getRarity())
        ));
        return lore;
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(FUEL_AMOUNT_KEY, PersistentDataType.INTEGER, this.fuelAmount)
                .build();
    }
}
