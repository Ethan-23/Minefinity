package org.evasive.me.minefinity.mining.blocks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.evasive.me.minefinity.core.registry.BlockTypeRegistryService;
import org.evasive.me.minefinity.mining.blocks.PlayerBlockTiers;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;

/**
 * Seeds a player's starting-world block tier the first time it is missing.
 * <p>
 * Block-tier data is a mining-owned {@link PlayerBlockTiers} component, so its no-arg default
 * factory can't reach the {@link BlockTypeRegistryService}. Mining owns the registry, so it does
 * the defaulting here instead. Idempotent: only seeds when the starting world isn't present, so
 * it's safe on every join (new players and players whose column predates this component).
 */
public class PlayerBlockTiersJoinListener implements Listener {

    private final PlayerDataService playerDataService;
    private final BlockTypeRegistryService blockTypeRegistryService;

    public PlayerBlockTiersJoinListener(PlayerDataService playerDataService, BlockTypeRegistryService blockTypeRegistryService) {
        this.playerDataService = playerDataService;
        this.blockTypeRegistryService = blockTypeRegistryService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData data = playerDataService.getPlayerData(event.getPlayer().getUniqueId());
        if (data == null) return;

        PlayerBlockTiers tiers = data.get(PlayerBlockTiers.class);
        if (tiers == null) return;

        String worldId = blockTypeRegistryService.getWorldList().getFirst();
        if (tiers.hasWorldUnlocked(worldId)) return;

        // Starting world begins on tier 0
        tiers.setUnlockedBlockTier(worldId, 0);
        tiers.setSelectedBlockTier(worldId, blockTypeRegistryService.getBlockIdByTier(worldId, 0));
    }
}
