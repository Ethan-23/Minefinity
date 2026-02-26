package org.evasive.me.minefinity.workshop.service;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;

public class EngineerCraftingService {

    private final EngineerService engineerService;
    private final Player player;
    private final WorkshopMode workshopMode;

    public EngineerCraftingService(EngineerService engineerService, Player player, WorkshopMode workshopMode) {
        this.engineerService = engineerService;
        this.player = player;
        this.workshopMode = workshopMode;
    }

    public boolean canCraftNextTool(){
        WorkshopToolsTiers resourceType = engineerService.getWorkshopCurrentResource(player, workshopMode);
        if(resourceType == null) return false;
        int amount = engineerService.getWorkshopCurrentResourceCount(player, workshopMode);
        return amount >= 4;
    }

    public void craftTool(){
        WorkshopToolsTiers resourceType = engineerService.getWorkshopCurrentResource(player, workshopMode);
        engineerService.setWorkshopToolType(player, workshopMode, resourceType);
        engineerService.setWorkshopToolDurability(player, workshopMode, resourceType.getDurability());
        int total = engineerService.getWorkshopCurrentResourceCount(player, workshopMode);
        engineerService.setWorkshopCurrentResourceCount(player, workshopMode, total-4);

        if(total-4 == 0){
            engineerService.setWorkshopCurrentResource(player, workshopMode, null);
            engineerService.setWorkshopCurrentResourceCount(player, workshopMode, 0);
        }
    }

}
