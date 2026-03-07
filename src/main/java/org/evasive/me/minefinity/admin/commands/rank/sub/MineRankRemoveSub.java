package org.evasive.me.minefinity.admin.commands.rank.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.ranks.Rank;
import org.evasive.me.minefinity.ranks.RankCategory;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.ranks.RankRegistry;
import org.evasive.me.minefinity.utils.command.CommandFeedback;

import java.util.UUID;

public class MineRankRemoveSub implements SubCommand {

    RankRegistry rankRegistry;
    RankManager rankManager;

    public MineRankRemoveSub(RankRegistry rankRegistry, RankManager rankManager) {
        this.rankRegistry = rankRegistry;
        this.rankManager = rankManager;
    }

    @Override
    public String getName() {
        return "removeRank";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        UUID targetUUID = target.getUniqueId();
        String rankName = args[2].toUpperCase();

        if(!rankRegistry.containsRank(rankName)){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        Rank rank = rankRegistry.getRank(rankName);
        RankCategory rankCategory = rank.getCategory();

        if(rankCategory == RankCategory.STAFF && rankManager.getStaffRank(targetUUID) == null ||
                rankCategory == RankCategory.DONOR && rankManager.getDonorRank(targetUUID) == null){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        if(!rankName.equalsIgnoreCase(rankManager.getRanks(targetUUID).getDonorRank().getId()) && !rankName.equalsIgnoreCase(rankManager.getRanks(targetUUID).getStaffRank().getId())){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        rankManager.removeRank(target, rank);
    }
}
