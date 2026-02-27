package org.evasive.me.minefinity.admin.commands.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GamemodeCreative implements CommandExecutor {

    public GamemodeCreative() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("gamemodecreative")).setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player))
            return true;

        player.setGameMode(GameMode.CREATIVE);

        return true;
    }
}
