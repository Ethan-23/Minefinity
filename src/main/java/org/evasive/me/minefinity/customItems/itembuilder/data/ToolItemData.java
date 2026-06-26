package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BaseToolItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToolItemData {

    List<BasePartItem> componentParts = new ArrayList<>();

    public ToolItemData(BaseToolItem baseToolItem) {

        for(Map.Entry<PartSlots, String> entry : baseToolItem.getPartMap().entrySet()) {
            if (entry.getValue() == null)
                continue;

            BaseCustomItem baseCustomItem = CustomItemRegistryService.get().getRegisteredBaseItem(entry.getValue());

            //if(!(baseCustomItem instanceof BasePartItem baseToolComponent) || baseToolComponent.getComponent().contains(entry.getKey()))
            //    continue;

            //componentParts.add(baseToolComponent);
        }
    }

    public List<BasePartItem> getPickaxeParts() {
        return componentParts;
    }
}
