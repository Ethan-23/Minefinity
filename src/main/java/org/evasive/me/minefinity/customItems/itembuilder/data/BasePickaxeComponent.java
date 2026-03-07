package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.utils.TextConversions.buildItemRarity;
import static org.evasive.me.minefinity.utils.TextConversions.buildItemType;

public class BasePickaxeComponent extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.COMPONENT_ABILITY,
            ItemOptions.PICKAXE_TIER
    );

    private int requiredPickaxeTier;
    private List<PickaxeAbilities> pickaxeAbilityList;

    public BasePickaxeComponent (String id, Material material, String name, Rarity rarity) {
        super(id, material, name, rarity);
        this.requiredPickaxeTier = 1;
        this.pickaxeAbilityList =  new ArrayList<>(List.of());
    }

    public BasePickaxeComponent (ItemStack itemStack){
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.requiredPickaxeTier = getOrDefault(pdc, REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, 1);

        if(pdc.has(PICKAXE_ABILITY_KEY)){

            String joined = pdc.get(PICKAXE_ABILITY_KEY, PersistentDataType.STRING);
            List<PickaxeAbilities> abilities = new ArrayList<>();
            if (joined == null || joined.isEmpty()) {
                this.pickaxeAbilityList =  new ArrayList<>(List.of());;
                return;
            }
            for (String ability : joined.split(";;")) {
                abilities.add(PickaxeAbilities.valueOf(ability));
            }
            this.pickaxeAbilityList = abilities;

        } else {
            this.pickaxeAbilityList = new ArrayList<>(List.of());
        }
    }

    public int getRequiredPickaxeTier() {
        return requiredPickaxeTier;
    }

    public void setRequiredPickaxeTier(int requiredPickaxeTier) {
        this.requiredPickaxeTier = requiredPickaxeTier;
    }

    public List<PickaxeAbilities> getPickaxeAbilityList() {
        return pickaxeAbilityList;
    }

    public void changePickaxeAbilityList(PickaxeAbilities pickaxeAbilities) {
        if(pickaxeAbilityList.contains(pickaxeAbilities))
            pickaxeAbilityList.remove(pickaxeAbilities);
        else
            pickaxeAbilityList.add(pickaxeAbilities);
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();
        if(getFlavorText().isPresent()) lore.add(getFlavorText().get());
        lore.add(buildItemType(getCustomItemType().name()));
        for(PickaxeAbilities pickaxeAbility : pickaxeAbilityList){
            lore.add(pickaxeAbility.getAbilityDisplay());
        }
        lore.add(buildItemRarity(getRarity()));
        return lore;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(PICKAXE_ABILITY_KEY, PersistentDataType.STRING, pickaxeAbilityList.stream().map(Enum::name).collect(Collectors.joining(";;")))
                .addPersistentDataContainer(REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, requiredPickaxeTier)
                .build();
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }
}

