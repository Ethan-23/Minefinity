package org.evasive.me.minefinity.playerdata.model;

import org.evasive.me.minefinity.playerdata.ranks.data.RankCategory;

import java.util.Collections;
import java.util.HashSet;
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
        this.permissions = new HashSet<>(permissions);
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
        return Collections.unmodifiableSet(permissions);
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission) || permissions.contains("*");
    }

    public RankCategory getCategory() {
        return category;
    }
}
