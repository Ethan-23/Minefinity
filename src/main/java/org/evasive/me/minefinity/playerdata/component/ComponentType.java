package org.evasive.me.minefinity.playerdata.component;

import java.util.function.Supplier;

/**
 * Describes a registered player-data component.
 *
 * @param id             stable key, also used as the database column name (so existing
 *                       data stays compatible)
 * @param type           concrete component class, used by Gson for (de)serialization
 * @param defaultFactory supplies a fresh default instance for new players / missing columns
 */
public record ComponentType<T extends PlayerDataComponent>(
        String id,
        Class<T> type,
        Supplier<T> defaultFactory
) {
}
