package org.evasive.me.minevolutionCore.customItems.pickaxes;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import static org.evasive.me.minevolutionCore.utils.EnchantKeys.*;
import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.*;

public class PickaxeStatFunctions {

    public boolean holdingPickaxe(ItemMeta meta){
        if(!meta.getPersistentDataContainer().has(holdingPickaxeKey, PersistentDataType.BOOLEAN))
            return false;
        return true;
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

        if(tierProgress >= tierCompletion)
            return false;

        return true;
    }

    public ItemStack tierUp(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();

        String id = meta.getPersistentDataContainer().get(itemID, PersistentDataType.STRING);
        int current = meta.getPersistentDataContainer().get(tierKey, PersistentDataType.INTEGER);

        current++;
        return PickaxeItems.valueOf(id).getPickaxeBuilder().buildItem(current, 0, meta);
    }

}
