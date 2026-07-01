package org.evasive.me.minefinity.towns.structures.workshop.engineer.data;
import org.evasive.me.minefinity.towns.structures.workshop.engineer.tools.EngineerTools;
import org.evasive.me.minefinity.playerdata.component.PlayerDataComponent;


public class Engineer implements PlayerDataComponent {

    EngineerTools carpentry;
    EngineerTools stoneworking;

    public Engineer(){
        this.carpentry = new EngineerTools();
        this.stoneworking = new EngineerTools();
    }

    public Engineer(EngineerTools carpentry, EngineerTools stoneworking) {
        this.carpentry = carpentry;
        this.stoneworking = stoneworking;
    }

    public EngineerTools getCarpentry() {
        return carpentry;
    }

    public void setCarpentry(EngineerTools carpentry) {
        this.carpentry = carpentry;
    }

    public EngineerTools getStoneworking() {
        return stoneworking;
    }

    public void setStoneworking(EngineerTools stoneworking) {
        this.stoneworking = stoneworking;
    }
}
