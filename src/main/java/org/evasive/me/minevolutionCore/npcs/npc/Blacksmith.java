package org.evasive.me.minevolutionCore.npcs.npc;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NPCManager;

public class Blacksmith {
    final String value = "ewogICJ0aW1lc3RhbXAiIDogMTY2Mzk3MTg5NjY3MSwKICAicHJvZmlsZUlkIiA6ICI4ODBiZWMwYTE0MmM0YzRlYTJlZjliMTFiMTBkNWNiNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmZ2FiIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk3NzI2NDJmZmNjZmM5ZTExYjM1MGM4NzRmMmM4NDY3OGZjMDgwNDRiNTFlN2E4ZTNhMDkxOWY4Zjc4OGVkOWEiCiAgICB9CiAgfQp9";
    final String signature = "knDUDPmPHs7Yym5z61XHFMdzcs1VTJOZC75FzzhSRwjGnALxLABhBQ+6MsFtXj2K//jFjp9taG1YixHG1GxgS6JJaVpL4UbKm8H3pQOOJn8ac5ppbM0bNZYaTlqqL3eHx5WtCdBBoLD7I0mvDXeoJWyN7tv6YvowsE6AV4ISVW6r5usSYipiG3EbChCKNDGg/DqS+g7gSZ771A/eYhhx5nqfgVoLWHsmTC3ppa0NA/gE8We73uM2V9WNKzTtF7Z44zOFyf+wD/eLOfJ/7rB967qFyFf7r0cdTvK1NJZJ3qY7bT75xi6586mqCbhCIwRUYo+XZB+e3XBnACCGZJIPMx718jZn6nkVzb6YAtj4BjOvqv9gPgKsPhefTG5zqrirngfCqM2MbV6X1a65mjEb4OuhXeey/R2LSFVYbO2FKSeH5NISK4xj42s+n8kC3rGdaFEpraDXCLxAFpI6fU4YhCKKoZA9LMctScIA4J+OR1rh4VCIAJypXOyd0H/cjqcao42//oF9grqkYbjm2Lx8Fw4fhhHVZcb+cK9/Wk4NaIpZSP7GzfZS0OjxQY+aRuchvbkBRmb7RFhAfWwSLgvps0pMwnwMZdlTLcYNWDagS74VAZJ132FM2giECDnZ4Xc9KyNwa1s9dTuyffdU0W/fMqKzMJ23tW2JJDQCVOJgRGc=";


    final NPCManager npcManager = MinevolutionCore.getNpcManager();

    public void createNPCPacket(Player player, Location location){

        npcManager.spawnCustomNPC(player, location, "Blacksmith", new TextureProperty("textures", value, signature));
    }

    public void prepareNPC(Player player){
        //Spawn block master at location of quest
        if(MinevolutionCore.getPlayerManager().getQuestLevel(player) == 0){
            //Spawn at front for tutorial
            //player.sendMessage("Quest 0");
            createNPCPacket(player, new Location(new Vector3d(-14.5f, 1f, -14.5f), -45, 0));
        }
    }
}
