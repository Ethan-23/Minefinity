package org.evasive.me.minevolutionCore.mining;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.customItems.PickaxeItems;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;

import static org.evasive.me.minevolutionCore.utils.EnchantKeys.*;
import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.*;

public class PickaxeStatFunctions {

    public boolean notHoldingPickaxe(ItemMeta meta){
        return !meta.getPersistentDataContainer().has(holdingPickaxeKey, PersistentDataType.BOOLEAN);
    }

    public float getBaseMiningSpeed(ItemMeta meta){
        return meta.getPersistentDataContainer().get(baseSpeedKey, PersistentDataType.FLOAT);
    }

    public float getTierMiningSpeed(ItemMeta meta){
        return meta.getPersistentDataContainer().get(tierSpeedKey, PersistentDataType.FLOAT);
    }

    public int getBaseEfficiencyLevel(ItemMeta meta){
        if(!meta.getPersistentDataContainer().has(efficiency))
            return 0;
        return meta.getPersistentDataContainer().get(efficiency, PersistentDataType.INTEGER);
    }

    public boolean canGainProgress(ItemMeta meta){
        int tierProgress = meta.getPersistentDataContainer().get(tierBlocksKey, PersistentDataType.INTEGER);
        int tierCompletion = meta.getPersistentDataContainer().get(tierBlocksCapKey, PersistentDataType.INTEGER);

        return tierProgress < tierCompletion;
    }

    public ItemStack tierUp(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();

        String id = meta.getPersistentDataContainer().get(itemID, PersistentDataType.STRING);
        int current = meta.getPersistentDataContainer().get(tierKey, PersistentDataType.INTEGER);

        current++;
        return PickaxeItems.valueOf(id).getPickaxeBuilder().buildItem(current, 0, meta);
    }

    public float getMiningSpeed(ItemMeta meta){
        float baseSpeed = meta.getPersistentDataContainer().get(baseSpeedKey, PersistentDataType.FLOAT);
        float tierSpeed = meta.getPersistentDataContainer().get(tierSpeedKey, PersistentDataType.FLOAT);
        float efficiencySpeed = 1f;
        if(meta.getPersistentDataContainer().has(efficiency))
            efficiencySpeed = 1 + (meta.getPersistentDataContainer().get(EnchantKeys.efficiency, PersistentDataType.INTEGER) * 0.1f);
        return (baseSpeed * efficiencySpeed) + tierSpeed;
    }

}
