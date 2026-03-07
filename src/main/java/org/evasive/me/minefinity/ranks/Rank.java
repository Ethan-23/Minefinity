package org.evasive.me.minefinity.ranks;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Rank {

    private final String id;
    private final String prefix;
    private final int weight;
    private final Set<String> permissions;
    private final RankCategory category;

    public Rank(String id, String prefix, int weight, Set<String> permissions, RankCategory category) {
        this.id = id;
        this.prefix = prefix;
        this.weight = weight;
        this.permissions = permissions;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getWeight() {
        return weight;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public RankCategory getCategory() {
        return category;
    }
}
