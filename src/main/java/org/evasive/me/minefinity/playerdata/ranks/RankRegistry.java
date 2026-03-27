package org.evasive.me.minefinity.playerdata.ranks;

import org.evasive.me.minefinity.playerdata.model.Rank;
import org.evasive.me.minefinity.playerdata.ranks.data.RankCategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RankRegistry {

    private static final RankRegistry INSTANCE = new RankRegistry();

    public static RankRegistry getInstance() { return INSTANCE; }

    private final Map<String, Rank> ranks = new HashMap<>();

    public void register(Rank rank) {
        ranks.put(rank.getId().toLowerCase(), rank);
    }

    public Optional<Rank> getRank(String id) {
        return Optional.ofNullable(id == null ? null : ranks.get(id.toLowerCase()));
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
