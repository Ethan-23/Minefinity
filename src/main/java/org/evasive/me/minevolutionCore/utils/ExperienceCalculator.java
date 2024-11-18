package org.evasive.me.minevolutionCore.utils;

import org.bukkit.entity.Player;

public class ExperienceCalculator {

    public int getTotalExperience(Player player) {
        int level = player.getLevel();  // Get the player's current level
        float progress = player.getExp();  // Get the player's progress to the next level (0.0 to 1.0)

        // Total experience from previous levels
        int totalExperience = getExperienceForLevel(level);

        // Add experience for current progress toward the next level
        int experienceToNextLevel = getExperienceToNextLevel(level);
        totalExperience += Math.round(progress * experienceToNextLevel);

        return totalExperience;
    }

    // Helper function to calculate the total experience needed to reach a specific level
    private int getExperienceForLevel(int level) {
        if (level <= 16) {
            return (level * level) + (6 * level);
        } else if (level <= 31) {
            return (int) (2.5 * (level * level) - 40.5 * level + 360);
        } else {
            return (int) (4.5 * (level * level) - 162.5 * level + 2220);
        }
    }

    // Helper function to calculate the experience needed to reach the next level from the current level
    private int getExperienceToNextLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

}
