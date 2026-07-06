package org.evasive.me.minefinity.playerdata.lunar;

import com.google.common.collect.Lists;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.core.utils.TextConversions;
import org.evasive.me.minefinity.playerdata.model.PlayerRanks;

import java.util.Optional;
import java.util.UUID;

public class LunarNametag {

    public void createPlayerNametag(Player player, PlayerRanks ranks) {
        UUID uuid = player.getUniqueId();

        NametagModule nametagModule = Apollo.getModuleManager().getModule(NametagModule.class);
        if (nametagModule == null) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Minefinity.getCore(), () -> {

            Nametag nametag = Nametag.builder()
                    .lines(Lists.newArrayList(
                            TextConversions.parse(
                                    ranks.getWeightedPrefix()
                                            + "<reset><white> " + player.getName()
                            ),
                            TextConversions.parse("<white>" + (int) player.getHealth() + "<red>❤")
                    ))
                    .build();

            for (Player online : Bukkit.getOnlinePlayers()) {
                Optional<ApolloPlayer> onlineApollo = Apollo.getPlayerManager().getPlayer(online.getUniqueId());

                onlineApollo.ifPresent(apolloPlayer2 -> {
                    nametagModule.overrideNametag(apolloPlayer2, uuid, nametag);
                });
            }

        }, 2L); // 2 ticks delay to ensure Apollo knows the player
    }

}
