package org.evasive.me.minevolutionCore.npcs.npc;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NPCManager;

public class ShopKeeper {

    final String value = "ewogICJ0aW1lc3RhbXAiIDogMTcwODM1NjQ4MDQ4NywKICAicHJvZmlsZUlkIiA6ICJkMDQ3ZGY3ZjMzMzk0MmM1OTA3ODY5ZjIwYmI0YzlkMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJleHBsaXloIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIwNGI2M2E3ZDYzMWExZWZmNzNlOGNlZGRiMjc4MjRkZDVlZTI2ZDZkOTUzZjc4NjQzMDA0NzVjY2QyODZhNjEiCiAgICB9CiAgfQp9";
    final String signature = "UXMlw/KT7665sLVyReM99/3Bj7ssyIkQNqeKfPt+EjXUSV0sWFvyUphJuOBuBoJiVItwXa5w8DLmYzTjLpdBP1u+HRf2zBaFTpufEZuSTNp0ycTi02VJB7XmnEbSWlaMYtkraCY1/MyPzW06Cj7ovInciPiQ+Qzl3DOeT9TzSoqh6VJkmlzWXuZGLLo7O+qOLNJAkJKI9f6eeqEPf75AzbKIRBgJfNpleT0gtKN2+gGEfLvXUTNKcOfZOSEkmS5Or+r494LdQINpBHzqyLn933Tk2xGOaJAf77S+cgkfEjI23zieAfURI+7rmfViFelEKIZArIPI3b9c8wpl1MGpWZjP/VMZGjRuw60WtaRCwzrkRAdIrsTCbqgHU1RVYrs5P9i4HCCKj6tHr/C14OL0gQCSoEi8OO+xZAXDDW6OIvogq6XVOAlbv+AnXfQSLGsULkLGPqr3BO0MvkM0x6Dp++eyJ+ZCNVmZvAC+7BqlBboxOI5ODq76cP2Iy1CuSXHcm6tKcktaBcno5qw9KfJyEvh4L0eFEZDXukjvKsS9wX4TCFg+nDHTlYPED9LuW4FVOraQ0JEGVVdUCU4l26aFKq4z6G1uGtMPFL/26rhgyNdkSZXxn76I0VfsWxOFtGIYzrYmLl8f4XayrITwnM3qyzaJAuxFlozGU6EyiP/CzgM=";

    final NPCManager npcManager = MinevolutionCore.getNpcManager();

    public void createNPCPacket(Player player, Location location){

        npcManager.spawnCustomNPC(player, location, "Shopkeeper", new TextureProperty("textures", value, signature));
    }

    public void prepareNPC(Player player){
        //Spawn block master at location of quest
        if(MinevolutionCore.getPlayerManager().getQuestLevel(player) == 0){
            //Spawn at front for tutorial
            //player.sendMessage("Quest 0");
            createNPCPacket(player, new Location(new Vector3d(-15.5f, 1f, 11.5f), -90, 0));
        }
    }

}
