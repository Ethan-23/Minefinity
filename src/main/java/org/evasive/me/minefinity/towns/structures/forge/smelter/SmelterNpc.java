package org.evasive.me.minefinity.towns.structures.forge.smelter;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.towns.structures.forge.smelter.gui.SmelterGui;
import org.evasive.me.minefinity.towns.structures.forge.smelter.recipes.SmelterRecipeManager;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterHandler;
import org.evasive.me.minefinity.towns.structures.forge.smelter.service.SmelterService;

public class SmelterNpc implements NpcBehavior {

    private final CustomItemRegistryService customItemRegistryService;
    private final SmelterService smelterService;
    private final SmelterHandler smelterHandler;
    private final SmelterRecipeManager smelterRecipeManager;

    public SmelterNpc(CustomItemRegistryService customItemRegistryService, SmelterService smelterService, SmelterHandler smelterHandler, SmelterRecipeManager smelterRecipeManager) {
        this.customItemRegistryService = customItemRegistryService;
        this.smelterService = smelterService;
        this.smelterHandler = smelterHandler;
        this.smelterRecipeManager = smelterRecipeManager;
    }

    @Override
    public void onInteract(Player player) {
        new SmelterGui(player, customItemRegistryService, smelterService, smelterHandler, smelterRecipeManager).open();
    }

    @Override
    public void onTick() {

    }
}
