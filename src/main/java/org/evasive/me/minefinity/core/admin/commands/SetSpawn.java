package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.spawn.service.SpawnService;
import org.evasive.me.minefinity.core.utils.command.CommandFeedback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetSpawn implements CommandExecutor {

    SpawnService spawnService;

    public SetSpawn(SpawnService spawnService) {
        Objects.requireNonNull(Minefinity.getCore().getCommand("setspawn")).setExecutor(this);
        this.spawnService = spawnService;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return true;

        spawnService.setSpawnLocation(player.getLocation());

        player.sendMessage(CommandFeedback.SET_SPAWN);
        return true;
    }
}
