package org.evasive.me.minefinity.admin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.npcs.npc.NpcType;
import org.evasive.me.minefinity.npcs.spawning.CreateNpc;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MineSpawn implements CommandExecutor {

    public MineSpawn(){
        Objects.requireNonNull(Minefinity.getCore().getCommand("minespawn")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player))
            return true;

        if(args.length != 1)
            return true;

        String npcName = args[0];

        NpcType npcType;
        try {
            npcType = NpcType.valueOf(npcName.toUpperCase());
        }catch (IllegalArgumentException e){
            player.sendMessage("Invalid Npc " + npcName);
            return true;
        }

        player.sendMessage("Spawning " + npcName);
        new CreateNpc().createNewNPC(player.getWorld(), player.getLocation(), npcType);
        return true;
    }
}
