package org.evasive.me.minefinity.core.items;

import org.evasive.me.minefinity.utils.TextConversions;

public interface CustomItem {

    String getID();
    BaseCustomItem getBuilder();

    default String getItemName(){
        return TextConversions.formatItemName(getID());
    }

    default String getItemNameRarity(){
        return TextConversions.buildRarityColor(getID(), this.getBuilder().getRarity());
    }

}
