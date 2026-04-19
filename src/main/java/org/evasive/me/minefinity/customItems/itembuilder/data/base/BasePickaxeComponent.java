package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;

import java.util.*;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BasePickaxeComponent extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.COMPONENT_ABILITY,
            ItemOptions.STATS
    );

    private final List<String> pickaxeAbilityList;

    public BasePickaxeComponent (String id, Material material, String name, Rarity rarity) {
        super(id, material, name, rarity);
        this.pickaxeAbilityList =  new ArrayList<>(List.of());
    }

    public BasePickaxeComponent (ItemStack itemStack){
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if(pdc.has(PICKAXE_ABILITY_KEY)){

            String joined = pdc.get(PICKAXE_ABILITY_KEY, PersistentDataType.STRING);
            if (joined == null || joined.isEmpty()) {
                this.pickaxeAbilityList =  new ArrayList<>(List.of());;
                return;
            }
            this.pickaxeAbilityList = new ArrayList<>(Arrays.asList(joined.split(";;")));

        } else {
            this.pickaxeAbilityList = new ArrayList<>(List.of());
        }
    }

    public List<String> getPickaxeAbilityList() {
        return pickaxeAbilityList;
    }

    public void changePickaxeAbilityList(String abilityId) {
        if(pickaxeAbilityList.contains(abilityId))
            pickaxeAbilityList.remove(abilityId);
        else
            pickaxeAbilityList.add(abilityId);
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();

        for(String pickaxeAbilityId : pickaxeAbilityList){
            lore.add(PickaxeAbilities.valueOf(pickaxeAbilityId).getAbilityDisplay());
        }

        return lore;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(PICKAXE_ABILITY_KEY, PersistentDataType.STRING, String.join(";;", pickaxeAbilityList))
                .build();
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePickaxeComponent(this.buildItem());
    }
}

