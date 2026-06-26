package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;

import java.util.*;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BasePartItem extends BaseCustomItem {

    public BasePartItem(String id, Material material, String name, Rarity rarity) {
        super(id, material, name, rarity);
    }

    public BasePartItem(ItemStack itemStack){
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();


    }




    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();


        return lore;
    }


    @Override
    public ItemStack buildItem() {


        return new ItemBuilder(super.buildItem())
                .build();
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePartItem(this.buildItem());
    }
}

