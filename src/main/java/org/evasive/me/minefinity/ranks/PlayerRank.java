package org.evasive.me.minefinity.ranks;

import java.util.Set;

public enum PlayerRank {
    DEFAULT("", Set.of()),
    VIP("VIP", Set.of()),
    VIPPLUS("VIP+", Set.of()),
    MVP("MVP", Set.of()),
    MVPPLUS("MVP+", Set.of()),
    ;

    private Rank rank;

    PlayerRank(String prefix, Set<String> permission) {
        this.rank = new Rank(this.name(), prefix, this.ordinal(), permission);
    }

    public Rank getRank() {
        return rank;
    }
}
