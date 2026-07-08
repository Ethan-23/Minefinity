package org.evasive.me.minefinity.mining.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evasive.me.minefinity.mining.abilities.MiningAbilityRegistry;
import org.evasive.me.minefinity.mining.data.MiningDataMap;
import org.evasive.me.minefinity.mining.data.SelectedBlockMap;

import java.util.UUID;

public class MiningQuitListener implements Listener {

    private final SelectedBlockMap selectedBlockMap;
    private final MiningDataMap miningDataMap;
    private final MiningAbilityRegistry miningAbilityRegistry;

    public MiningQuitListener(SelectedBlockMap selectedBlockMap, MiningDataMap miningDataMap, MiningAbilityRegistry miningAbilityRegistry) {
        this.selectedBlockMap = selectedBlockMap;
        this.miningDataMap = miningDataMap;
        this.miningAbilityRegistry = miningAbilityRegistry;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        selectedBlockMap.removePlayer(uuid);
        miningDataMap.removeAllForPlayer(uuid);
        miningAbilityRegistry.removePlayerData(uuid);
    }

}
