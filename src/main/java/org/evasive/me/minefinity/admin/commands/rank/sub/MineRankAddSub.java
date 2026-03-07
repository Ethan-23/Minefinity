package org.evasive.me.minefinity.admin.commands.rank.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.ranks.Rank;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.ranks.RankRegistry;
import org.evasive.me.minefinity.utils.command.CommandFeedback;

public class MineRankAddSub implements SubCommand {

    RankRegistry rankRegistry;
    RankManager rankManager;

    public MineRankAddSub(RankRegistry rankRegistry, RankManager rankManager) {
        this.rankRegistry = rankRegistry;
        this.rankManager = rankManager;
    }

    @Override
    public String getName() {
        return "addRank";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        String rankName = args[2].toUpperCase();

        if(!rankRegistry.containsRank(rankName)){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        Rank rank = rankRegistry.getRank(rankName);

        rankManager.setRank(target, rank);
    }
}
