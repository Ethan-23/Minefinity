package org.evasive.me.minefinity.playerdata.commands.rank.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.core.utils.command.SubCommand;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;
import org.evasive.me.minefinity.playerdata.model.Rank;
import org.evasive.me.minefinity.playerdata.ranks.data.RankCategory;
import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.evasive.me.minefinity.playerdata.service.RankService;

import java.util.UUID;

public class MineRankRemoveSub implements SubCommand {

    RankRegistry rankRegistry;
    RankService playerRankService;

    public MineRankRemoveSub(RankService playerRankService) {
        this.rankRegistry = RankRegistry.getInstance();
        this.playerRankService = playerRankService;
    }

    @Override
    public String getName() {
        return "removeRank";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            sender.sendMessage(CommandFeedback.INVALID_PLAYER);
            return;
        }

        UUID targetUUID = target.getUniqueId();


        String rankName = args[2].toUpperCase();

        Rank rank = rankRegistry.getRank(rankName).orElse(null);

        if(rank == null) {
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        RankCategory rankCategory = rank.getCategory();

        PlayerRanks playerRanks = playerRankService.getRanks(targetUUID);

        if(rankCategory == RankCategory.STAFF && playerRanks.getStaffRank() == null ||
                rankCategory == RankCategory.DONOR && playerRanks.getDonorRank() == null){
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        if(!rankName.equalsIgnoreCase(playerRanks.getDonorRankId()) && !rankName.equalsIgnoreCase(playerRanks.getStaffRankId())) {
            sender.sendMessage(CommandFeedback.INVALID_RANK);
            return;
        }

        if(rankCategory == RankCategory.STAFF) {
            playerRankService.removeStaffRank(targetUUID);
        }else if(rankCategory == RankCategory.DONOR) {
            playerRankService.removeDonorRank(targetUUID);
        }
    }
}
