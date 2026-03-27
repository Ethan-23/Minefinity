package org.evasive.me.minefinity.playerdata.model;

import org.evasive.me.minefinity.playerdata.ranks.RankRegistry;

import java.util.*;

public class PlayerRanks {

    private String staffRankId;
    private String donorRankId;

    public PlayerRanks(String staffRankId, String donorRankId) {
        this.staffRankId = staffRankId;
        this.donorRankId = donorRankId;
    }

    public String getStaffRankId() {
        return staffRankId;
    }

    public Rank getDonorRank(){
        return RankRegistry.getInstance().getRank(donorRankId).orElse(null);
    }

    public Rank getStaffRank(){
        return RankRegistry.getInstance().getRank(staffRankId).orElse(null);
    }

    public void setStaffRankId(String staffRankId) {
        this.staffRankId = staffRankId;
    }

    public String getDonorRankId() {
        return donorRankId;
    }

    public void setDonorRankId(String donorRankId) {
        this.donorRankId = donorRankId;
    }

    public String getCombinedPrefix() {
        String staffPrefix = getStaffRank() != null ? getStaffRank().getPrefix() : "";
        String donorPrefix = getDonorRank() != null ? getDonorRank().getPrefix() : "";
        return (staffPrefix + " " + donorPrefix).trim();
    }

    public Set<String> getAllPermissions() {
        Set<String> all = new HashSet<>();
        if (getStaffRank() != null) all.addAll(getStaffRank().getPermissions());
        if (getDonorRank() != null) all.addAll(getDonorRank().getPermissions());
        return Collections.unmodifiableSet(all);
    }

    public boolean hasPermission(String permission) {
        return (getStaffRank() != null && getStaffRank().hasPermission(permission)) ||
                (getDonorRank() != null && getDonorRank().hasPermission(permission));
    }

}
