package org.evasive.me.minevolutionCore.customItems.pickaxes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minevolutionCore.customItems.ItemBuilder;
import org.evasive.me.minevolutionCore.enchantments.enchants.*;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;

import org.evasive.me.minevolutionCore.utils.RomanNumeralUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minevolutionCore.utils.ComponentUtils.makeText;
import static org.evasive.me.minevolutionCore.utils.PickaxeKeys.*;
import static org.evasive.me.minevolutionCore.utils.EnchantKeys.*;

public interface PickaxeItemBuilder extends ItemBuilder {

    @Override
    default Component getName(){
        return getName(1);
    }
    Component getName(int tier);
    default List<Component> getLore(){
        return getLore(1, null);
    }
    default List<Component> getLore(int tier, ItemMeta meta){
        List<Component> lore = new ArrayList<>();

        float miningSpeed = getSpeed(tier);
        float baseSpeed = getBaseSpeed();

        if(meta != null && meta.getPersistentDataContainer().has(efficiency)){
            miningSpeed = miningSpeed + (baseSpeed * ((meta.getPersistentDataContainer().get(efficiency, PersistentDataType.INTEGER) * 0.1f)));
        }

        lore.add(makeText("Mining Speed: ", NamedTextColor.GRAY, false).append(makeText(""+Math.round(miningSpeed * 100f)/100f, NamedTextColor.WHITE, true)));
        lore.add(Component.text(""));
        lore.add(makeText("Enchants:", NamedTextColor.GOLD, true));

        if(meta != null){
            List<PickaxeEnchantBuilder> order = Arrays.asList(new Critcal(), new Efficiency(), new Alchemist(), new Fortune(), new SuperBreaker(), new Explosive(), new Wisdom(), new Compact(), new OrbitalMiner());

            for (PickaxeEnchantBuilder tempEnchant : order){
                if(!meta.getPersistentDataContainer().has(tempEnchant.getKey())) {
                    continue;
                }
                int tempLevel = meta.getPersistentDataContainer().get(tempEnchant.getKey(), PersistentDataType.INTEGER);
                TextColor enchantColor = NamedTextColor.RED;
                TextColor symbolColor = NamedTextColor.WHITE;

                switch (tempEnchant.getRarity()){
                    case MINOR -> enchantColor = TextColor.fromHexString("#BFBFBF");
                    case UNIQUE -> enchantColor = TextColor.fromHexString("#55FF55");
                    case RADIANT -> enchantColor = TextColor.fromHexString("#00FFFF");
                    case EXQUISITE -> enchantColor = TextColor.fromHexString("#AA00AA");
                    case PRISTINE -> enchantColor = TextColor.fromHexString("#FFAA00");
                }

                if(tempLevel >= tempEnchant.getMaxLevel()){
                    symbolColor = NamedTextColor.RED;
                }

                Component enchantLore = ComponentUtils.makeText(tempEnchant.getSymbol(), symbolColor, false)
                        .append(ComponentUtils.makeText(" " + tempEnchant.getName(), enchantColor, false)
                                .append(ComponentUtils.makeText(" " + RomanNumeralUtil.intToRoman(tempLevel), enchantColor, true)));

                lore.add(enchantLore);
            }
        }

        lore.add(Component.text(""));
        lore.add(makeText("Progress:", NamedTextColor.GOLD, true));
        lore.add(makeText("|||||||||||||||||||||||||||||", NamedTextColor.RED, false));
        lore.add(makeText("Mine ", NamedTextColor.GRAY, false).append(makeText("0/" + getTierRequirement(tier) + " ", NamedTextColor.GOLD, false).append(makeText(getMaterialRequirement(tier).toString(), NamedTextColor.GRAY, false))));
        return lore;
    }

    Material getMaterialRequirement(int tier);
    int getTierRequirement(int tier);
    float getBaseSpeed();
    float getTierSpeed(int tier);
    float getSpeed(int tier);

    @Override
    default ItemStack buildItem() {
        return buildItem(1, 0, null);
    }

    default ItemStack buildItem(int tier, int tierBlocks, ItemMeta oldMeta){
        ItemStack item = ItemBuilder.super.buildItem();
        ItemMeta meta = item.getItemMeta();
        if(oldMeta != null)
            meta = oldMeta;
        meta.displayName(getName(tier));

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(holdingPickaxeKey, PersistentDataType.BOOLEAN, true);
        data.set(baseSpeedKey, PersistentDataType.FLOAT, getBaseSpeed());
        data.set(tierSpeedKey, PersistentDataType.FLOAT, getTierSpeed(tier));
        data.set(tierRequirementKey, PersistentDataType.STRING, getMaterialRequirement(tier).toString());
        data.set(tierBlocksCapKey, PersistentDataType.INTEGER, getTierRequirement(tier));
        data.set(tierBlocksKey, PersistentDataType.INTEGER, tierBlocks);
        data.set(tierKey, PersistentDataType.INTEGER, tier);

        meta.lore(getLore(tier, meta));

        item.setItemMeta(meta);

        return item;
    }
}
