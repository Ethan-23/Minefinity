package org.evasive.me.minefinity.playerdata.stats;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.data.Stats;

import java.util.Map;

/**
 * A source of stat bonuses for a player, contributed by a feature module (for example equipment
 * stats from customItems or milestone bonuses from mining).
 * <p>
 * Features implement this interface and register it with the {@link StatContributorRegistry} at
 * startup. {@code StatsService} sums every registered contributor without ever naming a feature
 * type &mdash; the dependency points from feature to playerdata, not the other way around.
 */
public interface StatContributor {

    /**
     * @param player the player to compute a contribution for
     * @return this contributor's stat bonuses for the player (may be empty)
     */
    Map<Stats, Integer> contribute(Player player);
}
