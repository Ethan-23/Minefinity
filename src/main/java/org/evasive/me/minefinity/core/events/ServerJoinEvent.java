package org.evasive.me.minefinity.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.admin.service.VanishService;
import org.evasive.me.minefinity.customItems.framework.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.scoreboard.Scoreboard;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.UUID;

public class ServerJoinEvent implements Listener {

    VanishService vanishService = Minefinity.getCore().getVanishService();
    Scoreboard scoreboard = Minefinity.getCore().getScoreboard();

    private static final String WELCOME_MESSAGE = "<#55FFFF>Welcome to Minefinity!";
    private static final String PLAYER_JOIN = "<gray>[<#555555>+<gray>] <#55FFFF>";
    private static final String PLAYER_LEAVE = "<gray>[<#555555>-<gray>] <#55FFFF>";

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(!Minefinity.getCore().getPlayerManager().getAll().containsKey(uuid))
            firstJoin(player);
        event.joinMessage(vanishService.isVanished(player) ? null : TextConversions.parse(PLAYER_JOIN + player.getName()));
        scoreboard.setupMainScoreboard(player);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.quitMessage(vanishService.isVanished(player) ? null : TextConversions.parse(PLAYER_LEAVE + player.getName()));
    }

    private void firstJoin(Player player){
        player.getInventory().addItem(buildStartingPickaxe());
        player.sendMessage(TextConversions.parse(WELCOME_MESSAGE));
        Minefinity.getCore().getPlayerManager().registerPlayer(player.getUniqueId());
    }

    private ItemStack buildStartingPickaxe(){
        BasePickaxeItem starterPickaxe = new BasePickaxeItem(CustomItemRegistry.getByID("WOODEN_TEMPLATE").getBuilder().buildItem());
        return starterPickaxe.buildItem();
    }
}
