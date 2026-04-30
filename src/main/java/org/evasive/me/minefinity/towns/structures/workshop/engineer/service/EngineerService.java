package org.evasive.me.minefinity.towns.structures.workshop.engineer.service;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.playerdata.model.PlayerData;
import org.evasive.me.minefinity.playerdata.service.PlayerDataService;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.Engineer;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.data.WorkshopMode;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.EngineerTools;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.WorkshopToolsTiers;

public class EngineerService {

    private final PlayerDataService playerDataService;


    public EngineerService(PlayerDataService playerDataService) {
        this.playerDataService = playerDataService;
    }

    private PlayerData getPlayerData(Player player){
        return playerDataService.getPlayerData(player.getUniqueId());
    }

    private Engineer getEngineer(Player player){
        return getPlayerData(player).getEngineer();
    }

    private EngineerTools getEngineerSkill(Engineer engineer, WorkshopMode workshopMode){
        return workshopMode == WorkshopMode.CARPENTRY ? engineer.getCarpentry() : engineer.getStoneworking();
    }

    public void setWorkshopToolType(Player player, WorkshopMode workshopMode, WorkshopToolsTiers toolTier){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolId(toolTier == null ? null : toolTier.name());
    }

    public WorkshopToolsTiers getWorkshopToolType(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        if(engineerSkill.getCurrentToolId() == null) return null;
        return WorkshopToolsTiers.valueOf(engineerSkill.getCurrentToolId());
    }

    public void setWorkshopCurrentResource(Player player, WorkshopMode workshopMode, WorkshopToolsTiers resourceTier){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResourceId(resourceTier == null ? null : resourceTier.name());
        if(resourceTier == null)
            setWorkshopCurrentResourceCount(player, workshopMode, 0);
    }

    public WorkshopToolsTiers getWorkshopCurrentResource(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        if(engineerSkill.getResourceId() == null) return null;
        return WorkshopToolsTiers.valueOf(engineerSkill.getResourceId());
    }

    public void setWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode, int amount){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResourceAmount(amount);
    }

    public int getWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getResourceAmount();
    }

    public void setWorkshopToolDurability(Player player, WorkshopMode workshopMode, int amount){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolDurability(amount);
    }

    public int getWorkshopToolDurability(Player player, WorkshopMode workshopMode){
        EngineerTools engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getCurrentToolDurability();
    }
}
