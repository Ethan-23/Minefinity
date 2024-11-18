package org.evasive.me.minevolutionCore.mining.customItems.pickaxes;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.utils.EnchantKeys;
import org.evasive.me.minevolutionCore.utils.PickaxeKeys;

public class PickaxeStatFunctions {

    public boolean holdingPickaxe(ItemMeta meta){
        if(!meta.getPersistentDataContainer().has(PickaxeKeys.holdingPickaxeKey, PersistentDataType.BOOLEAN))
            return false;
        return meta.getPersistentDataContainer().get(PickaxeKeys.holdingPickaxeKey, PersistentDataType.BOOLEAN);
    }

    public float getBaseMiningSpeed(ItemMeta meta){
        return meta.getPersistentDataContainer().get(PickaxeKeys.baseSpeedKey, PersistentDataType.FLOAT);
    }

    public int getBaseEfficiencyLevel(ItemMeta meta){
        if(!meta.getPersistentDataContainer().has(EnchantKeys.efficiency))
            return 0;
        return meta.getPersistentDataContainer().get(EnchantKeys.efficiency, PersistentDataType.INTEGER);
    }


}
