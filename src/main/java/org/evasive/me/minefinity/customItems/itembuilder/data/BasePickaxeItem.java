package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.rarity.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.utils.TextConversions.*;

public class BasePickaxeItem extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.MINING_SPEED,
            ItemOptions.PICKAXE_HEAD,
            ItemOptions.PICKAXE_CORE,
            ItemOptions.PICKAXE_HANDLE,
            ItemOptions.PICKAXE_TIER
    );

    private float baseMiningSpeed;
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
        this.pickaxeHeadId = getOrDefault(pdc, PICKAXE_HEAD_KEY, PersistentDataType.STRING, null);
        this.pickaxeCoreId = getOrDefault(pdc, PICKAXE_CORE_KEY, PersistentDataType.STRING, null);
        this.pickaxeHandleId = getOrDefault(pdc, PICKAXE_HANDLE_KEY, PersistentDataType.STRING, null);
        this.pickaxeTier = getOrDefault(pdc, REQUIRED_PICKAXE_TIER_KEY, PersistentDataType.INTEGER, 1);
    }

    public BasePickaxeItem(String id, Material material, String displayName, Rarity rarity){
        super(id, material, displayName, rarity, CustomItemType.PICKAXE);
        this.baseMiningSpeed = 1f;
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
        lore.add(buildItemRarity(getRarity()));
        return lore;
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

    public int getPickaxeTier() {
        return pickaxeTier;
    }

    public void setPickaxeTier(int pickaxeTier) {
        this.pickaxeTier = pickaxeTier;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(MINING_SPEED_KEY, PersistentDataType.FLOAT, this.baseMiningSpeed)
                .addPersistentDataContainer(PICKAXE_HEAD_KEY, PersistentDataType.STRING, pickaxeHeadId == null? "NONE" : pickaxeHeadId)
                .addPersistentDataContainer(PICKAXE_CORE_KEY, PersistentDataType.STRING, pickaxeCoreId == null? "NONE" : pickaxeCoreId)
                .addPersistentDataContainer(PICKAXE_HANDLE_KEY, PersistentDataType.STRING, pickaxeHandleId == null? "NONE" : pickaxeHandleId)
                .addUnbreakable()
                .build();
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }

}
