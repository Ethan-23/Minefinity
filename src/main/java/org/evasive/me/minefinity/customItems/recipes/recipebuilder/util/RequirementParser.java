package org.evasive.me.minefinity.customItems.recipes.recipebuilder.util;

import org.bukkit.configuration.ConfigurationSection;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.MilestoneRequirement;
import org.evasive.me.minefinity.customItems.recipes.recipebuilder.data.RecipeRequirement;

import java.util.ArrayList;
import java.util.List;

public class RequirementParser {

    public static List<RecipeRequirement> parse(ConfigurationSection section) {
        List<RecipeRequirement> requirements = new ArrayList<>();

        if (section == null) return requirements;

        // Milestones
        ConfigurationSection milestoneSection = section.getConfigurationSection("milestones");
        if (milestoneSection != null) {
            for (String key : milestoneSection.getKeys(false)) {
                int level = milestoneSection.getInt(key);
                requirements.add(new MilestoneRequirement(key, level));
            }
        }
/*
        // Quests
        List<String> quests = section.getStringList("quests");
        for (String quest : quests) {
            //Add back when quest system is implemented
            //requirements.add(new QuestRequirement(quest));
        }



 */
        return requirements;
    }

}
