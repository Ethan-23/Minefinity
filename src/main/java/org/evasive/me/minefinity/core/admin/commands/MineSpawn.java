package org.evasive.me.minefinity.core.admin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.npcs.NpcInstanceMap;
import org.evasive.me.minefinity.core.npcs.NpcType;
import org.evasive.me.minefinity.core.npcs.spawning.CreateNpc;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MineSpawn implements CommandExecutor {

    private NpcInstanceMap npcInstanceMap;

    public MineSpawn(NpcInstanceMap npcInstanceMap){
        Objects.requireNonNull(Minefinity.getCore().getCommand("minespawn")).setExecutor(this);
        this.npcInstanceMap = npcInstanceMap;
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
        new CreateNpc(npcInstanceMap).createNewNPC(player.getWorld(), player.getLocation(), npcType);
        return true;
    }
}
