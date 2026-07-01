package org.evasive.me.minefinity.playerdata.component;

/**
 * Marker interface for a slice of per-player data that is owned by a feature module
 * (e.g. the engineer, the smelter) but stored and persisted centrally by playerdata.
 * <p>
 * Feature modules define their own implementation and register it with the
 * {@link PlayerDataComponentRegistry} at startup. playerdata never references the
 * concrete type - it only ever sees this interface plus the registered {@link Class}.
 */
public interface PlayerDataComponent {
}
