package org.evasive.me.minefinity.admin.commands.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.evasive.me.minefinity.utils.command.CommandFeedback.SET_PLAYERS_GAMEMODE;

public class GamemodeSpectator implements CommandExecutor {

    public GamemodeSpectator() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("gamemodespectator")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        player.setGameMode(GameMode.SPECTATOR);

        player.sendMessage(SET_PLAYERS_GAMEMODE(player.getGameMode()));


        return true;
    }
}
