package org.evasive.me.minefinity.core.spawn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.spawn.service.SpawnService;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Spawn implements CommandExecutor {

    SpawnService spawnService;

    public Spawn(SpawnService spawnService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("spawn")).setExecutor(this);
        this.spawnService = spawnService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player))
            return true;

        player.teleport(spawnService.getSpawnLocation());
        player.sendMessage(TextConversions.parse("<yellow>You have been teleported to spawn"));

        return true;
    }
}
