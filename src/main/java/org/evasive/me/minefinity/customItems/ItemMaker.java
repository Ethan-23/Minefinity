package org.evasive.me.minefinity.customItems;

import org.evasive.me.minefinity.core.items.CustomItem;


public class ItemMaker {

    /**
     * Builds all custom items
     */
    public void init(){
        for (CustomItem item : CustomItemRegistry.getAllItems()) {
            item.getBuilder().buildItem();
        }
    }

}
