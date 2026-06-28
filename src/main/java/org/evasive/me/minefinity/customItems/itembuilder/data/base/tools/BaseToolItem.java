package org.evasive.me.minefinity.customItems.itembuilder.data.base.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.core.rarity.Rarity;
import org.evasive.me.minefinity.customItems.itembuilder.data.CustomItemType;
import org.evasive.me.minefinity.customItems.itembuilder.data.PartSlots;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.components.ToolPartComponent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base for tools (pickaxe, axe, ...). A tool's installed parts are stored in a single
 * {@link ToolPartComponent}; the helpers here delegate to it so there is one source of truth.
 */
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

    /**
     * The part slots this tool exposes for editing/installing. Subclasses override with their
     * tool-specific slots (e.g. a pickaxe uses PICKAXE_HEAD/CORE/HANDLE).
     */
    public List<PartSlots> getToolSlots() {
        return List.of(PartSlots.HEAD, PartSlots.CORE, PartSlots.HANDLE);
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
}
