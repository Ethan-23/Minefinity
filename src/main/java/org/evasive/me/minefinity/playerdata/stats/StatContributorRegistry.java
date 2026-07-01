package org.evasive.me.minefinity.playerdata.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry feature modules use to declare a {@link StatContributor} at startup. {@code StatsService}
 * reads it when recalculating a player's stats. Adding a new stat source is one register call in the
 * owning feature module &mdash; no changes to playerdata.
 */
public class StatContributorRegistry {

    private final List<StatContributor> contributors = new ArrayList<>();

    public void register(StatContributor contributor) {
        contributors.add(contributor);
    }

    public List<StatContributor> all() {
        return Collections.unmodifiableList(contributors);
    }
}
