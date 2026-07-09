package org.evasive.me.minefinity.customItems.itembuilder.data.types.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.parts.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.types.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ToolPartComponent;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseToolItem extends BaseCustomItem {

    public BaseToolItem(ItemStack itemStack) {
        super(itemStack);
    }

    public BaseToolItem(String id, Material material, String displayName, Rarity rarity, CustomItemType customItemType) {
        super(id, material, displayName, rarity, customItemType);
    }

    @Override
    protected void registerComponents() {
        super.registerComponents();
        addComponent(new ToolPartComponent());
    }

    public ToolPartComponent partComponent() {
        return getComponent(ToolPartComponent.class);
    }

    public List<PartSlots> getToolSlots() {
        return List.of();
    }

    public void setPart(PartSlots slot, String partId) {
        partComponent().setPart(slot, partId);
    }

    public String getPart(PartSlots slot) {
        return partComponent().getPart(slot);
    }

    public Map<PartSlots, String> getPartMap() {
        return Collections.unmodifiableMap(partComponent().getValue());
    }

    public List<BasePartItem> getInstalledParts() {
        List<BasePartItem> parts = new ArrayList<>();
        for (String partId : getPartMap().values()) {
            if (partId == null || partId.isEmpty())
                continue;
            BaseCustomItem resolved = CustomItemRegistryService.get().getBaseItemById(partId);
            if (resolved instanceof BasePartItem part)
                parts.add(part);
        }
        return parts;
    }

    @Override
    protected List<String> getLore() {
        List<String> lore = super.getLore();

        lore.add("");

        ToolPartComponent parts = partComponent();
        for (PartSlots slot : getToolSlots()) {
            String label = TextConversions.formatItemName(slot.name());
            String partId = parts.getPart(slot);

            if (partId == null || partId.isEmpty()) {
                lore.add("<white>[" + "<gray>Empty " + label + "]");
            } else {
                BaseCustomItem part = CustomItemRegistryService.get().getBaseItemById(partId);
                Rarity rarity = part != null ? part.getRarity() : getRarity();
                lore.add("<white>[" + TextConversions.buildRarityColor(partId, rarity) + "<white>]");
            }
        }

        return lore;
    }
}
