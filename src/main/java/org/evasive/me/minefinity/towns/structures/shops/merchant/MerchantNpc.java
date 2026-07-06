package org.evasive.me.minefinity.towns.structures.shops.merchant;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.economy.EconomyService;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.backpack.BackpackService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.shops.merchant.gui.MerchantGui;

public class MerchantNpc implements NpcBehavior {

    private final CustomItemRegistryService customItemRegistryService;
    private final EconomyService economyService;
    private final BackpackService backpackService;

    public MerchantNpc(CustomItemRegistryService customItemRegistryService, EconomyService economyService, BackpackService backpackService) {
        this.customItemRegistryService = customItemRegistryService;
        this.economyService = economyService;
        this.backpackService = backpackService;
    }

    @Override
    public void onInteract(Player player) {
        new MerchantGui(player, customItemRegistryService, economyService, backpackService).open();
    }

    @Override
    public void onTick() {

    }

}
