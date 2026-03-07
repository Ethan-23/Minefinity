package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.registry.CustomItemRegistry;
import org.evasive.me.minefinity.rarity.Rarity;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.*;
import java.util.stream.Collectors;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.*;
import static org.evasive.me.minefinity.utils.TextConversions.*;

public class BaseBackpackItem extends BaseCustomItem {

    private static final List<ItemOptions> requiredOptions = List.of(
            ItemOptions.MATERIAL,
            ItemOptions.DISPLAY_NAME,
            ItemOptions.CUSTOM_ITEM_TYPE,
            ItemOptions.MINEFINITY_ID,
            ItemOptions.RARITY,
            ItemOptions.STORAGE_AMOUNT,
            ItemOptions.STORAGE_LIST
    );

    private int storedItemAmount;
    private final List<String> storedItemIdList;

    public BaseBackpackItem(String id, Material material, String displayName, Rarity rarity) {
        super(id, material, displayName, rarity);
        this.storedItemAmount = 640;
        this.storedItemIdList = new ArrayList<>();
    }

    public BaseBackpackItem(ItemStack itemStack) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();

        if(persistentDataContainer.has(STORAGE_AMOUNT_KEY)){
            this.storedItemAmount = persistentDataContainer.get(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER);
        } else {
            this.storedItemAmount = 1;
        }

        if(persistentDataContainer.has(ITEMID_STORAGE_LIST_KEY)){
            String joined = persistentDataContainer.get(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING);
            List<String> itemIds = new ArrayList<>();
            if (joined != null && !joined.isEmpty()) {
                itemIds = new ArrayList<>(List.of(joined.split(";;")));
            }
            this.storedItemIdList = itemIds;
        } else {
            this.storedItemIdList = new ArrayList<>();
        }

    }

    public BaseBackpackItem(String id, Material material, Rarity rarity, CustomItemType itemType, int storedItemAmount, List<String> storedItemIdList) {
        super(id, material, id, rarity, itemType);
        this.storedItemAmount = storedItemAmount;
        this.storedItemIdList = storedItemIdList;
    }

    //Update lore to take multiple lines if too long
    @Override
    protected List<String> getLore() {
        List<String> lore = new ArrayList<>();
        if(getFlavorText().isPresent()) lore.add(getFlavorText().get());
        lore.addAll(List.of(
                "<gray>Item pickups go directly into your backpack",
                "",
                "<gray>Items: "+ getStoredItemIdList().stream().map(string ->
                        "<green>" + (CustomItemRegistry.isRegistered(string) ? CustomItemRegistry.getByID(string).getBaseItem().getDisplayName().replace("\\", "\\\\") : TextConversions.formatItemName(string))
                ).collect(Collectors.joining("<gray>, ")),
                "",
                "<gray>Capacity <yellow>" + getStoredItemAmount() + " of each held item",
                "",
                "<yellow>Right Click to open backpack!",
                "",
                buildItemRarity(getRarity())
        ));
        return lore;
    }

    @Override
    public ItemStack buildItem() {
        return new ItemBuilder(super.buildItem())
                .addPersistentDataContainer(STORAGE_AMOUNT_KEY, PersistentDataType.INTEGER, storedItemAmount)
                .addPersistentDataContainer(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING, String.join(";;", getStoredItemIdList()))
                .build();
    }

    public void setStoredItemAmount(int storedItemAmount) {
        this.storedItemAmount = storedItemAmount;
    }

    public void changeStoredItemIdList(String storedItemId) {
        if(storedItemIdList.contains(storedItemId))
            storedItemIdList.remove(storedItemId);
        else
            storedItemIdList.add(storedItemId);
    }

    public int getStoredItemAmount() {
        return storedItemAmount;
    }

    public List<String> getStoredItemIdList() {
        return storedItemIdList;
    }

    public static List<ItemOptions> getRequiredOptions() {
        return requiredOptions;
    }
}
