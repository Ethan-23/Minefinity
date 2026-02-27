package org.evasive.me.minefinity.ranks;

import net.kyori.adventure.text.Component;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.Set;

public enum StaffRank {
    HELPER("HELPER", Set.of()),
    MOD("MOD", Set.of()),
    ADMIN("ADMIN", Set.of())
    ;

    private Rank rank;

    StaffRank(String prefix, Set<String> permission) {
        this.rank = new Rank(this.name(), prefix, this.ordinal(), permission);
    }

    public Rank getRank() {
        return rank;
    }
}
