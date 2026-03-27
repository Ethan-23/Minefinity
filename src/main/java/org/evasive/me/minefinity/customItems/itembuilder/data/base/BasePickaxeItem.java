package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.core.utils.TextConversions.*;

public class BasePickaxeItem extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.MINING_SPEED,
            ItemOptions.MINING_FORTUNE,
            ItemOptions.PICKAXE_HEAD,
            ItemOptions.PICKAXE_CORE,
            ItemOptions.PICKAXE_HANDLE,
            ItemOptions.PICKAXE_TIER
    );

    private float baseMiningSpeed;
    private float baseMiningFortune;
    //Eventually Update to objects when created
    private String pickaxeHeadId;
    private String pickaxeCoreId;
    private String pickaxeHandleId;
    private int pickaxeTier;

    public BasePickaxeItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.baseMiningSpeed = getOrDefault(pdc, MINING_SPEED_KEY, PersistentDataType.FLOAT, 1f);
        this.baseMiningFortune = getOrDefault(pdc, MINING_FORTUNE_KEY, PersistentDataType.FLOAT, 0f);
        this.pickaxeHeadId = getOrDefault(pdc, PICKAXE_HEAD_KEY, PersistentDataType.STRING, null);
        this.pickaxeCoreId = getOrDefault(pdc, PICKAXE_CORE_KEY, PersistentDataType.STRING, null);
        this.pickaxeHandleId = getOrDefault(pdc, PICKAXE_HANDLE_KEY, PersistentDataType.STRING, null);
        this.pickaxeTier = getOrDefault(pdc, REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, 1);
    }

    public BasePickaxeItem(String id, Material material, String displayName, Rarity rarity){
        super(id, material, displayName, rarity, CustomItemType.PICKAXE);
        this.baseMiningSpeed = 1f;
        this.baseMiningFortune = 0;
        this.pickaxeHeadId = null;
        this.pickaxeCoreId = null;
        this.pickaxeHandleId = null;
        this.pickaxeTier = 1;
    }

    public float getBaseMiningSpeed() {
        return baseMiningSpeed;
    }

    public String getPickaxeHeadId() {
        return pickaxeHeadId;
    }

    public String getPickaxeCoreId() {
        return pickaxeCoreId;
    }

    public String getPickaxeHandleId() {
        return pickaxeHandleId;
    }

    @Override
    protected List<String> getLore() {

        List<String> lore = new ArrayList<>();

        if(getFlavorText().isPresent()) lore.add(getFlavorText().get());
        lore.add("<gray>Mining Speed: <white>" + String.format("%.2f", this.baseMiningSpeed));
        lore.add("");

        String[] componentNames = {"HEAD", "CORE", "HANDLE"};
        List<String> components = Arrays.asList(this.pickaxeHeadId, this.pickaxeCoreId, this.pickaxeHandleId);

        for(int i = 0; i < componentNames.length; i++) {
            String pickaxeComponent = components.get(i);
            if(pickaxeComponent == null) {
                lore.add(buildRarityColor(getID().replace("TEMPLATE", componentNames[i]), getRarity()));
            }else {
                lore.add("<bold>" + pickaxeComponent + "</bold>");
                //lore.add(pickaxeComponent.getBaseItem().getPickaxeAbility().getAbilityDisplay());
            }
        }

        lore.add("");
        lore.add(buildRarityColor(getID(), getRarity()));
        lore.add(buildItemRarity(getRarity(), getCustomItemType()));
        return lore;
    }

    protected List<String> getLore(PickaxeData data) {
        List<String> lore = new ArrayList<>();

        lore.add("");

        BasePickaxeComponent[] components = data != null ? new BasePickaxeComponent[]{
                data.getHead(),
                data.getCore(),
                data.getHandle()
        } : new BasePickaxeComponent[]{null, null, null};

        if(getFlavorText().isPresent()) lore.add(getFlavorText().get());

        lore.add("<gray>Mining Speed <gold>⛏ " + String.format("%.2f", calculateMiningSpeed(components)));
        lore.add("<gray>Mining Fortune <gold>☘ " + String.format("%.2f", calculateMiningFortune(components)));
        lore.add("");

        String[] names = {"HEAD", "CORE", "HANDLE"};


        List<String> ids = Arrays.asList(pickaxeHeadId, pickaxeCoreId, pickaxeHandleId);

        for (int i = 0; i < names.length; i++) {
            BasePickaxeComponent component = components[i];

            if (component != null) {

                StringBuilder componentHeader = new StringBuilder("<white>[<reset>" + setRarityColor(component.getDisplayName(), component.getRarity()) + "<reset><white>]");

                if(component.getMiningSpeed() > 0)
                    componentHeader.append(" <gold>(⛏ ").append(component.getMiningSpeed()).append(")");
                if(component.getMiningFortune() > 0)
                    componentHeader.append(" <gold>(☘ ").append(component.getMiningFortune()).append(")");

                lore.add(componentHeader.toString());

                for(String abilityId : component.getPickaxeAbilityList()){
                    PickaxeAbilities pickaxeAbility = PickaxeAbilities.valueOf(abilityId);
                    lore.add(pickaxeAbility.getAbilityDisplay());
                }
            } else {
                String id = ids.get(i);
                if (id != null) {
                    lore.add("<bold>" + id + "</bold>");
                } else {
                    lore.add("<white>[<reset>" + buildRarityColor(getID().replace("TEMPLATE", names[i]), getRarity()) + "<reset><white>]");
                }
            }
            lore.add("");
        }

        lore.add(buildItemRarity(getRarity(), getCustomItemType()));

        return lore;
    }

    public float calculateMiningSpeed(BasePickaxeComponent[] components) {
        float miningSpeed = this.baseMiningSpeed;
        for(BasePickaxeComponent component : components) {
            if(component == null)
                continue;
            miningSpeed += component.getMiningSpeed();
        }
        return miningSpeed;
    }

    public float calculateMiningFortune(BasePickaxeComponent[] components) {
        float miningSpeed = this.baseMiningFortune;
        for(BasePickaxeComponent component : components) {
            if(component == null)
                continue;
            miningSpeed += component.getMiningFortune();
        }
        return miningSpeed;
    }

    public void setPickaxeHeadId(String pickaxeComponentId){
        this.pickaxeHeadId = pickaxeComponentId;
    }

    public void setPickaxeCoreId(String pickaxeComponentId){
        this.pickaxeCoreId = pickaxeComponentId;
    }

    public void setPickaxeHandleId(String pickaxeComponentId){
        this.pickaxeHandleId = pickaxeComponentId;
    }

    public void setBaseMiningSpeed(float baseMiningSpeed) {
        this.baseMiningSpeed = baseMiningSpeed;
    }

    public void setBaseMiningFortune(float baseMiningFortune) {
        this.baseMiningFortune = baseMiningFortune;
    }

    public float getBaseMiningFortune() {
        return baseMiningFortune;
    }

    public int getPickaxeTier() {
        return pickaxeTier;
    }

    public void setPickaxeTier(int pickaxeTier) {
        this.pickaxeTier = pickaxeTier;
    }

    @Override
    public ItemStack buildItem() {
        ItemBuilder itemBuilder = new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(MINING_SPEED_KEY, PersistentDataType.FLOAT, this.baseMiningSpeed)
                .addPersistentDataContainer(MINING_FORTUNE_KEY, PersistentDataType.FLOAT, this.baseMiningFortune)
                .addUnbreakable();

        if(pickaxeHeadId != null)
            itemBuilder.addPersistentDataContainer(PICKAXE_HEAD_KEY, PersistentDataType.STRING, pickaxeHeadId);
        if(pickaxeCoreId != null)
            itemBuilder.addPersistentDataContainer(PICKAXE_CORE_KEY, PersistentDataType.STRING, pickaxeCoreId);
        if(pickaxeHandleId != null)
            itemBuilder.addPersistentDataContainer(PICKAXE_HANDLE_KEY, PersistentDataType.STRING, pickaxeHandleId);


        return itemBuilder.build().clone();
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

    public ItemStack buildItem(PickaxeData data) {
        ItemBuilder itemBuilder = new ItemBuilder(buildItem());
        itemBuilder.setLore(getLore(data));
        return itemBuilder.build().clone();
    }

    @Override
    public BaseCustomItem copy() {
        return new BasePickaxeItem(this.buildItem());
    }

}
