package org.evasive.me.minevolutionCore.customItems;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.itemID;

public interface ItemBuilder {
    Component getName();
    String getID();
    List<Component> getLore();
    Material getMaterial();
    boolean isGlowing();
    ItemStack getItem();

    default ItemStack buildItem(){
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(getName());
        meta.lore(getLore());
        if(isGlowing()){
            meta.addEnchant(Enchantment.LOYALTY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }else{
            meta.removeEnchant(Enchantment.LOYALTY);
        }
        meta.getPersistentDataContainer().set(itemID, PersistentDataType.STRING, getID());
        item.setItemMeta(meta);
        return item;
    }
}
