package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.*;

import static org.evasive.me.minefinity.core.utils.TextConversions.*;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BasePickaxeItem extends BaseToolItem {


    public BasePickaxeItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

    }

    public BasePickaxeItem(String id, Material material, String displayName, Rarity rarity){
        super(id, material, displayName, rarity, CustomItemType.PICKAXE);
    }


    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();

        for(Map.Entry<PartSlots, String> entry : getPartMap().entrySet()) {
            if(entry.getValue() == null) {
                //Need to update
                lore.add(buildRarityColor(getID().split("_")[0] + "_" + entry.getKey().name(), getRarity()));
            }else {
                lore.add("<bold>" + entry.getValue() + "</bold>");
            }
        }
        return lore;
    }



    @Override
    public ItemStack buildItem() {
        ItemBuilder itemBuilder = new ItemBuilder(super.buildItem())
                .addUnbreakable();



        return itemBuilder.build().clone();
    }



    public ItemStack buildItem(ToolItemData data) {
        ItemBuilder itemBuilder = new ItemBuilder(buildItem());
        return itemBuilder.build().clone();
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePickaxeItem(this.buildItem());
    }

}
