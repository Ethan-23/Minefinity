package org.evasive.me.minefinity.ranks;

import java.util.Set;

public class Rank {

    private final String id;
    private final String prefix;
    private final int weight; // priority (higher = more important)
    private final Set<String> permissions;

    public Rank(String id, String prefix, int weight, Set<String> permissions) {
        this.id = id;
        this.prefix = prefix;
        this.weight = weight;
        this.permissions = permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public String getPrefix() {
        return prefix;
    }

    public int getWeight() {
        return weight;
    }

}
