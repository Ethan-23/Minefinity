package org.evasive.me.minevolutionCore.npcs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.npc.NpcType;
import org.evasive.me.minevolutionCore.npcs.spawning.CreateNpc;
import org.jetbrains.annotations.NotNull;

public class MSpawnNpc implements CommandExecutor {

    public MSpawnNpc(){
        MinevolutionCore.getCore().getCommand("mspawnnpc").setExecutor(this);
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
