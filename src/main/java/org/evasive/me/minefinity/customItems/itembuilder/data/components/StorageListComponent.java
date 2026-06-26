package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ITEMID_STORAGE_LIST_KEY;

public class StorageListComponent implements ItemComponent, EditableComponent<List<String>> {

    List<String> storageList;

    @Override
    public void load(PersistentDataContainer pdc) {
        if(pdc.has(ITEMID_STORAGE_LIST_KEY)){
            String joined = pdc.get(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING);
            List<String> itemIds = new ArrayList<>();
            if (joined != null && !joined.isEmpty()) {
                itemIds = new ArrayList<>(List.of(joined.split(";;")));
            }
            this.storageList = itemIds;
        } else {
            this.storageList = new ArrayList<>();
        }
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING, String.join(";;", this.storageList));
    }

    @Override
    public void addLore(List<String> lore) {

    }

    @Override
    public Class<?> type() {
        return null;
    }

    @Override
    public void setValue(List<String> value) {
        this.storageList = value;
    }

    @Override
    public List<String> getValue() {
        return this.storageList;
    }
}
