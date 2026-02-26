package org.evasive.me.minefinity.workshop.service;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.service.DirtyPlayerService;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.tools.WorkshopToolsTiers;
import org.evasive.me.minefinity.workshop.Engineer;
import org.evasive.me.minefinity.workshop.tools.EngineerTools;

public class EngineerService {

    private final PlayerManager playerManager;
    private final DirtyPlayerService dirtyPlayerService;


    public EngineerService(PlayerManager playerManager) {
        this.playerManager = playerManager;
        dirtyPlayerService = Minefinity.getCore().getDirtyPlayerService();
    }

    private Engineer getEngineer(Player player){
        return playerManager.get(player).getEngineer();
    }

    private EngineerTools getEngineerSkill(Engineer engineer, WorkshopMode workshopMode){
        return workshopMode == WorkshopMode.CARPENTRY ? engineer.getCarpentry() : engineer.getStoneworking();
    }

    public void setWorkshopToolType(Player player, WorkshopMode workshopMode, WorkshopToolsTiers toolTier){
        dirtyPlayerService.addDirtyPlayer(player);
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolType(toolTier);
    }

    public WorkshopToolsTiers getWorkshopToolType(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getCurrentToolType();
    }

    public void setWorkshopCurrentResource(Player player, WorkshopMode workshopMode, WorkshopToolsTiers resourceTier){
        dirtyPlayerService.addDirtyPlayer(player);
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResource(resourceTier);
        if(resourceTier == null)
            setWorkshopCurrentResourceCount(player, workshopMode, 0);
    }

    public WorkshopToolsTiers getWorkshopCurrentResource(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getResource();
    }

    public void setWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode, int amount){
        dirtyPlayerService.addDirtyPlayer(player);
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResourceAmount(amount);
    }

    public int getWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getResourceAmount();
    }

    public void setWorkshopToolDurability(Player player, WorkshopMode workshopMode, int amount){
        dirtyPlayerService.addDirtyPlayer(player);
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolDurability(amount);
    }

    public int getWorkshopToolDurability(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getCurrentToolDurability();
    }
}
