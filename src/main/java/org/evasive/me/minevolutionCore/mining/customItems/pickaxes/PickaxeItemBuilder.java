package org.evasive.me.minevolutionCore.mining.customItems.pickaxes;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.mining.customItems.ItemBuilder;

import java.util.List;

import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.*;
import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.tierKey;

public interface PickaxeItemBuilder extends ItemBuilder {

    Component getName();
    default List<Component> getLore(){
        return getLore(1);
    }
    List<Component> getLore(int tier);
    Material getMaterialRequirement(int tier);
    int getTierRequirement(int tier);
    int getMaxTier();
    float getBaseSpeed(int tier);

    @Override
    default ItemStack buildItem() {
        return buildItem(1, 0);
    }

    default ItemStack buildItem(int tier, int tierBlocks){
        ItemStack item = ItemBuilder.super.buildItem();
        ItemMeta meta = item.getItemMeta();
        meta.lore(getLore(tier));

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(holdingPickaxeKey, PersistentDataType.BOOLEAN, true);
        data.set(baseSpeedKey, PersistentDataType.FLOAT, getBaseSpeed(tier));
        data.set(tierRequirementKey, PersistentDataType.STRING, getMaterialRequirement(tier).toString());
        data.set(tierBlocksCapKey, PersistentDataType.INTEGER, getTierRequirement(tier));
        data.set(tierBlocksKey, PersistentDataType.INTEGER, tierBlocks);
        data.set(tierKey, PersistentDataType.INTEGER, tier);

        item.setItemMeta(meta);

        return item;
    }
}
