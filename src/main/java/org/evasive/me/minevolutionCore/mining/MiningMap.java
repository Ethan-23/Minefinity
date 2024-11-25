package org.evasive.me.minevolutionCore.mining;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningMap {

    private final Map<UUID, MiningPlayer> playerMiningMap = new HashMap<>();

    public MiningPlayer getMiningPlayer(Player player){
        return playerMiningMap.get(player.getUniqueId());
    }

    public boolean hasPlayer(Player player){
        UUID uuid = player.getUniqueId();
        return playerMiningMap.containsKey(uuid);
    }

    public void addPlayer(Player player, MiningPlayer miningPlayer){
        UUID uuid = player.getUniqueId();
        playerMiningMap.put(uuid, miningPlayer);
    }

    public void removePlayer(Player player){
        UUID uuid = player.getUniqueId();
        playerMiningMap.remove(uuid);
    }

    public boolean isMining(Player player){
        return getMiningPlayer(player).isMining();
    }

    public void setMining(Player player, boolean mining){
        getMiningPlayer(player).setMining(mining);
    }

    public boolean isSwinging(Player player){
        return getMiningPlayer(player).isSwinging();
    }

    public void setSwinging(Player player, boolean swinging){
        getMiningPlayer(player).setSwinging(swinging);
    }

    public float getProgress(Player player){
        return getMiningPlayer(player).getProgress();
    }

    public void addProgress(Player player, float amount){
        getMiningPlayer(player).setProgress(getMiningPlayer(player).getProgress() + amount);
    }

    public void setProgress(Player player, float amount){
        getMiningPlayer(player).setProgress(amount);
    }

    public BukkitTask getMiningTask(Player player){
        return getMiningPlayer(player).getMiningTask();
    }

    public void setMiningTask(Player player, BukkitTask task){
        getMiningPlayer(player).setMiningTask(task);
    }

    public BukkitRunnable getSwingCooldown(Player player){
        return getMiningPlayer(player).getSwingCooldown();
    }

    public void setSwingCooldown(Player player, BukkitRunnable task){
        getMiningPlayer(player).setSwingCooldown(task);
    }


}
