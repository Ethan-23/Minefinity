package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resolves the concrete part items installed in a tool from its {@link ToolPartComponent} map
 * (slot -&gt; part id). Used to total a tool's stats and collect its abilities.
 */
public class ToolItemData {

    private final List<BasePartItem> parts = new ArrayList<>();

    public ToolItemData(BaseToolItem tool) {
        for (Map.Entry<PartSlots, String> entry : tool.getPartMap().entrySet()) {
            String partId = entry.getValue();
            if (partId == null || partId.isEmpty()) {
                continue;
            }

            BaseCustomItem resolved = CustomItemRegistryService.get().getBaseItemById(partId);
            if (resolved instanceof BasePartItem part) {
                parts.add(part);
            }
        }
    }

    public List<BasePartItem> getParts() {
        return parts;
    }

    /** Kept for existing callers (mining / stats). */
    public List<BasePartItem> getPickaxeParts() {
        return parts;
    }
}
