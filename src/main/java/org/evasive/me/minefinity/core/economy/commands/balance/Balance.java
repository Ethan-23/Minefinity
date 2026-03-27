package org.evasive.me.minefinity.core.economy.commands.balance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.economy.EconomyService;
import org.evasive.me.minefinity.core.utils.command.SubCommand;
import org.evasive.me.minefinity.core.economy.commands.balance.sub.BalanceOtherSub;
import org.evasive.me.minefinity.core.economy.commands.balance.sub.BalanceSelfSub;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Balance implements CommandExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public Balance(EconomyService economyService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("balance")).setExecutor(this);

        register(new BalanceSelfSub(economyService));
        register(new BalanceOtherSub(economyService));
    }

    private void register(SubCommand sub) {
        subCommands.put(sub.getName().toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            subCommands.get("self").execute(player, args);
            return true;
        }

        SubCommand sub = subCommands.get("other");
        sub.execute(player, args);
        return true;
    }

}
