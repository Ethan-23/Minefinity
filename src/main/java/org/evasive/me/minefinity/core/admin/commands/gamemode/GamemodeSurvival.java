package org.evasive.me.minefinity.core.admin.commands.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.evasive.me.minefinity.core.utils.command.CommandFeedback.SET_PLAYERS_GAMEMODE;

public class GamemodeSurvival implements CommandExecutor {

    public GamemodeSurvival() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("gamemodesurvival")).setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        player.setGameMode(GameMode.SURVIVAL);

        player.sendMessage(SET_PLAYERS_GAMEMODE(player.getGameMode()));


        return true;
    }

}
