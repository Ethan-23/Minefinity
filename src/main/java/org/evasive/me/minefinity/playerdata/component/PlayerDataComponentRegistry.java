package org.evasive.me.minefinity.playerdata.component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry every feature module uses to declare its per-player data slice.
 * <p>
 * Populated once at startup (each module registers in its {@code enable()}), then read by
 * {@code PlayerData} (for defaults) and {@code PlayerDataRepository} (for persistence).
 * Adding a new feature means one {@link #register} call in that feature's module - no
 * changes to playerdata itself.
 */
public class PlayerDataComponentRegistry {

    // LinkedHashMap so column order in generated SQL is stable
    private final Map<String, ComponentType<?>> byId = new LinkedHashMap<>();

    public <T extends PlayerDataComponent> void register(String id, Class<T> type, Supplier<T> defaultFactory) {
        if (byId.containsKey(id))
            throw new IllegalArgumentException("Player data component already registered for id: " + id);
        byId.put(id, new ComponentType<>(id, type, defaultFactory));
    }

    public Collection<ComponentType<?>> all() {
        return byId.values();
    }
}
