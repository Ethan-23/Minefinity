package org.evasive.me.minefinity.customItems.itembuilder.resolvers;

import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

public class PickaxeResolver {

    public final CustomItemRegistryService customItemRegistryService;

    public PickaxeResolver(CustomItemRegistryService customItemRegistryService) {
        this.customItemRegistryService = customItemRegistryService;
    }

    public PickaxeData resolve(BasePickaxeItem item) {

        BasePickaxeComponent head = getComponent(item.getPickaxeHeadId());
        BasePickaxeComponent core = getComponent(item.getPickaxeCoreId());
        BasePickaxeComponent handle = getComponent(item.getPickaxeHandleId());

        return new PickaxeData(head, core, handle);
    }

    private BasePickaxeComponent getComponent(String id) {
        if (id == null) return null;

        BaseCustomItem item = customItemRegistryService.getBaseItemById(id);

        if (!(item instanceof BasePickaxeComponent component)) {
            throw new IllegalStateException("Item with ID '" + id + "' is not a pickaxe component");
        }

        return component;
    }

}
