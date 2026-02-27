package org.evasive.me.minefinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.service.SpawnService;
import org.evasive.me.minefinity.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Spawn implements CommandExecutor {

    SpawnService spawnService;

    public Spawn() {
        Objects.requireNonNull(Minefinity.getCore().getCommand("spawn")).setExecutor(this);
        spawnService = Minefinity.getCore().getSpawnService();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player))
            return true;

        player.teleport(spawnService.getSpawnLocation(player.getWorld()));
        player.sendMessage(TextConversions.parse("<yellow>You have been teleported to spawn"));

        return true;
    }
}
