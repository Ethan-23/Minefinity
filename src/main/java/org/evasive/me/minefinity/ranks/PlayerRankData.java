package org.evasive.me.minefinity.ranks;

public class PlayerRankData {

    private Rank staffRank;   // nullable
    private Rank donorRank;

    public Rank getStaffRank() {
        return staffRank;
    }

    public Rank getDonorRank() {
        return donorRank;
    }

    public Rank getHighestDisplayRank() {
        if (staffRank != null) return staffRank;
        if (donorRank != null) return donorRank;
        return RankManager.getDefaultRank();
    }

    public boolean hasPermission(String permission) {
        if (staffRank != null && staffRank.hasPermission(permission))
            return true;

        if (donorRank != null && donorRank.hasPermission(permission))
            return true;

        return RankManager.getDefaultRank().hasPermission(permission);
    }

}
