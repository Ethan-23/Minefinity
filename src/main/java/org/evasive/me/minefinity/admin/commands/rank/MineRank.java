package org.evasive.me.minefinity.admin.commands.rank;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomyAddSub;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomySetSub;
import org.evasive.me.minefinity.admin.commands.economy.sub.EconomySubSub;
import org.evasive.me.minefinity.admin.commands.rank.sub.MineRankAddSub;
import org.evasive.me.minefinity.admin.commands.rank.sub.MineRankRemoveSub;
import org.evasive.me.minefinity.core.SubCommand;
import org.evasive.me.minefinity.ranks.Rank;
import org.evasive.me.minefinity.ranks.RankManager;
import org.evasive.me.minefinity.ranks.RankRegistry;
import org.evasive.me.minefinity.utils.command.CommandFeedback;
import org.evasive.me.minefinity.utils.command.TabCompletionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MineRank implements CommandExecutor, TabCompleter {

    private final RankRegistry rankRegistry;

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public MineRank(RankManager rankManager, RankRegistry rankRegistry) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("minerank")).setExecutor(this);
        this.rankRegistry = rankRegistry;

        register(new MineRankAddSub(rankRegistry, rankManager));
        register(new MineRankRemoveSub(rankRegistry, rankManager));

    }

    private void register(SubCommand sub) {
        subCommands.put(sub.getName().toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(args.length != 3){
            sender.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null){
            sender.sendMessage(CommandFeedback.INVALID_PLAYER);
            return true;
        }

        String subCommand = args[1].toUpperCase();

        if(subCommand.equalsIgnoreCase("set")){
            subCommands.get("addrank").execute(sender, args);
            return true;
        }
        else if(subCommand.equalsIgnoreCase("remove")){
            subCommands.get("removerank").execute(sender, args);
            return true;
        }


        sender.sendMessage(CommandFeedback.INVALID_USAGE);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(args.length == 1){
            return TabCompletionUtils.getOnlinePlayers(args[0]);
        }


        if(args.length == 2){
            return List.of("set", "remove");
        }

        if(args.length == 3){
            return rankRegistry.getAllRanks().stream().map(Rank::getId).collect(Collectors.toList());
        }

        return List.of();
    }
}
