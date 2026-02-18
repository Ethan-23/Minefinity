package org.evasive.me.minefinity.player.sevices;

import org.bukkit.entity.Player;
import org.evasive.me.minefinity.player.PlayerManager;
import org.evasive.me.minefinity.workshop.WorkshopMode;
import org.evasive.me.minefinity.workshop.WorkshopToolsTiers;
import org.evasive.me.minefinity.workshop.data.Engineer;
import org.evasive.me.minefinity.workshop.data.EngineerSkill;

public class EngineerService {

    private final PlayerManager playerManager;

    public EngineerService(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    private Engineer getEngineer(Player player){
        return playerManager.get(player).getEngineer();
    }

    private EngineerSkill getEngineerSkill(Engineer engineer, WorkshopMode workshopMode){
        return workshopMode == WorkshopMode.CARPENTRY ? engineer.getCarpentry() : engineer.getStoneworking();
    }

    public void setWorkshopToolType(Player player, WorkshopMode workshopMode, WorkshopToolsTiers toolTier){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolType(toolTier);
    }

    public WorkshopToolsTiers getWorkshopToolType(Player player, WorkshopMode workshopMode){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getCurrentToolType();
    }

    public void setWorkshopCurrentResource(Player player, WorkshopMode workshopMode, WorkshopToolsTiers resourceTier){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResource(resourceTier);
        if(resourceTier == null)
            setWorkshopCurrentResourceCount(player, workshopMode, 0);
    }

    public WorkshopToolsTiers getWorkshopCurrentResource(Player player, WorkshopMode workshopMode){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getResource();
    }

    public void setWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode, int amount){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setResourceAmount(amount);
    }

    public int getWorkshopCurrentResourceCount(Player player, WorkshopMode workshopMode){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getResourceAmount();
    }

    public void setWorkshopToolDurability(Player player, WorkshopMode workshopMode, int amount){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        engineerSkill.setCurrentToolDurability(amount);
    }

    public int getWorkshopToolDurability(Player player, WorkshopMode workshopMode){
        EngineerSkill engineerSkill = getEngineerSkill(getEngineer(player), workshopMode);
        return engineerSkill.getCurrentToolDurability();
    }
}
