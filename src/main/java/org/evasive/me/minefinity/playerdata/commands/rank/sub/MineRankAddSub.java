package org.evasive.me.minefinity.playerdata.commands.rank.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.command.SubCommand;
import org.evasive.me.minefinity.playerdata.model.Rank;
import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.playerdata.ranks.data.RankCategory;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.Optional;
import java.util.UUID;

public class MineRankAddSub implements SubCommand {

    RankRegistry rankRegistry;
    RankService playerRankService;

    public MineRankAddSub(RankService playerRankService) {
        this.rankRegistry = RankRegistry.getInstance();
        this.playerRankService = playerRankService;
    }

    @Override
    public String getName() {
        return "addRank";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            sender.sendMessage(CommandFeedback.INVALID_PLAYER);
            return;
        }

        String rankName = args[2].toUpperCase();

        Optional<Rank> rank = rankRegistry.getRank(rankName);

        if(rank.isEmpty()){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        RankCategory rankCategory = rank.get().getCategory();
        String rankId = rank.get().getId();
        UUID targetUUID = target.getUniqueId();

        if(rankCategory == RankCategory.DONOR){
            playerRankService.setDonorRank(targetUUID, rankId);
        }else if(rankCategory == RankCategory.STAFF){
            playerRankService.setStaffRank(targetUUID, rankId);
        }
    }
}
