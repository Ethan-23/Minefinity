package org.evasive.me.minefinity.core.admin.commands.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.evasive.me.minefinity.core.utils.command.CommandFeedback.SET_PLAYERS_GAMEMODE;

public class Gamemode implements CommandExecutor {

    private static final Map<String, GameMode> GAMEMODES = Map.ofEntries(
            Map.entry("creative", GameMode.CREATIVE),
            Map.entry("c", GameMode.CREATIVE),
            Map.entry("1", GameMode.CREATIVE),

            Map.entry("survival", GameMode.SURVIVAL),
            Map.entry("s", GameMode.SURVIVAL),
            Map.entry("0", GameMode.SURVIVAL),

            Map.entry("adventure", GameMode.ADVENTURE),
            Map.entry("a", GameMode.ADVENTURE),
            Map.entry("2", GameMode.ADVENTURE),

            Map.entry("spectator", GameMode.SPECTATOR),
            Map.entry("sp", GameMode.SPECTATOR),
            Map.entry("3", GameMode.SPECTATOR)
    );

    public Gamemode(){
        Objects.requireNonNull(Minefinity.getCore().getCommand("gamemode")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        if(args.length != 1){
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        String input = args[0].toLowerCase(Locale.ROOT);
        GameMode mode = GAMEMODES.get(input);

        if (mode == null) {
            player.sendMessage(CommandFeedback.INVALID_USAGE);
            return true;
        }

        player.setGameMode(mode);
        player.sendMessage(SET_PLAYERS_GAMEMODE(player.getGameMode()));

        return true;
    }
}
