package org.evasive.me.minefinity.ranks;

import java.util.HashSet;
import java.util.Set;

public class PlayerRanks {

    private Rank staffRank;
    private Rank donorRank;

    public PlayerRanks(Rank staffRank, Rank donorRank) {
        this.staffRank = staffRank;
        this.donorRank = donorRank;
    }

    public Rank getStaffRank() {
        return staffRank;
    }

    public void setStaffRank(Rank staffRank) {
        this.staffRank = staffRank;
    }

    public Rank getDonorRank() {
        return donorRank;
    }

    public void setDonorRank(Rank donorRank) {
        this.donorRank = donorRank;
    }

    public String getCombinedPrefix() {
        String staffPrefix = staffRank != null ? staffRank.getPrefix() : "";
        String donorPrefix = donorRank != null ? donorRank.getPrefix() : "";
        return staffPrefix + donorPrefix;
    }

    public Set<String> getAllPermissions() {
        Set<String> all = new HashSet<>();
        if (staffRank != null) all.addAll(staffRank.getPermissions());
        if (donorRank != null) all.addAll(donorRank.getPermissions());
        return all;
    }

}
