package org.evasive.me.minefinity.towns.structures.townhall.mayor;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.npcs.framework.NpcBehavior;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.service.RecipeService;
import org.evasive.me.minefinity.customItems.registry.service.CustomItemRegistryService;
import org.evasive.me.minefinity.mining.milestones.MilestoneService;
import org.evasive.me.minefinity.towns.structures.service.StructureService;

public class MayorNpc implements NpcBehavior {

    private final CustomItemRegistryService customItemRegistryService;
    private final StructureService structureService;
    private final RecipeService recipeService;
    private final MilestoneService milestoneService;

    public MayorNpc(CustomItemRegistryService customItemRegistryService, StructureService structureService, RecipeService recipeService, MilestoneService milestoneService) {
        this.customItemRegistryService = customItemRegistryService;
        this.structureService = structureService;
        this.recipeService = recipeService;
        this.milestoneService = milestoneService;
    }

    @Override
    public void onInteract(Player player) {
        new MayorGui(player, customItemRegistryService, structureService, recipeService, milestoneService).open();
    }

    @Override
    public void onTick() {

    }
}
