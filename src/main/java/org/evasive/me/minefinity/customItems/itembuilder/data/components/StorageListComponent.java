package org.evasive.me.minefinity.customItems.itembuilder.data.components;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.ItemBuilder;
import org.evasive.me.minefinity.customItems.itembuilder.data.ItemComponent;
import org.evasive.me.minefinity.customItems.itembuilder.gui.EditContext;

import java.util.ArrayList;
import java.util.List;

import static org.evasive.me.minefinity.customItems.itembuilder.util.CustomItemKeys.ITEMID_STORAGE_LIST_KEY;

public class StorageListComponent implements ItemComponent, EditableComponent<List<String>> {

    private List<String> storageList = new ArrayList<>();

    @Override
    public void load(PersistentDataContainer pdc) {
        List<String> itemIds = new ArrayList<>();
        if (pdc.has(ITEMID_STORAGE_LIST_KEY)) {
            String joined = pdc.get(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING);
            if (joined != null && !joined.isEmpty()) {
                itemIds = new ArrayList<>(List.of(joined.split(";;")));
            }
        }
        this.storageList = itemIds;
    }

    @Override
    public void save(ItemBuilder builder) {
        builder.addPersistentDataContainer(ITEMID_STORAGE_LIST_KEY, PersistentDataType.STRING, String.join(";;", this.storageList));
    }

    @Override
    public void addLore(List<String> lore) {
    }

    @Override
    public void setValue(List<String> value) {
        this.storageList = value == null ? new ArrayList<>() : value;
    }

    @Override
    public List<String> getValue() {
        return this.storageList;
    }

    public void toggle(String itemId) {
        if (!storageList.remove(itemId)) {
            storageList.add(itemId);
        }
    }

    @Override
    public void openEditor(EditContext ctx) {
        ctx.promptString(input -> {
            String id = input.trim();
            if (!id.isEmpty()) {
                toggle(id);
                ctx.player().sendMessage(TextConversions.parse("<gray>Storage list now: <white>" + storageList));
            }
        });
    }
}
