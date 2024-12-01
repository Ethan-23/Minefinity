package org.evasive.me.minevolutionCore.npcs.npc;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NPCManager;

public class Archmage {

    final String value = "ewogICJ0aW1lc3RhbXAiIDogMTcyNjMxMzYzMzkwMywKICAicHJvZmlsZUlkIiA6ICJiZDgwZjkzZDBiODk0MjIwODVhMzZkNDFmMzE4ZmM5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZXl3b29kXzA2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2NzNlNDM5MmQwMTFmNTM1MDIxYTA1NzQxMDhhYzFmNzNkNjIxYTBiM2JjZmI0MWI5ZmI2NzRmNDU4MjIxYmEiCiAgICB9CiAgfQp9";
    final String signature = "qgZsbh2NMtGJkzf0eaSuyCFn8GblmrbC/fC3btEwQADkpNCFYHwmm66h7sIAe6dvN8xOCd1FdLxv6wZ8dq+pAj183P2ZV94nWXSQTyE+CZM7Ke71AW2T30uiyRzC7EgrhB25DB0LLJXjPzIsSj2HifO6nQXzjReVKTOlaw4rQB/mQYC27SCuZDYU04v+Ti5gtVmhj3/WBtD3abNprdQXETe+A+b+dS7zyy7LcWhsZwlbmqZ5TB2+XNBUdX80FWyS1G9IK/yWY1SS3H0494T+WzDCTosHgsm3wkiWXe+gHnc560q0IJ5xBgaRQwW3VwO6qVYl+I8+FqE0uWUdNXW1L17IQY7FRSTwYwD/a4wFHno+sya+cIaTe/DM08GF2+8QPl1gSNagVaSgeR9QP31m2IfX+mTfBJ8bbxf+blO8Qb6GiJ8bHVBNGJa9P4MSDGJSK5e7ZOeEnVCXzzaBAHtOei7CfgmPYusbBwlFTLtcHMVr58CWIO0zz874NRlLzMKtxe7j8/xwExv5N9faMUWfDvZEZYVnH0XrLF0a4ucv7nBKs2d82X8JhLFgqu4Jod0mvB4777nR0IXehUgdH5Jp9cIVzTsHS8EsBvSKBJSF/CWgaH47WRDNaPkRH7OTAM/8eEFT7qJXw7xWU9C9vuCA2nuYjiEfl5JFcjY+99C1Ax4=";


    final NPCManager npcManager = MinevolutionCore.getNpcManager();

    public void createNPCPacket(Player player, Location location){

        npcManager.spawnCustomNPC(player, location, "Archmage", new TextureProperty("textures", value, signature));
    }

    public void prepareNPC(Player player){
        //Spawn block master at location of quest
        if(MinevolutionCore.getPlayerManager().getQuestLevel(player) == 0){
            //Spawn at front for tutorial
            //player.sendMessage("Quest 0");
            createNPCPacket(player, new Location(new Vector3d(11.5f, 1f, -6.5f), 45, 0));
        }
    }

}
