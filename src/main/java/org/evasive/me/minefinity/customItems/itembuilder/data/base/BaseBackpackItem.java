package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StorageAmountComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.StorageListComponent;

import java.util.List;
import java.util.stream.Collectors;

public class BaseBackpackItem extends BaseCustomItem {

    public BaseBackpackItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity, CustomItemType.STORAGE);
    }

    public BaseBackpackItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        addComponent(new StorageAmountComponent());
        addComponent(new StorageListComponent());
    }

    public StorageAmountComponent storageAmountComponent() {
        return getComponent(StorageAmountComponent.class);
    }

    public StorageListComponent storageListComponent() {
        return getComponent(StorageListComponent.class);
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();

        lore.add("");
        lore.add("<gray>Item pickups go directly into your backpack");
        lore.add("");
        lore.add("<gray>Items: " + storageListComponent().getValue().stream()
                .map(id -> "<green>" + TextConversions.formatItemName(id.replace("\\", "\\\\")))
                .collect(Collectors.joining("<gray>, ")));
        lore.add("");
        lore.add("<gray>Capacity <yellow>" + storageAmountComponent().getValue() + " of each held item");
        lore.add("");
        lore.add("<yellow>Right Click to open backpack!");

        return lore;
    }

    @Override
    public BaseCustomItem copy() {
        return new BaseBackpackItem(this.buildItem());
    }
}
