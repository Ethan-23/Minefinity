package org.evasive.me.minefinity.ranks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RankRegistry {

    private final Map<String, Rank> ranks = new HashMap<>();

    public void register(Rank rank) {
        ranks.put(rank.getId().toLowerCase(), rank);
    }

    public Rank getRank(String id) {
        if(id == null)
            return null;
        return ranks.get(id.toLowerCase());
    }

    public Collection<Rank> getAllRanks() {
        return ranks.values();
    }

    public boolean containsRank(String id) {
        return ranks.containsKey(id.toLowerCase());
    }

    public Collection<Rank> getRanksByCategory(RankCategory category) {
        return ranks.values().stream()
                .filter(rank -> rank.getCategory() == category)
                .toList();
    }

}
