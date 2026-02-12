package org.evasive.me.minefinity.resourceblock.milestones;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.resourceblock.milestones.gui.MilestoneGui;

import java.util.Map;

import static org.evasive.me.minefinity.Minefinity.playerManager;

public class MilestoneFunctions {

    /**
     * Retrieves players block milestones.
     * @param player
     * @return map of players block milestones
     */
    public static Map<Material, Milestone> getPlayerMilestones(Player player){
        return playerManager.getPlayerData(player).getBlockMilestones();
    }

    /**
     * Retrieves players material milestone progress.
     * @param player
     * @param material Material type of milestone.
     * @return int progress of players material milestone.
     */
    public static int getMaterialMilestone(Player player, Material material){
        return getPlayerMilestones(player).get(material).getProgress();
    }

    /**
     * Sets the players milestone progress
     * @param player
     * @param material Material type of milestone.
     * @param amount Amount milestone is being set to
     */
    public static void setMaterialMilestone(Player player, Material material, int amount){
        getPlayerMilestones(player).get(material).setProgress(amount);
    }

    /**
     * Adds 1 to the players milestone progress
     * @param player
     * @param material Material type of milestone.
     */
    public static void addMaterialMilestone(Player player, Material material){
        getPlayerMilestones(player).get(material).addProgress();
    }

    /**
     * Sets the players claimed material milestone tier
     * @param player
     * @param material Material type of milestone
     * @param amount Tier being set to
     */
    public static void setClaimedTier(Player player, Material material, int amount){
        getPlayerMilestones(player).get(material).setTierClaim(amount);
    }

    /**
     * Adds 1 to the players material milestone tier
     * @param player
     * @param material Material type of milestone
     */
    public static void increaseClaimedTier(Player player, Material material){
        getPlayerMilestones(player).get(material).addTierClaim();
    }

    /**
     * Gets the players highest material milestone claimed tier
     * @param player
     * @param material Material type of milestone
     * @return highest claim material milestone tier
     */
    public static int getClaimedTier(Player player, Material material){
        return getPlayerMilestones(player).get(material).getTierClaim();
    }

    /**
     * Gets the players material milestone tier
     * @param player
     * @param material Material type of milestone
     * @return return the players material milestone tier
     */
    public static int getTier(Player player, Material material){
        int progress = getMaterialMilestone(player, material);
        for (int temp : MilestoneGui.TRACK_VALUES){
            if(temp > progress)
                return MilestoneGui.TRACK_VALUES.indexOf(temp);
        }
        return MilestoneGui.TRACK_VALUES.size();
    }

    /**
     * Sets the players selected material
     * @param player
     * @param material Material being set
     */
    public static void setSelectedMaterial(Player player, Material material){
        playerManager.setMaterialSelection(player, material);
    }

    /**
     * Gets the players selected material
     * @param player
     * @return Players selected material type
     */
    public static Material getSelectedMaterial(Player player){
        return playerManager.getMaterialSelection(player);
    }

    /**
     * Removes the players selected material type
     * @param player
     */
    public static void removeSelectedMaterial(Player player){
        playerManager.removeMaterialSelection(player);
    }
}
