package org.evasive.me.minefinity.customItems.itembuilder.data.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemOptions;
import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.mining.abilities.PickaxeAbilities;
import org.evasive.me.minefinity.playerdata.stats.data.Stats;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.evasive.me.minefinity.core.utils.TextConversions.*;
import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;

public class BasePickaxeItem extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.STATS,
            ItemOptions.EQUIPMENT_SLOT,
            ItemOptions.PICKAXE_HEAD,
            ItemOptions.PICKAXE_CORE,
            ItemOptions.PICKAXE_HANDLE
    );

    //Eventually Update to objects when created
    private String pickaxeHeadId;
    private String pickaxeCoreId;
    private String pickaxeHandleId;
    private int pickaxeTier;

    public BasePickaxeItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        this.pickaxeHeadId = getOrDefault(pdc, PICKAXE_HEAD_KEY, PersistentDataType.STRING, null);
        this.pickaxeCoreId = getOrDefault(pdc, PICKAXE_CORE_KEY, PersistentDataType.STRING, null);
        this.pickaxeHandleId = getOrDefault(pdc, PICKAXE_HANDLE_KEY, PersistentDataType.STRING, null);
        this.pickaxeTier = getOrDefault(pdc, REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, 1);
    }

    public BasePickaxeItem(String id, Material material, String displayName, Rarity rarity){
        super(id, material, displayName, rarity, CustomItemType.PICKAXE);
        this.pickaxeHeadId = null;
        this.pickaxeCoreId = null;
        this.pickaxeHandleId = null;
        this.pickaxeTier = 1;
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
        List<String> lore = super.getLore();
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
        return lore;
    }

    @Override
    protected void getStatsLore(List<String> lore) {
        Map<String, Integer> statsMap = getStatsMap();

        PickaxeData pickaxeData = new PickaxeData(this);

        BasePickaxeComponent[] pickaxeComponents = pickaxeData.getPickaxeParts();

        if(statsMap == null || statsMap.isEmpty())
            return;

        for(Stats stats : Stats.values()){
            int amount = 0;
            for(BasePickaxeComponent pickaxeComponent : pickaxeComponents){
                if(pickaxeComponent == null) continue;
                amount += pickaxeComponent.getStatAmount(stats);
            }
            if(!statsMap.containsKey(stats.name()) && amount == 0)
                continue;
            amount += getStatAmount(stats);
            lore.add(stats.getDisplay() + ": " + amount);
        }
    }

    protected List<String> getLore(PickaxeData data) {
        List<String> lore = super.getLore();

        BasePickaxeComponent[] components = data != null ? data.getPickaxeParts() : new BasePickaxeComponent[]{null, null, null};

        List<String> pickaxeComponents = List.of("HEAD", "CORE", "HANDLE");
        List<String> ids = Arrays.asList(pickaxeHeadId, pickaxeCoreId, pickaxeHandleId);

        for (int i = 0; i < components.length; i++) {
            BasePickaxeComponent component = components[i];

            if (component == null) {
                String id = ids.get(i);
                if (id != null) {
                    lore.add("<bold>" + id + "</bold>");
                } else {
                    lore.add("<white>[<reset>" + buildRarityColor(getID().replace("TEMPLATE", pickaxeComponents.get(i)) + "<reset><white>]", this.getRarity()));
                }
                lore.add("");
                continue;
            }

            StringBuilder componentHeader = new StringBuilder("<white>[<reset>" + setRarityColor(component.getDisplayName(), component.getRarity()) + "<reset><white>]");

            Map<String, Integer> componentStats = component.getStatsMap();

            for(Stats stats : Stats.values()){
                if(!componentStats.containsKey(stats.name()))
                    continue;
                int value = componentStats.get(stats.name());
                componentHeader.append(" <reset>(").append(stats.getShortDisplay()).append(value < 0 ? "<red> " : " +").append(value).append("<reset>)");
            }

            lore.add(componentHeader.toString());

            for(String abilityId : component.getPickaxeAbilityList()){
                PickaxeAbilities pickaxeAbility = PickaxeAbilities.valueOf(abilityId);
                lore.add(pickaxeAbility.getAbilityDisplay());
            }

            lore.add("");
        }

        lore.add(buildItemRarity(getRarity(), getCustomItemType()));

        return lore;
    }

    public Map<String, Integer> getTotalStats(BasePickaxeComponent[] components) {

        Map<String, Integer> statsMap = getStatsMap();
        for(BasePickaxeComponent component : components) {

            if(component == null)
                continue;
            Map<String, Integer> componentStats = component.getStatsMap();
            if(componentStats.isEmpty())
                continue;

            for(String statId : componentStats.keySet()){
                statsMap.put(statId, statsMap.getOrDefault(statId, 0) + componentStats.get(statId));
            }
        }
        return statsMap;
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

    public int getPickaxeTier() {
        return pickaxeTier;
    }

    public void setPickaxeTier(int pickaxeTier) {
        this.pickaxeTier = pickaxeTier;
    }

    @Override
    public ItemStack buildItem() {
        ItemBuilder itemBuilder = new ItemBuilder(super.buildItem())
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
