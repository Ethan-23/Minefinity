package org.evasive.me.minevolutionCore.customItems;

import org.evasive.me.minevolutionCore.core.items.CustomItem;


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
