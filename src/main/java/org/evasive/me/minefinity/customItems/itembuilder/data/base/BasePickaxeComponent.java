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

import static org.evasive.me.minefinity.core.utils.TextConversions.*;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BasePickaxeComponent extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.COMPONENT_ABILITY,
            ItemOptions.COMPONENT_TIER,
            ItemOptions.MINING_SPEED,
            ItemOptions.MINING_FORTUNE
    );

    private float miningSpeed;
    private float miningFortune;
    private int requiredPickaxeTier;
    private final List<String> pickaxeAbilityList;

    public BasePickaxeComponent (String id, Material material, String name, Rarity rarity) {
        super(id, material, name, rarity);
        this.miningSpeed = 0;
        this.miningFortune = 0;
        this.requiredPickaxeTier = 1;
        this.pickaxeAbilityList =  new ArrayList<>(List.of());
    }

    public BasePickaxeComponent (ItemStack itemStack){
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.requiredPickaxeTier = getOrDefault(pdc, REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, 1);
        this.miningSpeed = getOrDefault(pdc, MINING_SPEED_KEY, PersistentDataType.FLOAT, 0f);
        this.miningFortune = getOrDefault(pdc, MINING_FORTUNE_KEY, PersistentDataType.FLOAT, 0f);

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

    public int getRequiredPickaxeTier() {
        return requiredPickaxeTier;
    }

    public void setRequiredPickaxeTier(int requiredPickaxeTier) {
        this.requiredPickaxeTier = requiredPickaxeTier;
    }

    public List<String> getPickaxeAbilityList() {
        return pickaxeAbilityList;
    }

    public float getMiningSpeed() {
        return miningSpeed;
    }

    public float getMiningFortune() {
        return miningFortune;
    }

    public void setMiningFortune(float miningFortune) {
        this.miningFortune = miningFortune;
    }

    public void setMiningSpeed(float miningSpeed) {
        this.miningSpeed = miningSpeed;
    }

    public void changePickaxeAbilityList(String abilityId) {
        if(pickaxeAbilityList.contains(abilityId))
            pickaxeAbilityList.remove(abilityId);
        else
            pickaxeAbilityList.add(abilityId);
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();
        if(getFlavorText().isPresent())
            lore.add(getFlavorText().get());

        if(miningSpeed > 0)
            lore.add("<gray>Mining Speed: <gold>⛏ " + miningSpeed);
        if(miningFortune > 0)
            lore.add("<gray>Mining Fortune: <gold>☘ " + miningFortune);
        lore.add("");
        for(String pickaxeAbilityId : pickaxeAbilityList){
            lore.add(PickaxeAbilities.valueOf(pickaxeAbilityId).getAbilityDisplay());
            lore.add("");
        }
        lore.add(buildItemRarity(getRarity(), getCustomItemType()));
        return lore;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(PICKAXE_ABILITY_KEY, PersistentDataType.STRING, String.join(";;", pickaxeAbilityList))
                .addPersistentDataContainer(REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, requiredPickaxeTier)
                .addPersistentDataContainer(MINING_SPEED_KEY, PersistentDataType.FLOAT, miningSpeed)
                .addPersistentDataContainer(MINING_FORTUNE_KEY, PersistentDataType.FLOAT, miningFortune)
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

