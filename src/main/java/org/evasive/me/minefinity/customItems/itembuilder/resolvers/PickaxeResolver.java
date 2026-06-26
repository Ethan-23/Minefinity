package org.evasive.me.minefinity.customItems.itembuilder.resolvers;

import org.evasive.me.minefinity.customItems.itembuilder.data.ToolItemData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePartItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.tools.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

public class PickaxeResolver {

    public final CustomItemRegistryService customItemRegistryService;

    public PickaxeResolver(CustomItemRegistryService customItemRegistryService) {
        this.customItemRegistryService = customItemRegistryService;
    }

    public ToolItemData resolve(BasePickaxeItem item) {
        return new ToolItemData(item);
    }

    private BasePartItem getComponent(String id) {
        if (id == null) return null;

        BaseCustomItem item = customItemRegistryService.getBaseItemById(id);

        if (!(item instanceof BasePartItem component)) {
            throw new IllegalStateException("Item with ID '" + id + "' is not a pickaxe component");
        }

        return component;
    }

}
