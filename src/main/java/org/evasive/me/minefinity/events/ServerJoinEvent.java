package org.evasive.me.minefinity.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.admin.commands.Vanish;
import org.evasive.me.minefinity.customItems.CustomItemRegistry;
import org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem;
import org.evasive.me.minefinity.customItems.pickaxe.PickaxeComponent;
import org.evasive.me.minefinity.utils.TextConversions;

import java.util.UUID;

import static org.evasive.me.minefinity.customItems.pickaxe.BasePickaxeItem.*;

public class ServerJoinEvent implements Listener {

    Vanish vanish = Minefinity.getCore().getVanish();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.joinMessage(vanish.isVanished(player) ? null : TextConversions.parse("<gray>[<#555555>+<gray>] <#55FFFF>" + player.getName()));
        UUID uuid = player.getUniqueId();
        if(Minefinity.getCore().getPlayerManager().getAll().containsKey(uuid)) return;
        firstJoin(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.quitMessage(vanish.isVanished(player) ? null : TextConversions.parse("<gray>[<#555555>-<gray>] <#55FFFF>" + player.getName()));
    }

    private void firstJoin(Player player){
        player.getInventory().addItem(buildStartingPickaxe());
        player.sendMessage(TextConversions.parse("<#55FFFF>Welcome to Minefinity!"));
        Minefinity.getCore().getPlayerManager().registerPlayer(player.getUniqueId());
    }

    private ItemStack buildStartingPickaxe(){
        BasePickaxeItem starterPickaxe = (BasePickaxeItem) CustomItemRegistry.getByID("WOODEN_TEMPLATE").getBuilder();
        ItemStack pickaxe = starterPickaxe.buildItem();
        pickaxe = starterPickaxe.addPart(pickaxe, headKey, PickaxeComponent.WOOD_HEAD.getID());
        pickaxe = starterPickaxe.addPart(pickaxe, coreKey, PickaxeComponent.WOOD_CORE.getID());
        pickaxe = starterPickaxe.addPart(pickaxe, handleKey, PickaxeComponent.WOOD_HANDLE.getID());
        return pickaxe;
    }

}
