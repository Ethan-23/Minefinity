package org.evasive.me.minefinity.customItems.recipes.recipebuilder.data;
import org.evasive.me.minefinity.mining.milestones.BlockMilestone;

import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.model.PlayerData;

public class MilestoneRequirement implements RecipeRequirement{

    private final String blockId;
    private final int tier;

    public MilestoneRequirement(String blockId, int tier) {
        this.blockId = blockId;
        this.tier = tier;
    }

    @Override
    public boolean isMet(PlayerData player) {
        return player.get(BlockMilestone.class).getTier(blockId) >= tier;
    }

    @Override
    public String getDisplay() {
        return TextConversions.formatItemName(blockId) + " Milestone: <white>" + TextConversions.intToRoman(tier);
    }
}
