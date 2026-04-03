package org.evasive.me.minefinity.core.worlds;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class GenerateCustomWorlds {

    public void init(){
        createIfMissing("world");
        createIfMissing("world_mines");
        createIfMissing("world_nether");
        createIfMissing("world_the_end");
    }

    private void createIfMissing(String name) {
        if (Bukkit.getWorld(name) != null) return;

        WorldCreator wc = new WorldCreator(name);
        wc.generator(new VoidGenerator());

        Bukkit.createWorld(wc);
    }

}
