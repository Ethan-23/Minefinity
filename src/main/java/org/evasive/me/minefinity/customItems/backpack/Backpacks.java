package org.evasive.me.minefinity.customItems.backpack;

import org.bukkit.Material;
import org.evasive.me.minefinity.core.items.CustomItem;
import org.evasive.me.minefinity.customItems.types.CustomItemType;
import org.evasive.me.minefinity.customItems.types.FuelItem;
import org.evasive.me.minefinity.customItems.types.ResourceItem;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.Set;

public enum Backpacks implements CustomItem {
    RESOURCE_BACKPACK(Material.SUGAR,
            Material.GREEN_BUNDLE,
            Rarity.UNIQUE,
            640,
            Set.of(
                    ResourceItem.OAK_PLANK.getID(),
                    ResourceItem.GRAVEL.getID(),
                    ResourceItem.CLAY_BALL.getID(),
                    ResourceItem.COBBLESTONE.getID(),
                    ResourceItem.ANDESITE.getID(),
                    ResourceItem.TUFF.getID(),
                    ResourceItem.GRANITE.getID(),
                    ResourceItem.MOSSY_COBBLESTONE.getID(),
                    ResourceItem.DIORITE.getID(),
                    ResourceItem.CALCITE.getID(),
                    ResourceItem.DEEPSLATE.getID(),
                    ResourceItem.OBSIDIAN.getID(),
                    ResourceItem.REINFORCED_DEEPSLATE.getID()
            )
    ),
    SPECIAL_BACKPACK(Material.SUGAR,
            Material.GREEN_BUNDLE,
            Rarity.UNIQUE,
            640,
            Set.of(
                    ResourceItem.FLINT.getID(),
                    ResourceItem.ROCK.getID(),
                    ResourceItem.MOSS.getID()
            )
    ),
    ORE_BACKPACK(Material.SUGAR,
            Material.CYAN_BUNDLE,
            Rarity.RADIANT,
            640,
            Set.of(
                    ResourceItem.RAW_COPPER.getID(),
                    ResourceItem.COPPER_INGOT.getID(),
                    ResourceItem.RAW_IRON.getID(),
                    ResourceItem.IRON_INGOT.getID(),
                    ResourceItem.RAW_TIN.getID(),
                    ResourceItem.TIN_INGOT.getID(),
                    ResourceItem.BRONZE_INGOT.getID(),
                    ResourceItem.LAPIS.getID(),
                    ResourceItem.REDSTONE.getID(),
                    ResourceItem.RAW_GOLD.getID(),
                    ResourceItem.GOLD_INGOT.getID(),
                    ResourceItem.AMETHYST.getID(),
                    ResourceItem.DIAMOND.getID(),
                    ResourceItem.EMERALD.getID(),
                    ResourceItem.DIAMOND_ORE.getID(),
                    ResourceItem.EMERALD_ORE.getID()
            )
    ),
    CRAFTABLES_BACKPACK(Material.SUGAR,
            Material.RED_BUNDLE,
            Rarity.RADIANT,
            640,
            Set.of(
                    ResourceItem.TIMBER.getID(),
                    ResourceItem.MOSSY_TIMBER.getID(),
                    ResourceItem.ROCKY_STONE.getID(),
                    ResourceItem.TOUGH_STONE.getID(),
                    ResourceItem.BRICK.getID(),
                    ResourceItem.STONE.getID()
            )
    ),
    FUEL_BACKPACK(Material.SUGAR,
            Material.BLACK_BUNDLE,
            Rarity.EXQUISITE,
            640,
            Set.of(
                    FuelItem.BARK.getID(),
                    FuelItem.CHARCOAL.getID(),
                    FuelItem.COAL.getID(),
                    FuelItem.ENRICHED_COAL.getID(),
                    FuelItem.MAGMA_COAL.getID()
            )
    ),


    ;

    private final BaseBackpackItem builder;
    Backpacks(Material material, Material visual, Rarity rarity, int storedItemAmount, Set<String> storedItemIdList) {
        // Use the enum constant name as the ID automatically
        this.builder = new BaseBackpackItem(this.name(), material, visual, rarity, CustomItemType.STORAGE, storedItemAmount, storedItemIdList);
    }

    @Override
    public String getID() {
        return builder.getID();
    }

    public static boolean contains(String value) {
        for (Backpacks backpacks : Backpacks.values()) {
            if (backpacks.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BaseBackpackItem getBuilder() {
        return builder;
    }
}
