package org.evasive.me.minefinity.customItems.itembuilder.service;

import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.customItems.itembuilder.data.PickaxeData;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.itembuilder.resolvers.PickaxeResolver;

public class PickaxeService {

    private final PickaxeResolver resolver;

    public PickaxeService(PickaxeResolver resolver) {
        this.resolver = resolver;
    }

    public PickaxeData getData(BasePickaxeItem item) {
        return resolver.resolve(item);
    }

    public ItemStack buildItem(BasePickaxeItem item) {
        PickaxeData data = resolver.resolve(item);
        return item.buildItem(data);
    }

}
