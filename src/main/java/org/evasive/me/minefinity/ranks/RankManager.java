package org.evasive.me.minefinity.ranks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankManager {

    Map<UUID, PlayerRankData> ranks = new HashMap<>();



    public static Rank getDefaultRank() {
        return PlayerRank.DEFAULT.getRank();
    }

}
