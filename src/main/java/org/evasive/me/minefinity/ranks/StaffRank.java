package org.evasive.me.minefinity.ranks;

import net.kyori.adventure.text.Component;
import org.evasive.me.minefinity.utils.TextConversions;

public enum StaffRank {
    HELPER(TextConversions.parse("<bold><purple>Helper")),
    MOD(TextConversions.parse("<bold><green>Mod")),
    ADMIN(TextConversions.parse("<bold><red>Admin"));

    final Component tag;

    StaffRank(Component tag) {
        this.tag = tag;
    }
}
