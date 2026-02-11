package org.evasive.me.minevolutionCore.resourceblock.milestones;

public class Milestone {
    int tierClaim;
    int progress;

    public Milestone(int tierClaim, int progress) {
        this.tierClaim = tierClaim;
        this.progress = progress;
    }

    public int getTierClaim() {
        return tierClaim;
    }

    public void setTierClaim(int tierClaim) {
        this.tierClaim = tierClaim;
    }

    public void addTierClaim(){
        this.tierClaim += 1;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void addProgress(){
        this.progress += 1;
    }
}
