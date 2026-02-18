package org.evasive.me.minefinity.workshop.data;

public class Engineer {

    EngineerSkill carpentry;
    EngineerSkill stoneworking;

    public Engineer(){
        this.carpentry = new EngineerSkill();
        this.stoneworking = new EngineerSkill();
    }

    public Engineer(EngineerSkill carpentry, EngineerSkill stoneworking) {
        this.carpentry = carpentry;
        this.stoneworking = stoneworking;
    }

    public EngineerSkill getCarpentry() {
        return carpentry;
    }

    public void setCarpentry(EngineerSkill carpentry) {
        this.carpentry = carpentry;
    }

    public EngineerSkill getStoneworking() {
        return stoneworking;
    }

    public void setStoneworking(EngineerSkill stoneworking) {
        this.stoneworking = stoneworking;
    }
}
