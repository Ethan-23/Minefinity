package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.bukkit.Bukkit;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BaseCustomItem;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeComponent;
import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;

import java.util.ArrayList;
import java.util.List;

public class PickaxeData {

    BasePickaxeComponent[] pickaxeParts = new BasePickaxeComponent[3];

    public PickaxeData(BasePickaxeItem basePickaxeItem) {
        List<String> componentIds = new ArrayList<>();
        componentIds.add(basePickaxeItem.getPickaxeHeadId());
        componentIds.add(basePickaxeItem.getPickaxeCoreId());
        componentIds.add(basePickaxeItem.getPickaxeHandleId());
        List<CustomItemType> customItemTypes = List.of(CustomItemType.PICKAXE_HEAD, CustomItemType.PICKAXE_CORE, CustomItemType.PICKAXE_HANDLE);

        for(int i = 0; i < componentIds.size(); i++) {
            if (componentIds.get(i) == null)
                continue;
            BaseCustomItem baseCustomItem = CustomItemRegistryService.get().getRegisteredBaseItem(componentIds.get(i));
            if (!(baseCustomItem instanceof BasePickaxeComponent basePickaxeComponent) || baseCustomItem.getCustomItemType() != customItemTypes.get(i))
                continue;
            //throw new IllegalStateException("Invalid component: " + baseCustomItem.getID());
            pickaxeParts[i] = (basePickaxeComponent);
        }
    }

    public BasePickaxeComponent[] getPickaxeParts() {
        return pickaxeParts;
    }
}
