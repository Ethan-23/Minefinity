package org.evasive.me.minevolutionCore.npcs.npc;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;

import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.entity.Player;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.npcs.NPCManager;

public class BlockMaster{

    String welcomeText = "Welcome to Minevolution!";
    String welcomeText2 = "Take this pickaxe and start mining the cobblestone block in the center.";
    String welcomeText3 = "Bring me back 10 cobblestone for a reward!";

    final String value = "ewogICJ0aW1lc3RhbXAiIDogMTcxNTE5Mjg4MzM2MywKICAicHJvZmlsZUlkIiA6ICI3OTZjMDBhNmY0MDA0Mjg2OWMyMTIyNjc0ZmI0MWNiZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZWRvbnlhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk0ODc1ZjM4YmExZDQ4Y2ZkZjBiMzcxZDZmZTkzNjliNDQzMDhlYjAzYzhhMmJlOTA4YTk2NjEzZGExODQ4YjgiCiAgICB9CiAgfQp9";
    final String signature = "N/Wo75btRHfqjezlXTlAOKGD5WoZHR4wq1DHOfDk7FiKmiHzsKPfg+ZRbThfVubOleptIT7EtpfCOvdqnWfEAnEjIMDuAXufDk6HO2JXNUVuJDRpPz8C90CjhlJLyARxG/OWL+JeWzJuQv/gnaaTNe2QCS20/WxlyFif5/4dsuOzccuHuCjYKflVZWueJR0l3aBrcRKXQAZW3vBKQpEo6SDXiDNYjYpysV7/v1XZ2i06PT5mYzhIctNUfqEM038CB4/O6tWXmS+CdLLFn57T0/xkL6RKT5A4Bs8vKdResNv42CXaxpU9q9o55f2TH9rt2gDVYPyArLFLAbFAsjsTKnpREdYC9hjQvnQKRfv5zTdKWjlfJdLXEhfl2g8YDmvx2uuFkk0g3rbwFUuY77/vFbVe6y9SuMhHPdIn20umQmkh57xy/gf9EXxyR8D+l4RrlpdmFz1xVas4JL1OegFKP9qnU8/SoFCky9yS7nFjqQaAHqj5dIIKPird11jfJ186MpGm4e8ldDxbWGielHspGPIXvuBzCfGddsH7TnVlOpWssDG2+mwmkOibqAzcIs0tVwU625YlJDFomly59hAE2nB+ZqUDKHYMYodKrcyyUFrw8o6i/DrgpAwJdcEbPR3Dl32k3bnCZihizlWPsyVRV8He80bNvT8wzcyDkvFZju8=";


    final NPCManager npcManager = MinevolutionCore.getNpcManager();

    public void createBlockMasterPacket(Player player, Location location){

        npcManager.spawnCustomNPC(player, location, "BlockMaster", new TextureProperty("textures", value, signature));
    }

    public void prepareBlockMaster(Player player){
        //Spawn block master at location of quest
        if(MinevolutionCore.getPlayerManager().getQuestLevel(player) == 0){
            //Spawn at front for tutorial
            //player.sendMessage("Quest 0");
            createBlockMasterPacket(player, new Location(new Vector3d(-1.5f, 1f, -1.5f), 180, 0));
        }
    }
}
